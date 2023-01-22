package quarri6343.overcrafted.impl.block;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.block.OCBlock;
import quarri6343.overcrafted.api.item.interfaces.IOCItem;
import quarri6343.overcrafted.api.item.interfaces.IProcessedOCItem;
import quarri6343.overcrafted.api.item.interfaces.IRightClickEventHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.impl.item.OCItems;

public class BlockProcessor extends OCBlock implements IRightClickEventHandler {

    public BlockProcessor(Material material) {
        super(material);
    }

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }
    
    public void onRightClick(PlayerInteractEvent event) {
        if(event.isCancelled())
            return;

        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;

        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null) {
            return;
        }

        IOCItem ocItem = OCItems.toOCItem(event.getItem());

        for(OCItems ocItems : OCItems.values()){
            if(!(ocItems.get() instanceof IProcessedOCItem)){
                continue;
            }

            if(((IProcessedOCItem)ocItems.get()).getType().getMaterial() != event.getClickedBlock().getType())
                continue;

            if(((IProcessedOCItem)ocItems.get()).getIngredient().get().equals(ocItem)){
                event.setCancelled(true);
                event.getPlayer().setItemInHand(ocItems.get().getItemStack());
            }
        }
    }
}
