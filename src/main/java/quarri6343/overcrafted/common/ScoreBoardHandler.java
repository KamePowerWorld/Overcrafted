package quarri6343.overcrafted.common;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

/**
 * スコアボードハンドラ
 * TODO:チーム単位でのスコア管理に変更する
 */
public class ScoreBoardHandler {

    private static final String objectiveName = "overcrafted";
    private static final Component mainObjectiveDisplayName = Component.text("スコア");

    private static Objective objective;

    /**
     * スコアボードを作成する
     */
    public static void initialize(){
        objective = getBoard().getObjective(objectiveName);
        if(objective == null)
            objective = getBoard().registerNewObjective(objectiveName, "dummy", ChatColor.RED + "スコア");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    /**
     * スコアを追加する
     * @param player
     */
    public static void addScore(Player player){
        if(objective == null)
            initialize();
        
        Score score = objective.getScore(player);
        score.setScore(score.getScore() + 1);
    }

    /**
     * スコアボードを削除する
     */
    public static void destroy(){
        if(objective == null)
            return;

        objective.unregister();
    }

    private static Scoreboard getBoard() {
        return Bukkit.getScoreboardManager().getMainScoreboard();
    }
}
