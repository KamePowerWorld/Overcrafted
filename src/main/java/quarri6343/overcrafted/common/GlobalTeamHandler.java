package quarri6343.overcrafted.common;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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

        if (getLogic().gameStatus == OCLogic.GameStatus.ACTIVE && countAllPlayers() == 0) {
            getLogic().endGame(null, null, OCLogic.GameResult.FAIL, true);
        }
    }

    /**
     * チームメンバーをチームに加入した位置にテレポートさせる
     */
    public static void teleportTeamToLobby() {
        for (int i = 0; i < getData().teams.getTeamsLength(); i++) {
            OCTeam team = getData().teams.getTeam(i);
            if (team.joinLocation1 == null || team.joinLocation2 == null)
                continue;

            Location centerLocation = OvercraftedUtils.getCenterLocation(team.joinLocation1, team.joinLocation2);
            for (int j = 0; j < team.getPlayersSize(); j++) {
                team.getPlayer(j).teleport(centerLocation);
            }
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
            }
        }

        if (countAllPlayers() == 0) {
            gameMaster.sendMessage("誰もチームに参加していません!");
            return false;
        }

        return true;
    }

    /**
     * チームに参加している全てのプレイヤーをカウントする
     *
     * @return 全てのチームのプレイヤー数の合計
     */
    public static int countAllPlayers() {
        int playerCount = 0;
        for (int i = 0; i < getData().teams.getTeamsLength(); i++) {
            playerCount += getData().teams.getTeam(i).getPlayersSize();
        }

        return playerCount;
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

    /**
     * チーム中のプレイヤーがアイテムを2つ以上持っていた場合、余剰分をドロップさせる
     */
    public static void dropExcessiveItems() {
        for (int i = 0; i < getData().teams.getTeamsLength(); i++) {
            OCTeam team = getData().teams.getTeam(i);
            for (int j = 0; j < team.getPlayersSize(); j++) {
                Player player = team.getPlayer(j);

                ItemStack mainHandItem = player.getInventory().getItem(0);
                if (mainHandItem != null && mainHandItem.getAmount() > 1) {
                    try{
                        mainHandItem.setAmount(mainHandItem.getAmount() - 1);
                        player.getWorld().dropItemNaturally(player.getLocation(), mainHandItem);
                    }
                    finally {
                        mainHandItem.setAmount(1);
                        player.getInventory().setItem(0, mainHandItem);
                    }
                }

                ItemStack offHandItem = player.getInventory().getItemInOffHand();
                if (offHandItem.getAmount() > 0){
                    player.getInventory().setItemInOffHand(null);
                    player.getWorld().dropItemNaturally(player.getLocation(), offHandItem);
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
