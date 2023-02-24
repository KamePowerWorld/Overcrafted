package quarri6343.overcrafted.impl.task.game;

import org.bukkit.scheduler.BukkitRunnable;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.handler.GlobalTeamHandler;
import quarri6343.overcrafted.core.handler.PlaceItemHandler;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.core.handler.OrderHandler;
import quarri6343.overcrafted.api.block.IBlockProcessor;
import quarri6343.overcrafted.core.handler.order.ScoreBoardHandler;
import quarri6343.overcrafted.impl.OCStages;
import quarri6343.overcrafted.impl.block.OCBlocks;

/**
 * ゲーム終了後時間を空けて行いたい処理
 */
public class GameEndTask extends BukkitRunnable {
    private final Runnable additionalAction;
    private final boolean isScheduled;

    public GameEndTask(Runnable additionalAction, boolean isScheduled) {
        this.additionalAction = additionalAction;
        this.isScheduled = isScheduled;
    }

    @Override
    public void run() {
        getData().getTeams().teleportTeamToLobby();
        getData().getTeams().clearDishPile();
        if(ScoreBoardHandler.getHighestScore() > getData().getSelectedStage().get().getHighScore())
            getData().getSelectedStage().get().setHighScore(ScoreBoardHandler.getHighestScore());
        OrderHandler.clearOrders();
        GlobalTeamHandler.resetTeams(true);
        PlaceItemHandler.clear();
        for (OCBlocks blocks : OCBlocks.values()) {
            if(blocks.get() instanceof IBlockProcessor)
                ((IBlockProcessor)blocks.get()).cancelAllProcesses();
        }
        
        additionalAction.run();
        if (isScheduled)
            cancel();
    }

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }
}