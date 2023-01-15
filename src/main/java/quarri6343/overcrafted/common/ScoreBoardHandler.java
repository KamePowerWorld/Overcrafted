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
    public static void addScore(OCTeam team, int value) {
        if (objective == null)
            initialize();

        Score score = objective.getScore(team.name);
        score.setScore(score.getScore() + value);
    }

    /**
     * スコアを取得する
     * 
     * @param team
     * @return
     */
    public static int getScore(OCTeam team){
        if (objective == null)
            initialize();

        Score score = objective.getScore(team.name);
        return score.getScore();
    }

    /**
     * 最もスコアの高いチームを取得する
     * @return 最もスコアの高いチーム、もしチームが存在しないまたは複数存在した場合null
     */
    public static OCTeam getHighestScoreTeam(){
        int highestScore = 0;
        OCTeam highestTeam = null;
        boolean draw = false;
        
        for (int i = 0; i < getData().teams.getTeamsLength(); i++) {
            OCTeam team = getData().teams.getTeam(i);
            int score = objective.getScore(team.name).getScore();
            
            if(score > highestScore){
                highestTeam = team;
                highestScore = score;
                draw = false;
            }
            else if (score == highestScore){
                draw = true;
            }
        }
        
        return draw ? null : highestTeam;
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
