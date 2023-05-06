package quarri6343.overcrafted.core.ui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.object.RangedFloat;
import quarri6343.overcrafted.core.object.RangedInt;

import java.util.Collections;
import java.util.List;

/**
 * 数字入力を受け付ける金床UIのクラス
 */
public class UINumberConfiguration {

    public static void openUI(Player player, RangedInt field) {
        new AnvilGUI.Builder().onClick((slot, snapshot) -> onNumberInputted(slot, snapshot, field)).text("number").title("数値設定").plugin(Overcrafted.getInstance()).open(player);
    }

    public static void openUI(Player player, RangedFloat field) {
        new AnvilGUI.Builder().onClick((slot, snapshot) -> onNumberInputted(slot, snapshot, field)).text("number").title("数値設定").plugin(Overcrafted.getInstance()).open(player);
    }

    private static List<AnvilGUI.ResponseAction> onNumberInputted(int slot, AnvilGUI.StateSnapshot snapshot, RangedInt field) {
        if(slot != AnvilGUI.Slot.OUTPUT) {
            return Collections.emptyList();
        }
        
        int result;
        try {
            result = Integer.parseInt(snapshot.getText());
        } catch (NumberFormatException e) {
            snapshot.getPlayer().sendMessage(Component.text("数字以外を入力しないでください").color(NamedTextColor.RED));
            return AnvilGUI.Response.close();
        }

        if (!field.isValid(result)) {
            snapshot.getPlayer().sendMessage("現実的な数を入力してください");
            return AnvilGUI.Response.close();
        }

        field.set(result);
        snapshot.getPlayer().sendMessage(Component.text("数値を" + result + "に設定しました"));
        return AnvilGUI.Response.close();
    }

    private static List<AnvilGUI.ResponseAction> onNumberInputted(int slot, AnvilGUI.StateSnapshot snapshot, RangedFloat field) {
        if(slot != AnvilGUI.Slot.OUTPUT) {
            return Collections.emptyList();
        }
        
        float result;
        try {
            result = Float.parseFloat(snapshot.getText());
        } catch (NumberFormatException e) {
            snapshot.getPlayer().sendMessage(Component.text("数字以外を入力しないでください").color(NamedTextColor.RED));
            return AnvilGUI.Response.close();
        }

        if (!field.isValid(result)) {
            snapshot.getPlayer().sendMessage("現実的な数を入力してください");
            return AnvilGUI.Response.close();
        }

        field.set(result);
        snapshot.getPlayer().sendMessage(Component.text("数値を" + result + "に設定しました"));
        return AnvilGUI.Response.close();
    }
}
