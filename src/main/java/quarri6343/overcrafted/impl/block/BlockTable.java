package quarri6343.overcrafted.impl.block;

import it.unimi.dsi.fastutil.Pair;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.title.Title;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.block.OCBlock;
import quarri6343.overcrafted.api.item.interfaces.ICombinedOCItem;
import quarri6343.overcrafted.api.item.interfaces.IOCItem;
import quarri6343.overcrafted.api.item.interfaces.IRightClickEventHandler;
import quarri6343.overcrafted.common.PlaceItemHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.interfaces.IOCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.impl.item.OCItems;

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

    private static OCData getData() {
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

        if (event.getItem() != null && event.getItem().getType() == OCData.invalidItem.getType())
            return;

        event.setCancelled(true);
        
        if(PlaceItemHandler.placeItem(event.getClickedBlock(), event.getItem())) {
            event.getPlayer().setItemInHand(null);
            onPlace.forEach(blockConsumer -> blockConsumer.accept(event.getClickedBlock()));
        }

        if (event.getItem() == null || event.getItem().getType() == Material.AIR) {
            ItemStack itemStack = PlaceItemHandler.pickUpItem(event.getClickedBlock());
            if (itemStack != null){
                event.getPlayer().setItemInHand(itemStack);
                onPickUp.forEach(blockConsumer -> blockConsumer.accept(event.getClickedBlock(), event.getPlayer()));
            }
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
                    return;
                }
            }
        }
    }
}
