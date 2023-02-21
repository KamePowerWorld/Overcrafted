package quarri6343.overcrafted.common.event;

import it.unimi.dsi.fastutil.Pair;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerInteractEvent;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.item.interfaces.ICombinedOCItem;
import quarri6343.overcrafted.api.item.interfaces.IOCItem;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.interfaces.IOCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.impl.item.OCItems;
import quarri6343.overcrafted.utils.OverCraftedUtils;

/**
 * プレイヤーが皿置き場の下のブロックに素手で触れた時、皿を取る
 */
public class DishPileInteractEventHandler implements IPlayerInteractEventHandler {

    public DishPileInteractEventHandler() {
        Overcrafted.getInstance().getPlayerEventHandler().registerHandler(this);
    }

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled())
            return;

        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;

        if (event.getClickedBlock() == null)
            return;

        IOCTeam team = getData().getTeams().getTeamByPlayer(event.getPlayer());
        if (team == null) {
            return;
        }

        if (event.getItem() != null && event.getItem().getType() != Material.AIR){
            IOCItem ocItem1 = OCItems.toOCItem(event.getPlayer().getItemInHand());
            IOCItem ocItem2;
            if (event.getClickedBlock().getRelative(BlockFace.UP).equals(team.getCleanDishPile().getLocation().getBlock())) {
                ocItem2 = OCItems.DISH.get();
            }
            else{
                ocItem2 = OCItems.DIRTY_DISH.get();
            }

            for(OCItems ocItem : OCItems.values()){
                if(!(ocItem.get() instanceof ICombinedOCItem)){
                    continue;
                }

                Pair<OCItems, OCItems> ingredients = ((ICombinedOCItem)ocItem.get()).getIngredients();
                if((ingredients.left().get().equals(ocItem1) && ingredients.right().get().equals(ocItem2))
                        || (ingredients.left().get().equals(ocItem2) && ingredients.right().get().equals(ocItem1))){
                    if (event.getClickedBlock().getRelative(BlockFace.UP).equals(team.getCleanDishPile().getLocation().getBlock())) {
                        if (team.getCleanDishPile().removeDish())
                            event.getPlayer().setItemInHand(ocItem.get().getItemStack());
                    }
                    else{
                        if (team.getDirtyDishPile().removeDish())
                            event.getPlayer().setItemInHand(ocItem.get().getItemStack());
                    }
                    return;
                }
            }
            return;
        }

        if(OverCraftedUtils.getInventoryItemCount(event.getPlayer().getInventory()) > 0)
            return;

        if (event.getClickedBlock().getRelative(BlockFace.UP).equals(team.getCleanDishPile().getLocation().getBlock())) {
            if (team.getCleanDishPile().removeDish())
                event.getPlayer().setItemInHand(OCItems.DISH.get().getItemStack());
        } else if (event.getClickedBlock().getRelative(BlockFace.UP).equals(team.getDirtyDishPile().getLocation().getBlock())) {
            if (team.getDirtyDishPile().removeDish())
                event.getPlayer().setItemInHand(OCItems.DIRTY_DISH.get().getItemStack());
        }
    }
}
