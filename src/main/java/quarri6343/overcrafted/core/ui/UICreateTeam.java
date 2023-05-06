package quarri6343.overcrafted.core.ui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.impl.OCStages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 管理者がチームを作成するときのGUI
 */
public class UICreateTeam {

    private static String inputtedTeamName = "";
    private static String inputtedTeamColor = "";

    /**
     * チーム名入力フォームを開く
     */
    public static void openUI(Player player) {
        new AnvilGUI.Builder().onClick(UICreateTeam::onTeamNameInputted).text("name").title("チームの名前を入力").plugin(Overcrafted.getInstance()).open(player);
    }

    /**
     * チーム名が入力された時の挙動
     */
    private static List<AnvilGUI.ResponseAction> onTeamNameInputted(int slot, AnvilGUI.StateSnapshot snapshot) {
        if(slot != AnvilGUI.Slot.OUTPUT) {
            return Collections.emptyList();
        }

        OCVariableData data = Overcrafted.getInstance().getData();
        if (data.getTeams().getTeamByName(snapshot.getText()) != null) {
            snapshot.getPlayer().sendMessage(Component.text("その名前のチームは既に存在します").color(NamedTextColor.RED));
            return AnvilGUI.Response.close();
        }

        inputtedTeamName = snapshot.getText();
        openColorUI(snapshot.getPlayer());
        return AnvilGUI.Response.close();
    }

    /**
     * チームカラー入力フォームを開く
     */
    private static void openColorUI(Player player) {
        new AnvilGUI.Builder().onClick(UICreateTeam::onTeamColorInputted).text("color").title("チームの色を入力。例：red").plugin(Overcrafted.getInstance()).open(player);
    }

    /**
     * チームカラーが入力された時の挙動
     */
    private static List<AnvilGUI.ResponseAction> onTeamColorInputted(int slot, AnvilGUI.StateSnapshot snapshot) {
        if(slot != AnvilGUI.Slot.OUTPUT) {
            return Collections.emptyList();
        }
        
        if (NamedTextColor.NAMES.value(snapshot.getText()) == null) {
            snapshot.getPlayer().sendMessage(Component.text("チームカラーが不正です。redやgreenのように半角小文字で指定してください").color(NamedTextColor.RED));
            return AnvilGUI.Response.close();
        }

        OCVariableData data = Overcrafted.getInstance().getData();
        if (data.getTeams().getTeamByColor(snapshot.getText()) != null) {
            snapshot.getPlayer().sendMessage(Component.text("その色のチームは既に存在します").color(NamedTextColor.RED));
            return AnvilGUI.Response.close();
        }

        inputtedTeamColor = snapshot.getText();

        data.getTeams().addTeam(inputtedTeamName, inputtedTeamColor);
        snapshot.getPlayer().sendMessage(Component.text("チーム「" + inputtedTeamName + "」を作成しました").color(NamedTextColor.NAMES.value(inputtedTeamColor)));
        return AnvilGUI.Response.close();
    }
}
