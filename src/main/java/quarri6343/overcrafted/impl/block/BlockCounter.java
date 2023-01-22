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
import quarri6343.overcrafted.common.order.OrderHandler;
import quarri6343.overcrafted.impl.item.ISubmittable;
import quarri6343.overcrafted.impl.item.OCItems;

public class BlockCounter extends OCBlock implements IRightClickEventHandler {

    public BlockCounter(Material material) {
        super(material);
    }

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }

    @Override
    public void onRightClick(PlayerInteractEvent event) {
        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null) {
            event.getPlayer().sendMessage(Component.text("あなたはチームに所属していないため、皿を扱うことができません"));
            return;
        }

        trySubmitOrder(event, team);
    }

    /**
     * プレイヤーが手に持っている皿をカウンターに提出することを試みる
     */
    private void trySubmitOrder(PlayerInteractEvent event, OCTeam team) {
        IOCItem item = OCItems.toOCItem(event.getItem());
        if (!(item instanceof ISubmittable))
            return;

        if (OrderHandler.trySatisfyOrder(team, (ISubmittable) item)) {
            event.getPlayer().setItemInHand(null);
            team.dirtyDishPile.addDish();
        } else {
            event.getPlayer().sendMessage(Component.text("皿に載っているアイテムは誰も注文していないようだ..."));
        }
    }
}
