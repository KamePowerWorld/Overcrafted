package quarri6343.overcrafted.impl.task.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.api.IOCTeam;
import quarri6343.overcrafted.core.data.constant.OCCommonData;
import quarri6343.overcrafted.utils.OverCraftedUtil;

import java.util.ArrayList;
import java.util.List;

import static quarri6343.overcrafted.core.handler.GlobalTeamHandler.addPlayerToTeam;
import static quarri6343.overcrafted.core.handler.GlobalTeamHandler.removePlayerFromTeam;

/**
 * ゲームが始まる前に走る処理
 */
public class GameInactiveTask extends BukkitRunnable {

    private int count = 0;

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }

    @Override
    public void run() {
        if (count % OCCommonData.assignTeamLength == 0) {
            assignOrUnAssignPlayersToTeam();
        }
    }

    /**
     * チームの加入エリアにいるプレイヤーを自動的にチームに加入させ、加入していないプレイヤーをチームから外す
     */
    private void assignOrUnAssignPlayersToTeam() {
        List<Player> assignedPlayers = new ArrayList<>();

        for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
            IOCTeam team = getData().getTeams().getTeam(i);

            if (team.getJoinLocation1() == null || team.getJoinLocation2() == null)
                continue;

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (assignedPlayers.contains(onlinePlayer))
                    continue;

                if (OverCraftedUtil.isPlayerInArea(onlinePlayer, team.getJoinLocation1(), team.getJoinLocation2())) {
                    addPlayerToTeam(onlinePlayer, team);
                    assignedPlayers.add(onlinePlayer);
                }
            }
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!assignedPlayers.contains(onlinePlayer)) {
                removePlayerFromTeam(onlinePlayer, false);
            }
        }
    }
}
