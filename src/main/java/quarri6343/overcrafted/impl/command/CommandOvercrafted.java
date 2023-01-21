package quarri6343.overcrafted.impl.command;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import quarri6343.overcrafted.api.CommandBase;
import quarri6343.overcrafted.common.event.AdminMenuInteractEventHandler;
import quarri6343.overcrafted.utils.ItemCreator;

/**
 * 管理者に管理用メニューを配布するコマンド
 */
public class CommandOvercrafted extends CommandBase {

    private static final String commandName = "overcrafted";

    public CommandOvercrafted() {
        super(commandName, 0, 0, true);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @Nullable String[] arguments) {
        ((Player) sender).getInventory().addItem(new ItemCreator(Material.STICK).setName(AdminMenuInteractEventHandler.menuItemName).create());
        return true;
    }
}
