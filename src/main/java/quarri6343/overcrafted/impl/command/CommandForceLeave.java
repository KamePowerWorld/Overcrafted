package quarri6343.overcrafted.impl.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
 * プレイヤーを強制的にチームから外すコマンド。ゲーム中しか実行できない
 */
public class CommandForceLeave extends CommandBase {

    private static final String commandName = "forceleave";

    public CommandForceLeave() {
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
        
        Player player = Bukkit.getPlayer(arguments[0]);
        if (player == null) {
            sender.sendMessage("その名前のプレイヤーは存在しません");
            return true;
        }
        
        OCTeam team = getData().teams.getTeambyPlayer(player);
        if (team == null) {
            sender.sendMessage(Component.text("プレイヤー" + arguments[0] + "はチームに所属していません").color(NamedTextColor.RED));
            return true;
        }

        GlobalTeamHandler.removePlayerFromTeam(player);
        sender.sendMessage(arguments[0] + "をチーム" + team.name + "から離脱させました");
        return true;
    }
}
