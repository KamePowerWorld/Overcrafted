package quarri6343.overcrafted.impl.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.handler.GlobalTeamHandler;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.core.OCLogic;

/**
 * プレイヤーを強制的にチームから外すコマンド。ゲーム中しか実行できない
 */
public class CommandForceLeave extends CommandBase {

    private static final String commandName = "forceleave";

    public CommandForceLeave() {
        super(commandName, 1, 1, true);
    }

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @Nullable String[] arguments) {
        if (getLogic().gameStatus != OCLogic.GameStatus.ACTIVE) {
            sender.sendMessage("このコマンドはゲーム中にしか実行できません");
            return true;
        }

        Player player = Bukkit.getPlayer(arguments[0]);
        if (player == null) {
            sender.sendMessage("その名前のプレイヤーは存在しません");
            return true;
        }

        IOCTeam team = getData().getTeams().getTeamByPlayer(player);
        if (team == null) {
            sender.sendMessage(Component.text("プレイヤー" + arguments[0] + "はチームに所属していません").color(NamedTextColor.RED));
            return true;
        }

        GlobalTeamHandler.removePlayerFromTeam(player, true);
        sender.sendMessage(arguments[0] + "をチーム" + team.getName() + "から離脱させました");
        return true;
    }
}
