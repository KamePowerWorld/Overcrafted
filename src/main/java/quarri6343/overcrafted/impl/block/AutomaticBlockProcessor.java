package quarri6343.overcrafted.impl.block;

import org.bukkit.Location;
import org.bukkit.Material;
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
import quarri6343.overcrafted.api.item.interfaces.IOCItem;
import quarri6343.overcrafted.api.item.interfaces.IProcessedOCItem;
import quarri6343.overcrafted.common.PlaceItemHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.impl.item.OCItems;

import java.util.HashMap;
import java.util.Map;

/**
 * アイテムを時間をかけて別のアイテムに加工できるブロック
 */
public class AutomaticBlockProcessor extends BlockTable implements IBlockProcessor{
    
    private static final Vector armorStandOffset = new Vector(0.5,-0.5, 0.5);

    /**
     * ブロックとそのブロックの加工中のタスクのマップ
     */
    private static final Map<Block, BukkitTask> progressionMap = new HashMap<>();

    public AutomaticBlockProcessor(Material material) {
        super(material);
        onPickUp.add(this::cancelProcessing);
    }

    public void onRightClick(PlayerInteractEvent event) {
        super.onRightClick(event);

        if(canProcess(event.getClickedBlock()))
            this.continueProcessing(event.getClickedBlock());
    }

    public boolean canProcess(Block block){
        if(progressionMap.containsKey(block))
            return false;
        
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
        IBlockProcessor processor = this;
        BukkitTask task =  new BukkitRunnable(){
            int progression = 0;
            
            @Override
            public void run() {
                Location location = block.getLocation();
                location.add(armorStandOffset);
                ArmorStand armorStand = null;
                for (Entity nearbyEntity : location.getNearbyEntities(0.1, 0.1, 0.1)) {
                    if (nearbyEntity.getType() == EntityType.ARMOR_STAND){
                        armorStand = (ArmorStand) nearbyEntity;
                    }
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

                armorStand.setCustomName(Integer.toString(progression));

                if(progression > OCData.cookingTime){
                    cancel();
                    progressionMap.remove(block);
                    ItemStack itemStack = PlaceItemHandler.getItem(block);
                    IOCItem iocItem = OCItems.toOCItem(itemStack);

                    for(OCItems ocItems : OCItems.values()){
                        if(!(ocItems.get() instanceof IProcessedOCItem)){
                            continue;
                        }

                        if(((IProcessedOCItem)ocItems.get()).getProcessType() != processor)
                            continue;

                        if(((IProcessedOCItem)ocItems.get()).getIngredient().get().equals(iocItem)){
                            armorStand.remove();
                            PlaceItemHandler.pickUpItem(block);
                            PlaceItemHandler.placeItem(block, ocItems.get().getItemStack());
                        }
                    }
                }
            }
        }.runTaskTimer(Overcrafted.getInstance(), 0, 1);
        progressionMap.put(block, task);
    }

    @Override
    public void cancelProcessing(Block block){
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

        if(progressionMap.get(block) != null){
            progressionMap.get(block).cancel();
            progressionMap.remove(block);
        }
    }
    
    @Override
    public void cancelAllProcess(){

    }
}
