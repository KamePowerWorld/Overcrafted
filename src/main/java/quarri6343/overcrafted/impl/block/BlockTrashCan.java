package quarri6343.overcrafted.impl.block;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.item.IOCItem;
import quarri6343.overcrafted.api.item.IRightClickEventHandler;
import quarri6343.overcrafted.core.OCBlock;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.core.OCLogic;
import quarri6343.overcrafted.api.item.ISubmittableOCItem;
import quarri6343.overcrafted.core.data.constant.OCCommonData;
import quarri6343.overcrafted.impl.item.OCItems;

/**
 * ゴミ箱ブロック
 */
public class BlockTrashCan extends OCBlock implements IRightClickEventHandler {
    public BlockTrashCan() {
        super(Material.DROPPER);
    }
    
    private static OCVariableData getData() {
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

        IOCTeam team = getData().getTeams().getTeamByPlayer(event.getPlayer());
        if (team == null)
            return;

        event.setCancelled(true);

        if (event.getItem() == null)
            return;

        if (!event.getAction().isRightClick()) {
            return;
        }

        IOCItem ocItem = OCItems.toOCItem(event.getPlayer().getItemInHand());
        if(ocItem == null)
            return;

        if (ocItem.equals(OCItems.DIRTY_DISH.get())) {
            event.getPlayer().setItemInHand(null);
            event.getPlayer().sendActionBar(Component.text("ゴミ箱に持っている汚い皿を捨ててしまった！"));
            new BukkitRunnable() {
                @Override
                public void run() {
                    team.getDirtyDishPile().addDish();
                }
            }.runTaskLater(Overcrafted.getInstance(), OCCommonData.dishReturnLag);
            return;
        }

        if (ocItem.equals(OCItems.DISH.get()) || ocItem instanceof ISubmittableOCItem) {
            event.getPlayer().setItemInHand(null);
            event.getPlayer().sendActionBar(Component.text("ゴミ箱に持っている皿を捨てた！"));
            new BukkitRunnable() {
                @Override
                public void run() {
                    team.getCleanDishPile().addDish();
                }
            }.runTaskLater(Overcrafted.getInstance(), OCCommonData.dishReturnLag);
            return;
        }

        event.getPlayer().setItemInHand(null);
        event.getPlayer().sendActionBar(Component.text("ゴミ箱に持っているアイテムを捨てた！"));
    }
}
