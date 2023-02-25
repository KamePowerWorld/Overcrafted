package quarri6343.overcrafted.impl.task;

import lombok.Getter;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import quarri6343.overcrafted.api.block.IBlockProcessor;
import quarri6343.overcrafted.api.item.IBurntOCItem;
import quarri6343.overcrafted.api.item.IOCItem;
import quarri6343.overcrafted.api.item.IProcessedOCItem;
import quarri6343.overcrafted.core.handler.PlaceItemHandler;
import quarri6343.overcrafted.core.data.constant.OCCommonData;
import quarri6343.overcrafted.core.data.constant.OCParticleData;
import quarri6343.overcrafted.core.data.constant.OCResourcePackData;
import quarri6343.overcrafted.core.data.constant.OCSoundData;
import quarri6343.overcrafted.impl.block.OCBlocks;
import quarri6343.overcrafted.impl.item.OCItems;
import quarri6343.overcrafted.utils.ItemCreator;

import static quarri6343.overcrafted.impl.block.BlockAutomaticProcessor.armorStandOffset;

/**
 * ブロックによる自動加工のタスク
 */
public class AutomaticProcessingRunnable extends BukkitRunnable {
    
    @Getter
    private int progression = 0;
    private final Block block;
    private final IBlockProcessor processor;
    private final Sound processingSound;
    private final Particle processingParticle;

    public AutomaticProcessingRunnable(Block block, Sound processingSound, Particle processingParticle) {
        this.block = block;
        processor = (IBlockProcessor) OCBlocks.toOCBlock(block);
        this.processingSound = processingSound;
        this.processingParticle = processingParticle;

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

        if (progression < OCCommonData.cookingTime) {
            normalProcessing(armorStand);
        } else if (progression == OCCommonData.cookingTime) {
            normalProcessing(armorStand);
            finishProcessing();
        } else if (progression < OCCommonData.cookingTime + OCCommonData.burnTime) {
            burntProcessing(armorStand);
        } else {
            finishBurntProcessing(armorStand);
        }
    }

    private void normalProcessing(ArmorStand armorStand) {
        int filledPercent = progression / (OCCommonData.cookingTime / 10);
        for (OCResourcePackData.ProgressBarFont font : OCResourcePackData.ProgressBarFont.values()) {
            if (font.getFilledPercentage() == filledPercent) {
                armorStand.customName(Component.text(font.get_char()).font(OCResourcePackData.progressBarFontName));
                armorStand.getWorld().playSound(processingSound, armorStand.getLocation().getX(), armorStand.getLocation().getY(), armorStand.getLocation().getZ());
                break;
            }
        }

        if (progression % 5 == 0) {
            block.getWorld().spawnParticle(processingParticle, block.getLocation().add(0.5, 1.2, 0.5), 3);
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
        if(progression < OCCommonData.cookingTime + (OCCommonData.burnTime / 5)){
            armorStand.customName(Component.text(OCResourcePackData.ProgressBarFont._10.get_char()).font(OCResourcePackData.progressBarFontName));
        }
        else if(progression < OCCommonData.cookingTime + (OCCommonData.burnTime / 5) * 2){
            armorStand.customName(Component.text(OCResourcePackData.ProgressBarFont._overheat1.get_char()).font(OCResourcePackData.progressBarFontName));
        }
        else if(progression < OCCommonData.cookingTime + (OCCommonData.burnTime / 5) * 3){
            armorStand.customName(Component.text(OCResourcePackData.ProgressBarFont._overheat2.get_char()).font(OCResourcePackData.progressBarFontName));
        }
        else if(progression < OCCommonData.cookingTime + (OCCommonData.burnTime / 5) * 4){
            armorStand.customName(Component.text(OCResourcePackData.ProgressBarFont._overheat3.get_char()).font(OCResourcePackData.progressBarFontName));
        }
        else if(progression < OCCommonData.cookingTime + OCCommonData.burnTime){
            armorStand.customName(Component.text(OCResourcePackData.ProgressBarFont._overheat4.get_char()).font(OCResourcePackData.progressBarFontName));
        }
        
        if (progression % 5 == 0) {
            block.getWorld().spawnParticle(OCParticleData.overcraftParticle, block.getLocation().add(0.5, 1.2, 0.5), 3);
            armorStand.getWorld().playSound(OCSoundData.scorchingSound, armorStand.getLocation().getX(), armorStand.getLocation().getY(), armorStand.getLocation().getZ());
        }
        
        playAlertSound(armorStand);
    }
    
    private void playAlertSound(ArmorStand armorStand){
        int remainingTime = OCCommonData.cookingTime + OCCommonData.burnTime - progression;
        float remainingPercentage = (float) remainingTime / (float) OCCommonData.burnTime;
        if(remainingPercentage <= 0.8 && remainingPercentage > 0.4){
            if(remainingTime % 10 == 0)
                armorStand.getWorld().playSound(OCSoundData.overcraftAlertSound, armorStand.getLocation().getX(), armorStand.getLocation().getY(), armorStand.getLocation().getZ());
                armorStand.playSound(OCSoundData.overcraftAlertSound);
        }
        else if(remainingPercentage <= 0.4 && remainingPercentage > 0.2){
            if(remainingTime % 5 == 0)
                armorStand.getWorld().playSound(OCSoundData.overcraftAlertSound, armorStand.getLocation().getX(), armorStand.getLocation().getY(), armorStand.getLocation().getZ());
        }
        else if(remainingPercentage <= 0.2){
            if(remainingTime % 2 == 0)
                armorStand.getWorld().playSound(OCSoundData.overcraftAlertSound, armorStand.getLocation().getX(), armorStand.getLocation().getY(), armorStand.getLocation().getZ());
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
