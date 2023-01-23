package quarri6343.overcrafted.impl.command;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.CommandBase;
import quarri6343.overcrafted.api.item.interfaces.IOCItem;
import quarri6343.overcrafted.impl.item.OCItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * プレイヤーにカスタムアイテムを渡すコマンド
 */
public class CommandOCGive implements Listener {

    private final String commandName = "ocgive";

    public CommandOCGive() {
        Overcrafted.getInstance().getServer().getPluginManager().registerEvents(this, Overcrafted.getInstance());

        new CommandBase(commandName, 1, 3, true) {

            @Override
            public boolean onCommand(CommandSender sender, String[] argments) {
                IOCItem item = OCItems.commandToOCItem(argments[0]);
                if (item != null) {
                    if (argments.length > 1) {
                        item.onGiveCommand(sender, Arrays.copyOfRange(argments, 1, argments.length));
                    } else {
                        item.onGiveCommand(sender, null);
                    }
                } else {
                    sendUsage(sender);
                }

                return true;
            }

            @Override
            public String getUsage() {
                return "/ocgive {itemName} {arguments}";
            }
        };
    }

    @EventHandler
    public void AsyncTabCompleteEvent(AsyncTabCompleteEvent e) {
        if (e.getBuffer().startsWith("/" + commandName + "")) {
            List<String> suggestions = new ArrayList<>();
            Arrays.stream(OCItems.values()).forEach(v -> suggestions.add(v.get().getInternalName()));
            e.setCompletions(suggestions);
        }
    }
}
