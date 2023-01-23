package quarri6343.overcrafted.common.logic;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.block.IOCBlock;
import quarri6343.overcrafted.api.block.ISneakEventHandler;
import quarri6343.overcrafted.api.block.OCBlock;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.data.interfaces.IOCTeam;
import quarri6343.overcrafted.common.order.ScoreBoardHandler;
import quarri6343.overcrafted.impl.block.OCBlocks;

import java.util.function.Consumer;

import static quarri6343.overcrafted.common.data.OCData.blockEventTriggerRange;

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

        triggerSneakEvent();
    }

    private void triggerSneakEvent() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(!onlinePlayer.isSneaking())
                continue;
            
            if (getData().getTeams().getTeamByPlayer(onlinePlayer) != null) {
                for (int i = -blockEventTriggerRange; i < blockEventTriggerRange + 1; i++) {
                    for (int j = -blockEventTriggerRange; j < blockEventTriggerRange + 1; j++) {
                        for (int k = -blockEventTriggerRange; k < blockEventTriggerRange + 1; k++) {
                            Block block = onlinePlayer.getLocation().getBlock().getRelative(i, j, k);
                            IOCBlock ocBlock = OCBlocks.toOCBlock(block);
                            if (ocBlock instanceof ISneakEventHandler)
                                ((ISneakEventHandler) ocBlock).whileSneaking(block);
                        }
                    }
                }
            }
        }
    }

    private OCData getData() {
        return Overcrafted.getInstance().getData();
    }
}
