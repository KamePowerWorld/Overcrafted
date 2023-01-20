package quarri6343.overcrafted.common.logic;

import org.bukkit.scheduler.BukkitRunnable;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.GlobalTeamHandler;
import quarri6343.overcrafted.common.PlaceItemHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.order.OrderHandler;

/**
 * ゲーム終了後時間を空けて行いたい処理
 */
public class GameEndRunnable extends BukkitRunnable {
    private final Runnable additionalAction;
    private final boolean isScheduled;

    public GameEndRunnable(Runnable additionalAction, boolean isScheduled) {
        this.additionalAction = additionalAction;
        this.isScheduled = isScheduled;
    }

    @Override
    public void run() {
        getData().teams.teleportTeamToLobby();
        getData().teams.clearDishPile();
        OrderHandler.clearOrders();
        GlobalTeamHandler.resetTeams(true);
        PlaceItemHandler.clear();
        additionalAction.run();
        if (isScheduled)
            cancel();
    }

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }
}