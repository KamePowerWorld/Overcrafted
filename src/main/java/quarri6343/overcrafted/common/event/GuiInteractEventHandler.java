package quarri6343.overcrafted.common.event;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.impl.ui.UISupplyMenu;

import java.util.Arrays;
import java.util.List;

public class GuiInteractEventHandler implements IPlayerInteractEventHandler {

    public List<Material> blockHasGui = Arrays.asList(Material.CHEST, Material.CRAFTING_TABLE, Material.FURNACE);
    
    public GuiInteractEventHandler() {
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

        if (event.getClickedBlock() == null)
            return;

        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;

        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null)
            return;

        if (!event.getAction().isRightClick()) {
            return;
        }

        if(blockHasGui.contains(event.getClickedBlock().getType())){
            event.setCancelled(true);
        }
    }
}
