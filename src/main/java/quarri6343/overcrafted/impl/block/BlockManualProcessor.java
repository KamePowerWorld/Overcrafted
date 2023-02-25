package quarri6343.overcrafted.impl.block;

import lombok.Getter;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import quarri6343.overcrafted.api.block.IBlockProcessor;
import quarri6343.overcrafted.api.block.ISneakEventHandler;
import quarri6343.overcrafted.api.item.IOCItem;
import quarri6343.overcrafted.api.item.IProcessedOCItem;
import quarri6343.overcrafted.api.item.IRightClickEventHandler;
import quarri6343.overcrafted.core.handler.PlaceItemHandler;
import quarri6343.overcrafted.core.data.constant.OCCommonData;
import quarri6343.overcrafted.core.data.constant.OCResourcePackData;
import quarri6343.overcrafted.impl.item.OCItems;
import quarri6343.overcrafted.utils.ItemCreator;

import java.util.HashMap;
import java.util.Map;

/**
 * アイテムを手動で別のアイテムに加工できるブロック
 */
public class BlockManualProcessor extends BlockTable implements IBlockProcessor, IRightClickEventHandler, ISneakEventHandler {

    private static final Vector armorStandOffset = new Vector(0.5,-0.5, 0.5);

    /**
     * 進捗を表すNBTタグのID
     */
    @Getter
    private final String progressionNBTID;

    /**
     * 加工音
     */
    @Getter
    private final Sound processingSound;

    /**
     * 加工のパーティクル
     */
    @Getter
    private final Particle processingParticle;
    
    /**
     * ブロックとそのブロックの加工の進捗のmap
     */
    private static final Map<Block, Integer> progressionMap = new HashMap<>();
    
    public BlockManualProcessor(Material material, String progressionNBTID, Sound processingSound, Particle processingParticle) {
        super(material);
        this.progressionNBTID = progressionNBTID;
        this.processingSound = processingSound;
        onPickUp.add((block, player) -> cancelProcessing(block, player, true));
        onPlace.add(block -> {
            if(canProcess(block))
                continueProcessing(block);
        });
        this.processingParticle = processingParticle;
    }
    
    @Override
    public boolean canProcess(Block block){
        ItemStack itemStack = PlaceItemHandler.getItem(block);
        if(itemStack == null)
            return false;

        IOCItem iocItem = OCItems.toOCItem(itemStack);
        if(iocItem == null)
            return false;


        for(OCItems ocItems : OCItems.values()){
            if(!(ocItems.get() instanceof IProcessedOCItem)){
                continue;
            }

            if(((IProcessedOCItem)ocItems.get()).getProcessType() != this)
                continue;

            if(((IProcessedOCItem)ocItems.get()).getIngredient().get().equals(iocItem)){
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public void continueProcessing(Block block){
        Location location = block.getLocation();
        location.add(armorStandOffset);
        ArmorStand armorStand = null;
        for (Entity nearbyEntity : location.getNearbyEntities(0.1, 0.1, 0.1)) {
            if (nearbyEntity.getType() == EntityType.ARMOR_STAND){
                armorStand = (ArmorStand) nearbyEntity;
            }
        }
        
        Integer progression = progressionMap.get(block);
        if(progression == null){
            progression = new ItemCreator(PlaceItemHandler.getItem(block)).getIntNBT(progressionNBTID);
            if(progression == null)
                progression = 0;
        }
        progression++;
        
        if(armorStand == null || armorStand.getCustomName() == null){
            if(armorStand != null)
                armorStand.remove();
            armorStand = location.getWorld().spawn(location, ArmorStand.class);
            armorStand.setVisible(false);
            armorStand.setCanMove(false);
            armorStand.setCanTick(false);
            armorStand.setCustomNameVisible(true);
        }
        
        int filledPercent = progression / (OCCommonData.craftingTime / 10);
        for (OCResourcePackData.ProgressBarFont font : OCResourcePackData.ProgressBarFont.values()) {
            if(font.getFilledPercentage() == filledPercent){
                armorStand.customName(Component.text(font.get_char()).font(OCResourcePackData.progressBarFontName));
                armorStand.getWorld().playSound(processingSound, armorStand.getLocation().getX(), armorStand.getLocation().getY(), armorStand.getLocation().getZ());
                break;
            }
        }
        
        if(progression % 5 == 0){
            block.getWorld().spawnParticle(processingParticle, block.getLocation().add(0.5, 1.2, 0.5), 3);
        }
        
        if(progression <= OCCommonData.craftingTime){
            progressionMap.put(block, progression);
        }
        else{
            finishProcessing(block, armorStand);
        }
    }
    
    private void finishProcessing(Block block, ArmorStand armorStand){
        progressionMap.remove(block);
        ItemStack itemStack = PlaceItemHandler.getItem(block);
        IOCItem iocItem = OCItems.toOCItem(itemStack);

        for(OCItems ocItems : OCItems.values()){
            if(!(ocItems.get() instanceof IProcessedOCItem)){
                continue;
            }

            if(((IProcessedOCItem)ocItems.get()).getProcessType() != this)
                continue;

            if(((IProcessedOCItem)ocItems.get()).getIngredient().get().equals(iocItem)){
                armorStand.remove();
                PlaceItemHandler.pickUpItem(block);
                PlaceItemHandler.placeItem(block, ocItems.get().getItemStack());
            }
        }
    }

    @Override
    public void whileSneaking(Block block) {
        if(canProcess(block))
            continueProcessing(block);
    }
    
    @Override
    public void cancelProcessing(Block block, Player player, boolean removeFromMap){
        Location location = block.getLocation();
        location.add(armorStandOffset);
        ArmorStand armorStand = null;
        for (Entity nearbyEntity : location.getNearbyEntities(0.1, 0.1, 0.1)) {
            if (nearbyEntity.getType() == EntityType.ARMOR_STAND){
                armorStand = (ArmorStand) nearbyEntity;
            }
        }
        if(armorStand != null)
            armorStand.remove();

        if(player != null && progressionMap.get(block) != null){
            player.setItemInHand(new ItemCreator(player.getItemInHand()).setIntNBT(progressionNBTID, progressionMap.get(block)).create());
        }
        
        if(removeFromMap)
            progressionMap.remove(block);
    }
    
    @Override
    public void cancelAllProcesses(){
        progressionMap.forEach((block, integer) -> cancelProcessing(block, null, false));
        progressionMap.clear();
    }
}
