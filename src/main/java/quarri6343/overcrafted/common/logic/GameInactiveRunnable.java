package quarri6343.overcrafted.common.logic;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.utils.OvercraftedUtils;

import java.util.ArrayList;
import java.util.List;

import static quarri6343.overcrafted.common.GlobalTeamHandler.addPlayerToTeam;
import static quarri6343.overcrafted.common.GlobalTeamHandler.removePlayerFromTeam;

/**
 * ゲームが始まる前に走る処理
 */
public class GameInactiveRunnable extends BukkitRunnable {

    private int count = 0;
    
    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }
    
    @Override
    public void run() {
        if (count % OCData.assignTeamLength == 0) {
            assignOrUnAssignPlayersToTeam();
        }
    }

    /**
     * チームの加入エリアにいるプレイヤーを自動的にチームに加入させ、加入していないプレイヤーをチームから外す
     */
    private void assignOrUnAssignPlayersToTeam() {
        List<Player> assignedPlayers = new ArrayList<>();
                
        for (int i = 0; i < getData().teams.getTeamsLength(); i++) {
            OCTeam team = getData().teams.getTeam(i);

            if (team.joinLocation1 == null || team.joinLocation2 == null)
                continue;

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if(assignedPlayers.contains(onlinePlayer))
                    continue;
                
                if (OvercraftedUtils.isPlayerInArea(onlinePlayer, team.joinLocation1, team.joinLocation2)) {
                    addPlayerToTeam(onlinePlayer, team);
                    assignedPlayers.add(onlinePlayer);
                    break;
                }
            }
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(!assignedPlayers.contains(onlinePlayer)){
                removePlayerFromTeam(onlinePlayer, false);
            }
        }
    }
}
