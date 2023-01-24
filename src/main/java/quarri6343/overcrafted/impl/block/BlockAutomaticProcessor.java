package quarri6343.overcrafted.impl.block;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.item.interfaces.IBurntOCItem;
import quarri6343.overcrafted.api.item.interfaces.IOCItem;
import quarri6343.overcrafted.api.item.interfaces.IProcessedOCItem;
import quarri6343.overcrafted.common.PlaceItemHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCResourcePackData;
import quarri6343.overcrafted.impl.item.OCItems;

import java.util.HashMap;
import java.util.Map;

/**
 * アイテムを時間をかけて別のアイテムに加工できるブロック
 */
public class BlockAutomaticProcessor extends BlockTable implements IBlockProcessor {

    private static final Vector armorStandOffset = new Vector(0.5, -0.5, 0.5);

    /**
     * ブロックとそのブロックの加工中のタスクのマップ
     */
    private static final Map<Block, BukkitTask> progressionMap = new HashMap<>();

    public BlockAutomaticProcessor(Material material) {
        super(material);
        onPickUp.add(block -> cancelProcessing(block, true));
    }

    public void onRightClick(PlayerInteractEvent event) {
        super.onRightClick(event);

        if (canProcess(event.getClickedBlock()))
            this.continueProcessing(event.getClickedBlock());
    }

    @Override
    public boolean canProcess(Block block) {
        if (progressionMap.containsKey(block))
            return false;

        ItemStack itemStack = PlaceItemHandler.getItem(block);
        if (itemStack == null)
            return false;

        IOCItem iocItem = OCItems.toOCItem(itemStack);
        if (iocItem == null)
            return false;


        for (OCItems ocItems : OCItems.values()) {
            if (!(ocItems.get() instanceof IProcessedOCItem)) {
                continue;
            }

            if (((IProcessedOCItem) ocItems.get()).getProcessType() != this)
                continue;

            if (((IProcessedOCItem) ocItems.get()).getIngredient().get().equals(iocItem)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void continueProcessing(Block block) {
        BukkitTask task = new AutomaticProcessingRunnable(block).runTaskTimer(Overcrafted.getInstance(), 0, 1);
        progressionMap.put(block, task);
    }

    @Override
    public void cancelProcessing(Block block, boolean removeFromMap) {
        Location location = block.getLocation();
        location.add(armorStandOffset);
        ArmorStand armorStand = null;
        for (Entity nearbyEntity : location.getNearbyEntities(0.1, 0.1, 0.1)) {
            if (nearbyEntity.getType() == EntityType.ARMOR_STAND) {
                armorStand = (ArmorStand) nearbyEntity;
            }
        }
        if (armorStand != null)
            armorStand.remove();

        if (progressionMap.get(block) != null) {
            progressionMap.get(block).cancel();
            if (removeFromMap)
                progressionMap.remove(block);
        }
    }

    @Override
    public void cancelAllProcesses() {
        progressionMap.forEach((block, task) -> cancelProcessing(block, false));
        progressionMap.clear();
    }

    private static class AutomaticProcessingRunnable extends BukkitRunnable {
        private int progression = 0;
        private final Block block;
        private final IBlockProcessor processor;

        public AutomaticProcessingRunnable(Block block) {
            this.block = block;
            processor = (IBlockProcessor) OCBlocks.toOCBlock(block);
        }

        @Override
        public void run() {
            Location location = block.getLocation();
            location.add(armorStandOffset);
            ArmorStand armorStand = null;
            for (Entity nearbyEntity : location.getNearbyEntities(0.1, 0.1, 0.1)) {
                if (nearbyEntity.getType() == EntityType.ARMOR_STAND) {
                    armorStand = (ArmorStand) nearbyEntity;
                }
            }

            progression++;

            if (armorStand == null || armorStand.getCustomName() == null) {
                if (armorStand != null)
                    armorStand.remove();
                armorStand = location.getWorld().spawn(location, ArmorStand.class);
                armorStand.setVisible(false);
                armorStand.setCanMove(false);
                armorStand.setCanTick(false);
                armorStand.setCustomNameVisible(true);
            }

            if (progression < OCData.cookingTime) {
                normalProcessing(armorStand);
            } else if (progression == OCData.cookingTime) {
                finishProcessing();
            } else if (progression < OCData.cookingTime + OCData.burnTime) {
                burntProcessing();
            } else {
                finishBurntProcessing(armorStand);
            }
        }

        private void normalProcessing(ArmorStand armorStand) {
            int filledPercent = progression / (OCData.cookingTime / 10);
            for (OCResourcePackData.ProgressBarFont font : OCResourcePackData.ProgressBarFont.values()) {
                if (font.getFilledPercentage() == filledPercent) {
                    armorStand.customName(Component.text(font.get_char()).font(OCResourcePackData.progressBarFontName));
                    break;
                }
            }
        }

        private void finishProcessing() {
            ItemStack itemStack = PlaceItemHandler.getItem(block);
            IOCItem iocItem = OCItems.toOCItem(itemStack);

            for (OCItems ocItems : OCItems.values()) {
                if (!(ocItems.get() instanceof IProcessedOCItem)) {
                    continue;
                }

                if (((IProcessedOCItem) ocItems.get()).getProcessType() != processor)
                    continue;

                if (((IProcessedOCItem) ocItems.get()).getIngredient().get().equals(iocItem)) {
                    PlaceItemHandler.pickUpItem(block);
                    PlaceItemHandler.placeItem(block, ocItems.get().getItemStack());
                    break;
                }
            }
        }

        private void burntProcessing() {
            if (progression % 5 == 0) {
                block.getWorld().spawnParticle(Particle.SMOKE_NORMAL, block.getLocation().add(0.5, 1.2, 0.5), 1);
            }
        }

        private void finishBurntProcessing(ArmorStand armorStand) {
            cancel();
            armorStand.remove();
            ItemStack itemStack = PlaceItemHandler.getItem(block);
            IOCItem iocItem = OCItems.toOCItem(itemStack);

            for (OCItems ocItems : OCItems.values()) {
                if (!(ocItems.get() instanceof IBurntOCItem)) {
                    continue;
                }

                if (((IBurntOCItem) ocItems.get()).getIngredient().equals(iocItem)) {
                    PlaceItemHandler.pickUpItem(block);
                    PlaceItemHandler.placeItem(block, ocItems.get().getItemStack());
                    break;
                }
            }
            progressionMap.remove(block);
        }
    }
}
