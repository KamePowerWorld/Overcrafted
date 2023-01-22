package quarri6343.overcrafted.impl.block;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.block.OCBlock;
import quarri6343.overcrafted.api.item.interfaces.IOCItem;
import quarri6343.overcrafted.api.item.interfaces.IRightClickEventHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.impl.item.OCItems;

public class BlockTrashCan extends OCBlock implements IRightClickEventHandler {
    public BlockTrashCan() {
        super(Material.DROPPER);
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

        if (event.getItem() == null)
            return;

        if (!event.getAction().isRightClick()) {
            return;
        }

        if (event.getPlayer().getItemInHand().getType() == OCData.invalidItem.getType()) {
            return;
        }

        IOCItem ocItem = OCItems.toOCItem(event.getPlayer().getItemInHand());
        if(ocItem == null)
            return;

        if (ocItem.equals(OCItems.DIRTY_DISH.get())) {
            if (team.dirtyDishPile.addDish()) {
                event.getPlayer().setItemInHand(null);
                event.getPlayer().sendMessage(Component.text("ゴミ箱に持っている皿を捨てた！"));
            } else {
                event.getPlayer().sendMessage(Component.text("皿置場が一杯で皿を捨てられなかった！"));
            }
            return;
        }

        if (ocItem.equals(OCItems.DISH.get())) {
            if (team.cleanDishPile.addDish()) {
                event.getPlayer().setItemInHand(null);
                event.getPlayer().sendMessage(Component.text("ゴミ箱に持っている皿を捨てた！"));
            } else {
                event.getPlayer().sendMessage(Component.text("皿置場が一杯で皿を捨てられなかった！"));
            }
            return;
        }

        event.getPlayer().setItemInHand(null);
        event.getPlayer().sendMessage(Component.text("ゴミ箱に持っているアイテムを捨てた！"));
    }
}
