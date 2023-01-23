package quarri6343.overcrafted.impl.block;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import quarri6343.overcrafted.api.block.ISneakEventHandler;
import quarri6343.overcrafted.api.item.interfaces.IOCItem;
import quarri6343.overcrafted.api.item.interfaces.IProcessedOCItem;
import quarri6343.overcrafted.api.item.interfaces.IRightClickEventHandler;
import quarri6343.overcrafted.common.PlaceItemHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.impl.item.OCItems;

import java.util.HashMap;
import java.util.Map;

/**
 * アイテムを手動で別のアイテムに加工できるブロック
 */
public class ManualBlockProcessor extends BlockTable implements IBlockProcessor, IRightClickEventHandler, ISneakEventHandler {

    private static final Vector armorStandOffset = new Vector(0.5,-0.5, 0.5);
    
    /**
     * ブロックとそのブロックの加工の進捗のmap
     */
    private static final Map<Block, Integer> progressionMap = new HashMap<>();
    
    public ManualBlockProcessor(Material material) {
        super(material);
        onPickUp.add(this::cancelProcessing);
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
        
        Integer progress = progressionMap.get(block);
        if(progress == null)
            progress = 0;
        progress++;
        
        if(armorStand == null || armorStand.getCustomName() == null){
            if(armorStand != null)
                armorStand.remove();
            armorStand = location.getWorld().spawn(location, ArmorStand.class);
            armorStand.setVisible(false);
            armorStand.setCanMove(false);
            armorStand.setCanTick(false);
            armorStand.setCustomNameVisible(true);
        }
        
        armorStand.setCustomName(Integer.toString(progress));
        
        if(progress <= OCData.craftingTime){
            progressionMap.put(block, progress);
        }
        else{
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
    }

    @Override
    public void whileSneaking(Block block) {
        if(canProcess(block))
            continueProcessing(block);
    }
    
    @Override
    public void cancelProcessing(Block block){
        Location location = block.getLocation();
        location.add(armorStandOffset);
        ItemFrame itemFrame = null;
        for (Entity nearbyEntity : location.getNearbyEntities(0.1, 0.1, 0.1)) {
            if (nearbyEntity.getType() == EntityType.ITEM_FRAME){
                itemFrame = (ItemFrame) nearbyEntity;
            }
        }
        if(itemFrame != null)
            itemFrame.remove();
        
        progressionMap.remove(block);
    }
    
    @Override
    public void cancelAllProcess(){
        
    }
}
