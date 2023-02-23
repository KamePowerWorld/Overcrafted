package quarri6343.overcrafted.impl.task.game;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.api.block.IOCBlock;
import quarri6343.overcrafted.api.block.ISneakEventHandler;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.core.data.constant.OCCommonData;
import quarri6343.overcrafted.core.handler.order.BossBarHandler;
import quarri6343.overcrafted.core.handler.order.ScoreBoardHandler;
import quarri6343.overcrafted.impl.block.OCBlocks;

import java.util.function.Consumer;

import static quarri6343.overcrafted.core.data.constant.OCCommonData.blockEventTriggerRange;

/**
 * 定期的に起動してゲームの状態を監視するrunnable
 */
public class GameActiveTask extends BukkitRunnable {

    private int count = 0;
    private final Consumer<IOCTeam> onGameSuccess;

    public GameActiveTask(Consumer<IOCTeam> onGameSuccess) {
        this.onGameSuccess = onGameSuccess;
    }

    @Override
    public void run() {
        count++;

        if (count % 20 == 0) {
            BossBarHandler.updateRemainingTime( ((float) getData().getSelectedStage().get().getTime() * 20 - (float)count) / ((float) getData().getSelectedStage().get().getTime() * 20));
        }

        if (count >= getData().getSelectedStage().get().getTime() * 20) {
            onGameSuccess.accept(ScoreBoardHandler.getHighestScoreTeam());
            cancel();
        }

        if (count % OCCommonData.checkInventoryInterval == 0) {
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

    private OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }
}
