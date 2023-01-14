package quarri6343.overcrafted.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;

/**
 * スコアボードハンドラ
 * TODO:チーム単位でのスコア管理に変更する
 */
public class ScoreBoardHandler {

    private static final String objectiveName = "overcrafted";
    private static final Component mainObjectiveDisplayName = Component.text("Overcrafted").color(NamedTextColor.RED);
    private static final String remainingTime = "残り時間";

    private static Objective objective;

    /**
     * スコアボードを作成する
     */
    public static void initialize() {
        objective = getBoard().getObjective(objectiveName);
        if (objective == null)
            objective = getBoard().registerNewObjective(objectiveName, "dummy", mainObjectiveDisplayName);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        setTime(OCData.gameLength);

        for (int i = 0; i < getData().teams.getTeamsLength(); i++) {
            OCTeam team = getData().teams.getTeam(i);

            Score score = objective.getScore(team.name);
            score.setScore(0);
        }
    }

    /**
     * スコアを追加する
     *
     * @param team
     */
    public static void addScore(OCTeam team) {
        if (objective == null)
            initialize();

        Score score = objective.getScore(team.name);
        score.setScore(score.getScore() + 1);
    }

    public static void setTime(int time) {
        if (objective == null)
            initialize();

        objective.getScore(remainingTime).setScore(time);
    }

    /**
     * スコアボードを削除する
     */
    public static void destroy() {
        if (objective == null)
            return;

        objective.unregister();
    }

    private static Scoreboard getBoard() {
        return Bukkit.getScoreboardManager().getMainScoreboard();
    }

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }
}
