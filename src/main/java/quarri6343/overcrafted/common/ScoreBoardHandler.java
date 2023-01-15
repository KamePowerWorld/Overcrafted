package quarri6343.overcrafted.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * スコアボードハンドラ
 */
public class ScoreBoardHandler {

    private static final String objectiveName = "overcrafted";
    private static final Component mainObjectiveDisplayName = Component.text("Overcrafted").color(NamedTextColor.RED);
    private static final String remainingTime = "残り時間: ";

    private static Objective objective;

    private final static Map<OCTeam, Integer> scores = new HashMap<>();
    private final static List<String> currentScoreString = new ArrayList<>();

    /**
     * スコアボードを作成する
     */
    public static void initialize() {
        for (int i = 0; i < getData().teams.getTeamsLength(); i++) {
            OCTeam team = getData().teams.getTeam(i);
            scores.put(team, 0);
        }

        refresh(OCData.gameLength);
    }

    /**
     * スコアを追加する
     *
     * @param team
     */
    public static void addScore(OCTeam team, int value) {
        if (scores.get(team) == null)
            return;

        scores.put(team, scores.get(team) + value);
    }

    /**
     * 最もスコアの高いチームを取得する
     *
     * @return 最もスコアの高いチーム、もしチームが存在しないまたは複数存在した場合null
     */
    public static OCTeam getHighestScoreTeam() {
        int highestScore = 0;
        OCTeam highestTeam = null;
        boolean draw = false;

        for (int i = 0; i < getData().teams.getTeamsLength(); i++) {
            OCTeam team = getData().teams.getTeam(i);
            int score = scores.get(team);

            if (score > highestScore) {
                highestTeam = team;
                highestScore = score;
                draw = false;
            } else if (score == highestScore) {
                draw = true;
            }
        }

        return draw ? null : highestTeam;
    }

    /**
     * スコアボードを更新する
     *
     * @param time
     */
    public static void refresh(int time) {
        objective = getBoard().getObjective(objectiveName);
        if (objective == null) {
            objective = getBoard().registerNewObjective(objectiveName, "dummy", mainObjectiveDisplayName);
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        currentScoreString.forEach(s -> getBoard().resetScores(s));
        currentScoreString.clear();

        Score timeScore = objective.getScore(remainingTime + ChatColor.YELLOW + time);
        timeScore.setScore(0);
        currentScoreString.add(timeScore.getEntry());

        int i = 1;
        for (Map.Entry<OCTeam, Integer> entry : scores.entrySet()) {
            Score score = objective.getScore(entry.getKey().name + ": " + ChatColor.YELLOW + entry.getValue());
            score.setScore(i);
            currentScoreString.add(score.getEntry());
            i++;
        }
    }

    /**
     * スコアボードを削除する
     */
    public static void destroy() {
        scores.clear();
        if (objective == null)
            return;

        objective.unregister();
        currentScoreString.clear();
    }

    private static Scoreboard getBoard() {
        return Bukkit.getScoreboardManager().getMainScoreboard();
    }

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }
}
