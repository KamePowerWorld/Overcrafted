package quarri6343.overcrafted.common.logic;

import org.bukkit.scheduler.BukkitRunnable;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.data.interfaces.IOCTeam;
import quarri6343.overcrafted.common.order.ScoreBoardHandler;

import java.util.function.Consumer;

/**
 * 定期的に起動してゲームの状態を監視するrunnable
 */
public class GameRunnable extends BukkitRunnable {

    private int count = 0;
    private final Consumer<IOCTeam> onGameSuccess;

    public GameRunnable(Consumer<IOCTeam> onGameSuccess) {
        this.onGameSuccess = onGameSuccess;
    }

    @Override
    public void run() {
        count++;

        if (count % 20 == 0) {
            ScoreBoardHandler.refresh(OCData.gameLength - count / 20);
        }

        if (count >= OCData.gameLength * 20) {
            onGameSuccess.accept(ScoreBoardHandler.getHighestScoreTeam());
            cancel();
        }

        if (count % OCData.checkInventoryInterval == 0) {
            getData().getTeams().clearExcessiveItemsFromAllTeam();
        }
    }

    private OCData getData() {
        return Overcrafted.getInstance().getData();
    }
}
