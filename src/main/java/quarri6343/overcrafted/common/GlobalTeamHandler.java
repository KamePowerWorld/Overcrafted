package quarri6343.overcrafted.common;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.utils.OvercraftedUtils;

/**
 * UR,MC両方のチームクラスに対する処理を行う
 */
public class GlobalTeamHandler {

    /**
     * プレイヤーをUR, MC両方のチームに入れる
     *
     * @param player プレイヤー
     * @param team   入れたいURチーム
     */
    public static void addPlayerToTeam(Player player, OCTeam team) {
        team.addPlayer(player);
        MCTeams.addPlayerToMCTeam(player, team);
    }

    /**
     * プレイヤーをUR, MC両方のチームから退出させる
     */
    public static void removePlayerFromTeam(Player player, boolean restoreStats) {
        OCTeam team = getData().teams.getTeambyPlayer(player);
        if (team != null) {
            team.removePlayer(player, restoreStats);
        }
        MCTeams.removePlayerFromMCTeam(player);

        if (getLogic().gameStatus == OCLogic.GameStatus.ACTIVE && getData().teams.countAllPlayers() == 0) {
            getLogic().endGame(null, null, OCLogic.GameResult.FAIL, true);
        }
    }

    /**
     * 全てのプレイヤーをUR, MC両方のチームから退出させる
     */
    public static void removeAllPlayerFromTeam(OCTeam team, boolean restoreStats) {
        for (int i = 0; i < team.getPlayersSize(); i++) {
            removePlayerFromTeam(team.getPlayer(i), restoreStats);
        }
    }

    public static void resetTeams(boolean restoreStats) {
        MCTeams.deleteMinecraftTeams();
        getData().teams.disbandTeams(restoreStats);
    }

    /**
     * チームの状態がゲームを開始できる状態にあるか判定する
     *
     * @param gameMaster ゲーム開始者
     * @return ゲームを開始できるか
     */
    public static boolean areTeamsValid(Player gameMaster) {
        if (getData().teams.getTeamsLength() == 0) {
            gameMaster.sendMessage("チームが存在しません!");
            return false;
        }

        for (int i = 0; i < getData().teams.getTeamsLength(); i++) {
            OCTeam team = getData().teams.getTeam(i);

            if (team.getPlayersSize() > 0) {
                if (team.getStartLocation() == null) {
                    gameMaster.sendMessage("チーム" + team.name + "の開始地点を設定してください");
                    return false;
                }
                if (team.orderBox.location == null) {
                    gameMaster.sendMessage("チーム" + team.name + "の注文箱の地点を設定してください");
                    return false;
                }
            }
        }

        if (getData().teams.countAllPlayers() == 0) {
            gameMaster.sendMessage("誰もチームに参加していません!");
            return false;
        }

        return true;
    }

    /**
     * 参加エリアにいるプレイヤーをチームに割り当てる
     */
    public static void assignPlayersInJoinArea() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            for (int i = 0; i < getData().teams.getTeamsLength(); i++) {
                if (getData().teams.getTeam(i).containsPlayer(onlinePlayer)) {
                    return;
                }
            }

            for (int i = 0; i < getData().teams.getTeamsLength(); i++) {
                OCTeam team = getData().teams.getTeam(i);

                if (team.joinLocation1 == null || team.joinLocation2 == null)
                    continue;

                if (OvercraftedUtils.isPlayerInArea(onlinePlayer, team.joinLocation1, team.joinLocation2)) {
                    addPlayerToTeam(onlinePlayer, team);
                    break;
                }
            }
        }
    }

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }
}
