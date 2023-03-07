package quarri6343.overcrafted.core.ui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.object.RangedFloat;
import quarri6343.overcrafted.core.object.RangedInt;

import java.util.List;

/**
 * 数字入力を受け付ける金床UIのクラス
 */
public class UINumberConfiguration {

    public static void openUI(Player player, RangedInt field) {
        new AnvilGUI.Builder().onComplete((player1, s) -> onNumberInputted(player, s, field)).text("number").title("数値設定").plugin(Overcrafted.getInstance()).open(player);
    }

    public static void openUI(Player player, RangedFloat field) {
        new AnvilGUI.Builder().onComplete((player1, s) -> onNumberInputted(player, s, field)).text("number").title("数値設定").plugin(Overcrafted.getInstance()).open(player);
    }

    private static List<AnvilGUI.ResponseAction> onNumberInputted(Player player, String text, RangedInt field) {
        int result;
        try {
            result = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            player.sendMessage(Component.text("数字以外を入力しないでください").color(NamedTextColor.RED));
            return AnvilGUI.Response.close();
        }

        if (!field.isValid(result)) {
            player.sendMessage("現実的な数を入力してください");
            return AnvilGUI.Response.close();
        }

        field.set(result);
        player.sendMessage(Component.text("数値を" + result + "に設定しました"));
        return AnvilGUI.Response.close();
    }

    private static List<AnvilGUI.ResponseAction> onNumberInputted(Player player, String text, RangedFloat field) {
        float result;
        try {
            result = Float.parseFloat(text);
        } catch (NumberFormatException e) {
            player.sendMessage(Component.text("数字以外を入力しないでください").color(NamedTextColor.RED));
            return AnvilGUI.Response.close();
        }

        if (!field.isValid(result)) {
            player.sendMessage("現実的な数を入力してください");
            return AnvilGUI.Response.close();
        }

        field.set(result);
        player.sendMessage(Component.text("数値を" + result + "に設定しました"));
        return AnvilGUI.Response.close();
    }
}
