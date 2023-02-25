package quarri6343.overcrafted.core.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.OCLogic;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.utils.OverCraftedUtil;

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
    public static void addPlayerToTeam(Player player, IOCTeam team) {
        team.addPlayer(player);
        MCTeams.addPlayerToMCTeam(player, team);
    }

    /**
     * プレイヤーをUR, MC両方のチームから退出させる
     */
    public static void removePlayerFromTeam(Player player, boolean restoreStats) {
        IOCTeam team = getData().getTeams().getTeamByPlayer(player);
        if (team != null) {
            team.removePlayer(player, restoreStats);
        }
        MCTeams.removePlayerFromMCTeam(player);

        if (getLogic().gameStatus == OCLogic.GameStatus.ACTIVE && getData().getTeams().countAllPlayers() == 0) {
            getLogic().endGame(null, null, false, true);
        }
    }

    /**
     * あるチームの全てのプレイヤーをUR, MC両方のチームから退出させる
     *
     * @param restoreStats プレイヤーのインベントリを復元するかどうか
     */
    public static void removeAllPlayerFromTeam(IOCTeam team, boolean restoreStats) {
        for (int i = 0; i < team.getPlayersSize(); i++) {
            removePlayerFromTeam(team.getPlayer(i), restoreStats);
        }
    }

    /**
     * 全てのチームの全てのプレイヤーをUR, MC両方のチームから退出させる
     *
     * @param restoreStats プレイヤーのインベントリを復元するかどうか
     */
    public static void resetTeams(boolean restoreStats) {
        MCTeams.deleteMinecraftTeams();
        getData().getTeams().disbandTeams(restoreStats);
    }

    /**
     * チームの状態がゲームを開始できる状態にあるか判定する
     *
     * @param gameMaster ゲーム開始者
     * @return ゲームを開始できるか
     */
    public static boolean areTeamsValid(Player gameMaster) {
        if (getData().getTeams().getTeamsLength() == 0) {
            gameMaster.sendMessage("チームが存在しません!");
            return false;
        }

        for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
            IOCTeam team = getData().getTeams().getTeam(i);

            if (team.getStartLocations().get(getData().getSelectedStage().ordinal()) == null) {
                gameMaster.sendMessage("チーム" + team.getName() + "の開始地点を設定してください");
                return false;
            }
            if (team.getCleanDishPiles().get(getData().getSelectedStage().ordinal()).getLocation() == null) {
                gameMaster.sendMessage("チーム" + team.getName() + "の綺麗な皿置き場の地点を設定してください");
                return false;
            }
            if (team.getDirtyDishPiles().get(getData().getSelectedStage().ordinal()).getLocation() == null) {
                gameMaster.sendMessage("チーム" + team.getName() + "の汚い皿置き場の地点を設定してください");
                return false;
            }
        }

        if (getData().getTeams().countAllPlayers() == 0) {
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
            for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
                if (getData().getTeams().getTeam(i).containsPlayer(onlinePlayer)) {
                    return;
                }
            }

            for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
                IOCTeam team = getData().getTeams().getTeam(i);

                if (team.getJoinLocation1() == null || team.getJoinLocation2() == null)
                    continue;

                if (OverCraftedUtil.isPlayerInArea(onlinePlayer, team.getJoinLocation1(), team.getJoinLocation2())) {
                    addPlayerToTeam(onlinePlayer, team);
                    break;
                }
            }
        }
    }

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }
}
