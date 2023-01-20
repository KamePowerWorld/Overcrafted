package quarri6343.overcrafted.common.event;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.PlaceItemHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;

public class PlaceItemEventHandler implements IPlayerInteractEventHandler {
    
    private static final Material blockCanPlaceItem = Material.DARK_OAK_PLANKS;
    
    public PlaceItemEventHandler(){
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

        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;

        if(event.getClickedBlock() == null || event.getClickedBlock().getType() != blockCanPlaceItem)
            return;

        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null) {
            return;
        }

        event.setCancelled(true);
        
        if(event.getItem() != null){
            if(PlaceItemHandler.placeItem(event.getClickedBlock(), event.getItem().getType())){
                event.getPlayer().setItemInHand(null);
            }
        }
        else{
            Material material = PlaceItemHandler.pickUpItem(event.getClickedBlock());
            if(material != null)
                event.getPlayer().setItemInHand(new ItemStack(material));
        }
    }
}
