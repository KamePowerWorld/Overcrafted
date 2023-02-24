package quarri6343.overcrafted.impl.event.interact;

import it.unimi.dsi.fastutil.Pair;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerInteractEvent;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.IPlayerInteractEventHandler;
import quarri6343.overcrafted.api.item.ICombinedOCItem;
import quarri6343.overcrafted.api.item.IOCItem;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.core.OCLogic;
import quarri6343.overcrafted.impl.item.OCItems;
import quarri6343.overcrafted.utils.OverCraftedUtil;

/**
 * プレイヤーが皿置き場の下のブロックに素手で触れた時、皿を取る
 */
public class DishPileInteractEventHandler implements IPlayerInteractEventHandler {

    public DishPileInteractEventHandler() {
        Overcrafted.getInstance().getPlayerEventHandler().registerHandler(this);
    }

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }
    
    private static int getStageID(){
        return getData().getSelectedStage().ordinal();
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
            if (event.getClickedBlock().getRelative(BlockFace.UP).equals(team.getCleanDishPiles().get(getStageID()).getLocation().getBlock())) {
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
                    if (event.getClickedBlock().getRelative(BlockFace.UP).equals(team.getCleanDishPiles().get(getStageID()).getLocation().getBlock())) {
                        if (team.getCleanDishPiles().get(getStageID()).removeDish())
                            event.getPlayer().setItemInHand(ocItem.get().getItemStack());
                    }
                    else{
                        if (team.getDirtyDishPiles().get(getStageID()).removeDish())
                            event.getPlayer().setItemInHand(ocItem.get().getItemStack());
                    }
                    return;
                }
            }
            return;
        }

        if(OverCraftedUtil.getInventoryItemCount(event.getPlayer().getInventory()) > 0)
            return;

        if (event.getClickedBlock().getRelative(BlockFace.UP).equals(team.getCleanDishPiles().get(getStageID()).getLocation().getBlock())) {
            if (team.getCleanDishPiles().get(getStageID()).removeDish())
                event.getPlayer().setItemInHand(OCItems.DISH.get().getItemStack());
        } else if (event.getClickedBlock().getRelative(BlockFace.UP).equals(team.getDirtyDishPiles().get(getStageID()).getLocation().getBlock())) {
            if (team.getDirtyDishPiles().get(getStageID()).removeDish())
                event.getPlayer().setItemInHand(OCItems.DIRTY_DISH.get().getItemStack());
        }
    }
}
