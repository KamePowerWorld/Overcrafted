package quarri6343.overcrafted.common.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.interfaces.IOCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.utils.OverCraftedUtils;

/**
 * プレイヤーがアイテムを1個より多く持たないように拾う量を調整する
 */
public class BlockPickUpEventHandler implements IPlayerInteractEventHandler{

    public BlockPickUpEventHandler() {
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
        Entity entity = OverCraftedUtils.getNearestEntityInSight(event.getPlayer(), 1);

        if(!(entity instanceof Item) || ((Item)entity).getPickupDelay() > 0 || !((Item)entity).canPlayerPickup())
            return;

        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return;

        IOCTeam team = getData().getTeams().getTeamByPlayer(event.getPlayer());
        if (team == null)
            return;

        if(event.getPlayer().getInventory().getItem(0) != null)
            return;

        if (((Item)entity).getItemStack().getAmount() == 1){
            entity.remove();
            ItemStack itemOnGround = ((Item)entity).getItemStack();
            event.getPlayer().getInventory().addItem(itemOnGround);
        }
        else if(((Item)entity).getItemStack().getAmount() > 1){
            ItemStack itemOnGround = ((Item)entity).getItemStack();
            itemOnGround.setAmount(itemOnGround.getAmount() - 1);
            ((Item)entity).setItemStack(itemOnGround);
            event.setCancelled(true);
            itemOnGround.setAmount(1);
            event.getPlayer().getInventory().addItem(itemOnGround);
        }
    }
}
