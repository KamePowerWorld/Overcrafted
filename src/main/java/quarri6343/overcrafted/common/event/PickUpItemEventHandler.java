package quarri6343.overcrafted.common.event;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.PlaceItemHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;

public class PickUpItemEventHandler implements IPlayerInteractEventHandler {
    
    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }

    public PickUpItemEventHandler() {
        Overcrafted.getInstance().getPlayerEventHandler().registerHandler(this);
    }
    
    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.isCancelled())
            return;

        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;

        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null) {
            return;
        }

        if (event.getItem() == null || event.getItem().getType() == Material.AIR) {
            ItemStack itemStack = PlaceItemHandler.pickUpItem(event.getClickedBlock());
            if (itemStack != null)
                event.getPlayer().setItemInHand(itemStack);
        }

    }
}
