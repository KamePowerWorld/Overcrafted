package quarri6343.overcrafted.common.event;

import net.kyori.adventure.text.Component;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerInteractEvent;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.common.order.OrderHandler;

/**
 * プレイヤーが皿置き場の下のブロックに素手で触れた時、皿を取る
 */
public class DishPileInteractEventHandler implements IPlayerInteractEventHandler {

    public DishPileInteractEventHandler(){
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
        if(event.isCancelled())
            return;

        if (event.getItem() != null)
            return;

        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;
        
        if(event.getClickedBlock() == null)
            return;

        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null) {
            return;
        }
        
        if(event.getClickedBlock().getRelative(BlockFace.UP).equals(team.cleanDishPile.location.getBlock())){
            if(team.cleanDishPile.removeDish())
                event.getPlayer().setItemInHand(OrderHandler.getDish());
        }
    }
}
