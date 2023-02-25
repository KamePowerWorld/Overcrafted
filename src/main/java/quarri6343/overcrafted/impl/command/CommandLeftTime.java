package quarri6343.overcrafted.impl.command;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.OCLogic;
import quarri6343.overcrafted.core.data.OCVariableData;

import java.util.List;

/**
 * ゲームの残り時間を変更する、デバッグコマンド
 */
public class CommandLeftTime implements Listener {

    private final String commandName = "lefttime";

    public CommandLeftTime() {
        Overcrafted.getInstance().getServer().getPluginManager().registerEvents(this, Overcrafted.getInstance());

        new CommandBase(commandName, 1, 1, true) {
            @Override
            public boolean onCommand(CommandSender sender, String[] argments) {
                if (getLogic().gameStatus != OCLogic.GameStatus.ACTIVE) {
                    sender.sendMessage("このコマンドはゲーム中にしか実行できません");
                    return true;
                }
                
                int leftTime = 0;
                try{
                    leftTime = Integer.parseInt(argments[0]);
                }
                catch (NumberFormatException e){
                    sender.sendMessage("数字を入力してください");
                    return true;
                }
                
                if(leftTime < 0 || leftTime > getData().getSelectedStage().get().getTime()){
                    sender.sendMessage("数字が不適切です");
                    return true;
                }

                int count = (getData().getSelectedStage().get().getTime() - leftTime) * 20;
                getLogic().getGameRunnable().setCount(count);
                
                return true;
            }

            @Override
            public String getUsage() {
                return "/lefttime {秒数}";
            }
        };
    }

    @EventHandler
    public void AsyncTabCompleteEvent(AsyncTabCompleteEvent e) {
        if (e.getBuffer().startsWith("/" + commandName + "")) {
            List<String> suggestions = List.of("{秒数}");
            e.setCompletions(suggestions);
        }
    }

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }
}
