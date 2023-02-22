package quarri6343.overcrafted.impl.block;

import lombok.Getter;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.block.IBlockProcessor;
import quarri6343.overcrafted.api.item.IBurntOCItem;
import quarri6343.overcrafted.api.item.IOCItem;
import quarri6343.overcrafted.api.item.IProcessedOCItem;
import quarri6343.overcrafted.core.handler.PlaceItemHandler;
import quarri6343.overcrafted.impl.item.OCItems;
import quarri6343.overcrafted.impl.task.AutomaticProcessingRunnable;
import quarri6343.overcrafted.utils.ItemCreator;

import java.util.HashMap;
import java.util.Map;

/**
 * アイテムを時間をかけて別のアイテムに加工できるブロック
 */
public class BlockAutomaticProcessor extends BlockTable implements IBlockProcessor {

    public static final Vector armorStandOffset = new Vector(0.5, -0.5, 0.5);
    @Getter
    private final String progressionNBTID;
    @Getter
    private final Sound processingSound;
    @Getter
    private final Particle processingParticle;
    
    /**
     * ブロックとそのブロックの加工中のタスクのマップ
     */
    private static final Map<Block, AutomaticProcessingRunnable> progressionMap = new HashMap<>();

    public BlockAutomaticProcessor(Material material, String progressionNBTID, Sound processingSound, Particle processingParticle) {
        super(material);
        this.progressionNBTID = progressionNBTID;
        this.processingSound = processingSound;
        this.processingParticle = processingParticle;
        onPickUp.add((block, player) -> cancelProcessing(block, player, true));
    }

    public void onRightClick(PlayerInteractEvent event) {
        super.onRightClick(event);

        if (canProcess(event.getClickedBlock()))
            this.continueProcessing(event.getClickedBlock());
    }

    @Override
    public boolean canProcess(Block block) {
        if (progressionMap.containsKey(block) && !progressionMap.get(block).isCancelled())
            return false;

        ItemStack itemStack = PlaceItemHandler.getItem(block);
        if (itemStack == null)
            return false;

        IOCItem iocItem = OCItems.toOCItem(itemStack);
        if (iocItem == null)
            return false;


        for (OCItems ocItems : OCItems.values()) {
            if (ocItems.get() instanceof IProcessedOCItem
                && ((IProcessedOCItem) ocItems.get()).getProcessType() == this
                && ((IProcessedOCItem) ocItems.get()).getIngredient().get().equals(iocItem)) {
                return true;
            }

            if (ocItems.get() instanceof IBurntOCItem
                    && ((IBurntOCItem) ocItems.get()).getIngredient().equals(iocItem)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void continueProcessing(Block block) {
        AutomaticProcessingRunnable task = new AutomaticProcessingRunnable(block, processingSound, processingParticle);
        task.runTaskTimer(Overcrafted.getInstance(),0, 1);
        progressionMap.put(block, task);
    }

    @Override
    public void cancelProcessing(Block block, Player player, boolean removeFromMap) {
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
            int progression = progressionMap.get(block).getProgression();
            if (removeFromMap)
                progressionMap.remove(block);
            
            if(player != null){
                player.setItemInHand(new ItemCreator(player.getItemInHand()).setIntNBT(progressionNBTID, progression).create());
            }
        }
    }

    @Override
    public void cancelAllProcesses() {
        progressionMap.forEach((block, task) -> cancelProcessing(block, null, false));
        progressionMap.clear();
    }
}
