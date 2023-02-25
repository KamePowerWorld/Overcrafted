package quarri6343.overcrafted.core.ui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.core.handler.GlobalTeamHandler;

import java.util.List;

/**
 * 管理者がチームを削除するときの確認画面
 */
public class UIRemoveTeam {

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }
    
    /**
     * チーム削除の確認画面を開く
     */
    public static void openUI(Player player) {
        new AnvilGUI.Builder().onComplete(UIRemoveTeam::onResponseInputted).text("no").title("本当にチームを消す場合yesと入力してください").plugin(Overcrafted.getInstance()).open(player);
    }

    /**
     * 返答が入力された時の挙動
     */
    private static List<AnvilGUI.ResponseAction> onResponseInputted(Player player, String text) {
        if(text.equals("yes") || text.equals("YES")){
            IOCTeam team = getData().getTeams().getTeamByName(getData().getAdminSelectedTeam());
            
            GlobalTeamHandler.removeAllPlayerFromTeam(team, false);
            getData().getTeams().removeTeam(getData().getAdminSelectedTeam());
            player.sendMessage(Component.text("チーム" + getData().getAdminSelectedTeam() + "を削除しました").color(NamedTextColor.WHITE));
            getData().setAdminSelectedTeam("");
        }
        else{
            player.sendMessage(Component.text("チーム削除をキャンセルしました"));
        }
        return AnvilGUI.Response.close();
    }
}
