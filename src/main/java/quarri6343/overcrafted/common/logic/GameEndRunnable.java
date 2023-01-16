package quarri6343.overcrafted.common.logic;

import org.bukkit.scheduler.BukkitRunnable;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.GlobalTeamHandler;
import quarri6343.overcrafted.common.data.OCData;

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
        getData().teams.clearOrderBox();
        GlobalTeamHandler.resetTeams(true);
        additionalAction.run();
        if (isScheduled)
            cancel();
    }

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }
}