package quarri6343.overcrafted.common.event;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.impl.ui.UISupplyMenu;

public class SupplierInteractEventHandler implements IPlayerInteractEventHandler {
    
    public SupplierInteractEventHandler(){
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

        if (event.getClickedBlock() == null || !event.getClickedBlock().getType().equals(Material.OBSERVER))
            return;

        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;

        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null)
            return;

        event.setCancelled(true);

        if(!event.getAction().isRightClick()){
            return;
        }

        UISupplyMenu.openUI(event.getPlayer());
    }
}
