package quarri6343.overcrafted.impl.block;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.block.ISneakEventHandler;
import quarri6343.overcrafted.api.block.OCBlock;
import quarri6343.overcrafted.api.item.OCItem;
import quarri6343.overcrafted.api.item.interfaces.IOCItem;
import quarri6343.overcrafted.api.item.interfaces.IProcessedOCItem;
import quarri6343.overcrafted.api.item.interfaces.IRightClickEventHandler;
import quarri6343.overcrafted.common.PlaceItemHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.interfaces.IOCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.impl.item.OCItems;
import quarri6343.overcrafted.utils.ItemCreator;

/**
 * アイテムを別のアイテムに加工できるブロック
 */
public class BlockProcessor extends BlockTable implements IRightClickEventHandler, ISneakEventHandler {

    public BlockProcessor(Material material) {
        super(material);
    }

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }
    
    public void onRightClick(PlayerInteractEvent event) {
        if(event.isCancelled())
            return;

        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;

        IOCTeam team = getData().getTeams().getTeamByPlayer(event.getPlayer());
        if (team == null) {
            return;
        }
        
        super.onRightClick(event);
    }
    
    private boolean canProcess(Block block){
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
    
    public void process(Block block){
        Location location = block.getLocation();
        location.add(0.5, 1.1, 0.5);
        ItemFrame itemFrame = null;
        for (Entity nearbyEntity : location.getNearbyEntities(0.1, 0.1, 0.1)) {
            if (nearbyEntity.getType() == EntityType.ITEM_FRAME){
                itemFrame = (ItemFrame) nearbyEntity;
            }
        }
        
        if(itemFrame == null || itemFrame.getItem().getItemMeta() == null){
            if(itemFrame != null)
                itemFrame.remove();
            itemFrame = location.getWorld().spawn(block.getRelative(BlockFace.UP).getLocation(), ItemFrame.class);
            itemFrame.setFacingDirection(BlockFace.DOWN);
            itemFrame.setFixed(true);
            itemFrame.setVisible(false);
            itemFrame.setCustomNameVisible(true);
            itemFrame.setItem(new ItemCreator(Material.BIRCH_BUTTON).setName(Component.text("0")).create());
        }
        
        int progress = 0;
        try{
            progress = Integer.parseInt(itemFrame.getItem().getItemMeta().getDisplayName());
        }
        catch (NumberFormatException e){
            return;
        }
        
        if(progress <= OCData.craftingTime){
            progress++;
            itemFrame.setItem(new ItemCreator(Material.BIRCH_BUTTON).setName(Component.text(Integer.toString(progress))).create());
        }
        else{
            ItemStack itemStack = PlaceItemHandler.getItem(block);
            IOCItem iocItem = OCItems.toOCItem(itemStack);
            
            for(OCItems ocItems : OCItems.values()){
                if(!(ocItems.get() instanceof IProcessedOCItem)){
                    continue;
                }

                if(((IProcessedOCItem)ocItems.get()).getProcessType() != this)
                    continue;

                if(((IProcessedOCItem)ocItems.get()).getIngredient().get().equals(iocItem)){
                    itemFrame.remove();
                    PlaceItemHandler.pickUpItem(block);
                    PlaceItemHandler.placeItem(block, ocItems.get().getItemStack());
                }
            }
        }
        
    }

    @Override
    public void whileSneaking(Block block) {
        if(canProcess(block))
            process(block);
    }
}
