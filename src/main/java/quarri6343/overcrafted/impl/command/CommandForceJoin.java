package quarri6343.overcrafted.impl.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.CommandBase;
import quarri6343.overcrafted.common.GlobalTeamHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;

/**
 * プレイヤーを強制的にチームに参加させるコマンド。ゲーム中しか実行できない
 */
public class CommandForceJoin extends CommandBase {

    private static final String commandName = "forcejoin";

    public CommandForceJoin() {
        super(commandName, 1, 1, true);
    }

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @Nullable String[] arguments) {
        if (getLogic().gameStatus != OCLogic.GameStatus.ACTIVE){
            sender.sendMessage("このコマンドはゲーム中にしか実行できません");
            return true;
        }
        
        if (getData().adminSelectedTeam.isEmpty()) {
            sender.sendMessage("まずGUIで加入させたいチームを選択してください");
            return true;
        }

        Player player = Bukkit.getPlayer(arguments[0]);
        if (player == null) {
            sender.sendMessage("その名前のプレイヤーは存在しません");
            return true;
        }

        if (getData().teams.getTeambyPlayer(player) != null) {
            GlobalTeamHandler.removePlayerFromTeam(player);
            sender.sendMessage(arguments[0] + "が既にチームに入っていたので離脱させました");
        }

        OCTeam team = getData().teams.getTeambyName(getData().adminSelectedTeam);
        if (team == null)
            return true;

        GlobalTeamHandler.addPlayerToTeam(player, team);
        sender.sendMessage(arguments[0] + "をチーム" + getData().adminSelectedTeam + "に加入させました");

        team.setUpGameEnvforPlayer(player);
        
        return true;
    }
}
