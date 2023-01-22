package quarri6343.overcrafted.impl.block;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.block.OCBlock;
import quarri6343.overcrafted.api.item.interfaces.IRightClickEventHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.impl.ui.UISupplyMenu;

public class BlockSupplier extends OCBlock implements IRightClickEventHandler {

    public BlockSupplier() {
        super(Material.OBSERVER);
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

        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null)
            return;

        event.setCancelled(true);

        if (!event.getAction().isRightClick()) {
            return;
        }

        UISupplyMenu.openUI(event.getPlayer());
    }
}
