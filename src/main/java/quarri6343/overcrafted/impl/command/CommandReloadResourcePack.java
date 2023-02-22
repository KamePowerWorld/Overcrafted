package quarri6343.overcrafted.impl.command;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.core.OCLogic;
import quarri6343.overcrafted.utils.ResourcePackUtil;

/**
 * リソースパックをリロードするコマンド
 */
public class CommandReloadResourcePack extends CommandBase {

    private static final String commandName = "reloadresourcepack";

    public CommandReloadResourcePack() {
        super(commandName, 0, 0, true);
    }

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @Nullable String[] arguments) {
        if (getLogic().gameStatus != OCLogic.GameStatus.INACTIVE) {
            sender.sendMessage("このコマンドはゲームが有効でない時しか実行できません");
            return true;
        }

        sender.sendMessage(Component.text("リソースパックのリロードを開始します。もしリソースパックの中身が変わっていた場合、" +
                "これからログインするプレイヤーはリソースパックを再ダウンロードすることになります"));
        ResourcePackUtil.reloadResourcePack();
        return true;
    }
}
