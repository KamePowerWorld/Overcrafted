package quarri6343.overcrafted.common.event;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.DishHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;

/**
 * プレイヤーがゴミ箱に触れた時、手に持っているアイテムを捨てる
 */
public class TrashCanInteractEventHandler implements IPlayerInteractEventHandler {

    public TrashCanInteractEventHandler(){
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
        if (!event.getClickedBlock().getType().equals(Material.DROPPER))
            return;

        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;
        
        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null)
            return;

        event.setCancelled(true);
        
        if(event.getItem() == null)
            return;
        
        if(!event.getAction().isRightClick()){
            return;
        }

        if(event.getPlayer().getItemInHand().getType() == OCData.invalidItem.getType()){
            return;
        }

        if(DishHandler.isDish(event.getPlayer().getItemInHand())){
            try{
                team.orderBox.addItem(event.getPlayer().getItemInHand());
            }
            finally {
                event.getPlayer().setItemInHand(null);
                event.getPlayer().sendMessage(Component.text("ゴミ箱に持っているアイテムを捨てた！"));
            }
            return;
        }

        event.getPlayer().setItemInHand(null);
        event.getPlayer().sendMessage(Component.text("ゴミ箱に持っているアイテムを捨てた！"));
    }
}
