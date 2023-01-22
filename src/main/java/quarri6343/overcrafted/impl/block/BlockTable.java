package quarri6343.overcrafted.impl.block;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.block.OCBlock;
import quarri6343.overcrafted.api.item.interfaces.IRightClickEventHandler;
import quarri6343.overcrafted.common.PlaceItemHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;

public class BlockTable extends OCBlock implements IRightClickEventHandler {

    public BlockTable() {
        super(Material.DARK_OAK_PLANKS);
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

        if(PlaceItemHandler.placeItem(event.getClickedBlock(), event.getItem())) {
            event.setCancelled(true);
            event.getPlayer().setItemInHand(null);
        }

        if (event.getItem() == null || event.getItem().getType() == Material.AIR) {
            ItemStack itemStack = PlaceItemHandler.pickUpItem(event.getClickedBlock());
            if (itemStack != null){
                event.setCancelled(true);
                event.getPlayer().setItemInHand(itemStack);
            }
        }
    }
}
