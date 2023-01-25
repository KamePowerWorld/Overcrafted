package quarri6343.overcrafted.impl.block;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import quarri6343.overcrafted.api.item.interfaces.IBurntOCItem;
import quarri6343.overcrafted.api.item.interfaces.IOCItem;
import quarri6343.overcrafted.api.item.interfaces.IProcessedOCItem;
import quarri6343.overcrafted.common.PlaceItemHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCResourcePackData;
import quarri6343.overcrafted.impl.item.OCItems;
import quarri6343.overcrafted.utils.ItemCreator;

import static quarri6343.overcrafted.impl.block.BlockAutomaticProcessor.armorStandOffset;

public class AutomaticProcessingRunnable extends BukkitRunnable {
    
    @Getter
    private int progression = 0;
    private final Block block;
    private final IBlockProcessor processor;

    public AutomaticProcessingRunnable(Block block) {
        this.block = block;
        processor = (IBlockProcessor) OCBlocks.toOCBlock(block);

        ItemStack itemStack = PlaceItemHandler.getItem(block);
        if(processor != null){
            Integer progression = new ItemCreator(itemStack).getIntNBT(processor.getProgressionNBTID());
            if(progression != null)
                this.progression = progression; 
        }
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
            normalProcessing(armorStand);
            finishProcessing();
        } else if (progression < OCData.cookingTime + OCData.burnTime) {
            burntProcessing(armorStand);
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

    private void burntProcessing(ArmorStand armorStand) {
        if(progression < OCData.cookingTime + (OCData.burnTime / 5)){
            armorStand.customName(Component.text(OCResourcePackData.ProgressBarFont._10.get_char()).font(OCResourcePackData.progressBarFontName));
        }
        else if(progression < OCData.cookingTime + (OCData.burnTime / 5) * 2){
            armorStand.customName(Component.text(OCResourcePackData.ProgressBarFont._overheat1.get_char()).font(OCResourcePackData.progressBarFontName));
        }
        else if(progression < OCData.cookingTime + (OCData.burnTime / 5) * 3){
            armorStand.customName(Component.text(OCResourcePackData.ProgressBarFont._overheat2.get_char()).font(OCResourcePackData.progressBarFontName));
        }
        else if(progression < OCData.cookingTime + (OCData.burnTime / 5) * 4){
            armorStand.customName(Component.text(OCResourcePackData.ProgressBarFont._overheat3.get_char()).font(OCResourcePackData.progressBarFontName));
        }
        else if(progression < OCData.cookingTime + OCData.burnTime){
            armorStand.customName(Component.text(OCResourcePackData.ProgressBarFont._overheat4.get_char()).font(OCResourcePackData.progressBarFontName));
        }
        
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
    }
}
