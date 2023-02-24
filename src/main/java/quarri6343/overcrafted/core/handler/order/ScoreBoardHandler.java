package quarri6343.overcrafted.core.handler.order;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.api.object.IOCTeam;

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

    private static Objective objective;

    private final static Map<IOCTeam, Integer> scores = new HashMap<>();
    private final static List<String> currentScoreString = new ArrayList<>();

    /**
     * スコアボードを作成する
     */
    public static void initialize() {
        objective = getBoard().getObjective(objectiveName);
        destroy();

        for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
            IOCTeam team = getData().getTeams().getTeam(i);
            scores.put(team, 0);
        }

        refresh();
    }

    /**
     * スコアを追加する
     *
     * @param team
     */
    public static void addScore(IOCTeam team, int value) {
        if (scores.get(team) == null)
            return;

        scores.put(team, scores.get(team) + value);
        
        refresh();
    }

    /**
     * 最もスコアの高いチームを取得する
     *
     * @return 最もスコアの高いチーム、もしチームが存在しないまたは複数存在した場合null
     */
    public static IOCTeam getHighestScoreTeam() {
        int highestScore = 0;
        IOCTeam highestTeam = null;
        boolean draw = false;

        for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
            IOCTeam team = getData().getTeams().getTeam(i);
            Integer score = scores.get(team);
            if(score == null)
                continue;

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
     * 最もスコアの高いチームを取得する
     *
     * @return 最もスコアの高いチーム、もしチームが存在しないまたは複数存在した場合null
     */
    public static int getHighestScore() {
        int highestScore = 0;

        for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
            IOCTeam team = getData().getTeams().getTeam(i);
            Integer score = scores.get(team);
            if(score == null)
                continue;

            if (score > highestScore) {
                highestScore = score;
            }
        }

        return highestScore;
    }

    /**
     * スコアボードを更新する
     */
    private static void refresh() {
        objective = getBoard().getObjective(objectiveName);
        if (objective == null) {
            objective = getBoard().registerNewObjective(objectiveName, "dummy", mainObjectiveDisplayName);
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        currentScoreString.forEach(s -> getBoard().resetScores(s));
        currentScoreString.clear();

        int i = 1;
        for (Map.Entry<IOCTeam, Integer> entry : scores.entrySet()) {
            Score score = objective.getScore(entry.getKey().getName() + ": " + ChatColor.YELLOW + entry.getValue());
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

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }
}
