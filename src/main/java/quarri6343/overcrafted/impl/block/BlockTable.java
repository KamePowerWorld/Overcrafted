package quarri6343.overcrafted.impl.block;

import it.unimi.dsi.fastutil.Pair;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.item.ICombinedOCItem;
import quarri6343.overcrafted.api.item.IOCItem;
import quarri6343.overcrafted.api.item.IRightClickEventHandler;
import quarri6343.overcrafted.core.OCBlock;
import quarri6343.overcrafted.core.handler.PlaceItemHandler;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.core.data.constant.OCSoundData;
import quarri6343.overcrafted.api.IOCTeam;
import quarri6343.overcrafted.core.OCLogic;
import quarri6343.overcrafted.impl.item.OCItems;
import quarri6343.overcrafted.utils.OverCraftedUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * アイテムを上に置くことのできるブロック
 */
public class BlockTable extends OCBlock implements IRightClickEventHandler {

    /**
     * アイテムが拾われた時に発火されるイベント
     */
    protected List<BiConsumer<Block, Player>> onPickUp = new ArrayList<>();

    protected List<Consumer<Block>> onPlace = new ArrayList<>();

    public BlockTable(Material material) {
        super(material);
    }

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }
    
    @Override
    public void onRightClick(PlayerInteractEvent event) {
        if (event.isCancelled())
            return;

        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;

        IOCTeam team = getData().getTeams().getTeamByPlayer(event.getPlayer());
        if (team == null)
            return;

        event.setCancelled(true);
        
        if(PlaceItemHandler.placeItem(event.getClickedBlock(), event.getItem())) {
            event.getPlayer().setItemInHand(null);
            onPlace.forEach(blockConsumer -> blockConsumer.accept(event.getClickedBlock()));
            return;
        }

        if ((event.getItem() == null || event.getItem().getType() == Material.AIR) && OverCraftedUtil.getInventoryItemCount(event.getPlayer().getInventory()) == 0) {
            ItemStack itemStack = PlaceItemHandler.pickUpItem(event.getClickedBlock());
            if (itemStack != null){
                event.getPlayer().setItemInHand(itemStack);
                onPickUp.forEach(blockConsumer -> blockConsumer.accept(event.getClickedBlock(), event.getPlayer()));
                event.getPlayer().playSound(OCSoundData.tablePickUpSound);
            }
            return;
        }

        if(PlaceItemHandler.getItem(event.getClickedBlock()) != null){
            IOCItem ocItem1 = OCItems.toOCItem(event.getItem());
            IOCItem ocItem2 = OCItems.toOCItem(PlaceItemHandler.getItem(event.getClickedBlock()));

            for(OCItems ocItem : OCItems.values()){
                if(!(ocItem.get() instanceof ICombinedOCItem)){
                    continue;
                }

                Pair<OCItems, OCItems> ingredients = ((ICombinedOCItem)ocItem.get()).getIngredients();
                if((ingredients.left().get().equals(ocItem1) && ingredients.right().get().equals(ocItem2))
                        || (ingredients.left().get().equals(ocItem2) && ingredients.right().get().equals(ocItem1))){
                    PlaceItemHandler.pickUpItem(event.getClickedBlock());
                    event.getPlayer().setItemInHand(ocItem.get().getItemStack());
                    onPickUp.forEach(blockConsumer -> blockConsumer.accept(event.getClickedBlock(),event.getPlayer()));
                    event.getPlayer().playSound(OCSoundData.tablePickUpSound);
                    return;
                }
            }
        }
    }
}
