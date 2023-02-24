package quarri6343.overcrafted.core;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.core.data.constant.OCSoundData;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.core.handler.GlobalTeamHandler;
import quarri6343.overcrafted.impl.task.game.GameBeginTask;
import quarri6343.overcrafted.impl.task.game.GameEndTask;
import quarri6343.overcrafted.impl.task.game.GameInactiveTask;
import quarri6343.overcrafted.impl.task.game.GameActiveTask;
import quarri6343.overcrafted.core.handler.OrderHandler;

import java.util.List;

import static quarri6343.overcrafted.core.data.constant.OCCommonData.gameResultSceneLength;

/**
 * ゲームの進行を司るクラス
 */
public class OCLogic {

    public GameStatus gameStatus = GameStatus.INACTIVE;
    public World gameWorld = null;
    private BukkitTask gameInactiveRunnable;
    private BukkitTask gameBeginRunnable;
    @Getter
    private GameActiveTask gameRunnable;
    private BukkitTask gameEndRunnable;

    public OCLogic() {
        gameInactiveRunnable = new GameInactiveTask().runTaskTimer(Overcrafted.getInstance(), 0, 1);
    }

    /**
     * ゲームを開始する
     *
     * @param gameMaster ゲームを開始した人
     */
    public void startGame(@NotNull Player gameMaster) {
        if (gameStatus != GameStatus.INACTIVE) {
            gameMaster.sendMessage(Component.text("ゲームが進行中です！"));
            return;
        }

        if(getData().getSelectedStage() == null){
            gameMaster.sendMessage(Component.text("プレイするステージが選択されていません！"));
            return;
        }

        if (!GlobalTeamHandler.areTeamsValid(gameMaster))
            return;

        if (gameInactiveRunnable != null)
            gameInactiveRunnable.cancel();
        gameWorld = gameMaster.getWorld();
        gameStatus = GameStatus.BEGINNING;
        gameBeginRunnable = new GameBeginTask(this::onGameBegin).runTaskTimer(Overcrafted.getInstance(), 0, 1);
    }

    /**
     * ゲームが実際に始まった時に行う処理
     */
    private void onGameBegin() {
        for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
            IOCTeam team = getData().getTeams().getTeam(i);
            if (team.getPlayersSize() == 0)
                continue;

            for (int j = 0; j < team.getPlayersSize(); j++) {
                team.setUpGameEnvforPlayer(team.getPlayer(j), getData().getSelectedStage().ordinal());
            }

            team.getCleanDishPiles().get(getData().getSelectedStage().ordinal()).setUp();
        }
        Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(Title.title(Component.text(Overcrafted.getInstance().getData().getSelectedStage().get().getName()).color(NamedTextColor.YELLOW), Component.empty())));
        OrderHandler.generateRandomOrders();

        gameStatus = GameStatus.ACTIVE;
        gameRunnable = new GameActiveTask(urTeam -> endGame(null, urTeam, true, true));
        gameRunnable.runTaskTimer(Overcrafted.getInstance(), 0, 1);
    }

    public void endGame() {
        endGame(null, null, false, false);
    }

    /**
     * ゲームを終了する
     *
     * @param sender      ゲームを終了した人
     * @param victoryTeam 勝ったチーム
     * @param isSuccessed  ゲームが成功したかどうか
     */
    public void endGame(@Nullable Player sender, @Nullable IOCTeam victoryTeam, boolean isSuccessed, boolean hasResultScene) {
        if (gameStatus == GameStatus.INACTIVE) {
            if (sender != null)
                sender.sendMessage("ゲームが始まっていません！");
            return;
        }

        if (gameBeginRunnable != null)
            gameBeginRunnable.cancel();
        if (gameRunnable != null)
            gameRunnable.cancel();
        if (gameEndRunnable != null)
            gameEndRunnable.cancel();

        if (isSuccessed) {
            displayGameSuccessTitle(victoryTeam);
        } else {
            displayGameFailureTitle();
        }

        gameStatus = GameStatus.ENDING;
        if (hasResultScene)
            gameEndRunnable = new GameEndTask(() -> {
                gameStatus = GameStatus.INACTIVE;
                gameInactiveRunnable = new GameInactiveTask().runTaskTimer(Overcrafted.getInstance(), 0, 1);
            }, true)
                    .runTaskTimer(Overcrafted.getInstance(), gameResultSceneLength, 1);
        else
            new GameEndTask(() -> gameStatus = GameStatus.INACTIVE, false).run();
    }

    /**
     * ゲームが成功したというタイトルを表示する
     *
     * @param victoryTeam 勝利したチーム。もしnullだった場合引き分けになる
     */
    private void displayGameSuccessTitle(IOCTeam victoryTeam) {
        if (victoryTeam == null) {
            Bukkit.getOnlinePlayers().forEach(player ->
                    player.showTitle(Title.title(Component.text("引き分け"), Component.text(""))));
            return;
        }

        List<TextComponent> playerList = victoryTeam.playerNamesToText();
        Component subTitle = Component.text("");
        for (int i = 0; i < playerList.size(); i++) {
            if (i != 0)
                subTitle = subTitle.append(Component.text(", ").color(NamedTextColor.YELLOW));
            subTitle = subTitle.append(playerList.get(i));
        }
        Component finalSubTitle = subTitle;
        for (int i = 0; i < victoryTeam.getPlayersSize(); i++) {
            victoryTeam.getPlayer(i).playSound(OCSoundData.victorySound);;
        }
        Bukkit.getOnlinePlayers().forEach(player ->
                player.showTitle(Title.title(Component.text("チーム" + victoryTeam.getName() + "の勝利！"), finalSubTitle)));
    }

    /**
     * ゲームが失敗したというタイトルを表示する
     */
    private void displayGameFailureTitle() {
        Bukkit.getOnlinePlayers().forEach(player -> {
                    player.playSound(OCSoundData.gameOverSound);
                    player.showTitle(Title.title(Component.text("ゲームオーバー"), Component.empty()));
        });
    }

    /**
     * ゲームの状態(進行中/始まっていない)
     */
    public enum GameStatus {
        BEGINNING,
        ACTIVE,
        ENDING,
        INACTIVE
    }

    private OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }
}
