package quarri6343.overcrafted.core.ui;

import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.object.IDishPile;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.core.OCLogic;
import quarri6343.overcrafted.core.object.DishPile;
import quarri6343.overcrafted.impl.OCStages;
import quarri6343.overcrafted.impl.item.StackedDish;
import quarri6343.overcrafted.utils.ItemCreator;
import quarri6343.overcrafted.utils.UIUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static quarri6343.overcrafted.utils.UIUtility.*;

/**
 * 管理メニューの2行目
 */
public class AdminMenuRow2 {
    private static final List<Component> joinTeamButtonGuide = Arrays.asList(
            Component.text("コマンド/forcejoin {プレイヤー名}")
                    .color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("を使用してください")
                    .color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
    private static final List<Component> leaveTeamButtonGuide = Arrays.asList(
            Component.text("コマンド/forceleave {プレイヤー名}")
                    .color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("を使用してください")
                    .color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
    private static final List<Component> setJoinLocationButtonGuide = Arrays.asList(
            Component.text("ゲームが始まった時、")
                    .color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
            Component.text("このエリア内にいる人は")
                    .color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
            Component.text("選択中のチームに参加できます")
                    .color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }

    public static void addElements(PaginatedGui gui, Player player) {
        ItemStack forceJoinItem = new ItemCreator(Material.GREEN_BANNER).setName(Component.text("選択中のチームにプレイヤーを強制加入させる"))
                .setLores(joinTeamButtonGuide).create();
        GuiItem forceJoinButton = new GuiItem(forceJoinItem, event -> {
        });
        gui.setItem(9, forceJoinButton);

        ItemStack forceLeaveItem = new ItemCreator(Material.RED_BANNER).setName(Component.text("プレイヤーをチームから外す"))
                .setLores(leaveTeamButtonGuide).create();
        GuiItem forceLeaveButton = new GuiItem(forceLeaveItem, event -> {
        });
        gui.setItem(11, forceLeaveButton);

        ItemStack setJoinLocation1Item = new ItemCreator(Material.STRUCTURE_BLOCK).setName(Component.text("チーム" + getData().getAdminSelectedTeam() + "の参加エリアの始点を選ぶ"))
                .setLores(setJoinLocationButtonGuide).addLore(getSetJoinLocation1ButtonStats()).create();
        GuiItem setJoinLocation1Button = new GuiItem(setJoinLocation1Item,
                event -> {
                    onSetJoinLocationButton(event, true);
                    UIAdminMenu.openUI((Player) event.getWhoClicked());
                });
        gui.setItem(13, setJoinLocation1Button);

        ItemStack setJoinLocation2Item = new ItemCreator(Material.STRUCTURE_BLOCK).setName(Component.text("チーム" + getData().getAdminSelectedTeam() + "の参加エリアの終点を選ぶ"))
                .setLores(setJoinLocationButtonGuide).addLore(getSetJoinLocation2ButtonStats()).create();
        GuiItem setJoinLocation2Button = new GuiItem(setJoinLocation2Item,
                event -> {
                    onSetJoinLocationButton(event, false);
                    UIAdminMenu.openUI((Player) event.getWhoClicked());
                });
        gui.setItem(15, setJoinLocation2Button);

        ItemStack resetTeamSettingsItem = new ItemCreator(Material.PUFFERFISH).setName(Component.text("チーム" + getData().getAdminSelectedTeam() + "の設定をリセットする")).create();
        GuiItem resetTeamSettingsButton = new GuiItem(resetTeamSettingsItem,
                event -> {
                    onResetTeamSettingsButton(event);
                    UIAdminMenu.openUI((Player) event.getWhoClicked());
                });
        gui.setItem(17, resetTeamSettingsButton);
    }

    /**
     * 加入エリアの始点の登録状況を返す
     *
     * @return 登録状況
     */
    private static TextComponent getSetJoinLocation1ButtonStats() {
        IOCTeam selectedTeam = getData().getTeams().getTeamByName(getData().getAdminSelectedTeam());
        if (selectedTeam == null) {
            return teamNotSelectedText;
        }

        return getLocDesc(selectedTeam.getJoinLocation1());
    }

    /**
     * 加入エリアの終点の登録状況を返す
     *
     * @return 登録状況
     */
    private static TextComponent getSetJoinLocation2ButtonStats() {
        IOCTeam selectedTeam = getData().getTeams().getTeamByName(getData().getAdminSelectedTeam());
        if (selectedTeam == null) {
            return teamNotSelectedText;
        }

        return getLocDesc(selectedTeam.getJoinLocation2());
    }

    /**
     * チーム設定をリセットするボタンの挙動
     *
     * @param event
     */
    private static void onResetTeamSettingsButton(InventoryClickEvent event) {
        if (getLogic().gameStatus != OCLogic.GameStatus.INACTIVE) {
            event.getWhoClicked().sendMessage(gameRunningText);
            return;
        }

        IOCTeam team = getData().getTeams().getTeamByName(getData().getAdminSelectedTeam());
        if (team == null) {
            event.getWhoClicked().sendMessage(teamNotSelectedText);
            return;
        }

        team.setStartLocations(new ArrayList<>(Collections.nCopies(OCStages.values().length, null)));
        team.setJoinLocation1(null);
        team.setJoinLocation2(null);

        team.getCleanDishPiles().clear();
        team.getDirtyDishPiles().clear();
        for (int i = 0; i < OCStages.values().length; i++) {
            team.getCleanDishPiles().add(new DishPile(StackedDish.StackedDishType.CLEAN));
            team.getDirtyDishPiles().add(new DishPile(StackedDish.StackedDishType.DIRTY));
        }
        
        event.getWhoClicked().sendMessage(Component.text("チーム" + team.getName() + "の設定をリセットしました"));
    }

    /**
     * チーム加入地点を設定するボタンの挙動
     *
     * @param isLocation1 チーム加入地点1かどうか
     */
    private static void onSetJoinLocationButton(InventoryClickEvent event, boolean isLocation1) {
        IOCTeam selectedTeam = getData().getTeams().getTeamByName(getData().getAdminSelectedTeam());
        if (selectedTeam == null) {
            event.getWhoClicked().sendMessage(teamNotSelectedText);
            return;
        }

        if (isLocation1) {
            selectedTeam.setJoinLocation1(event.getWhoClicked().getLocation());
            event.getWhoClicked().sendMessage(Component.text("チーム" + getData().getAdminSelectedTeam() + "の参加エリアの始点を" + UIUtility.locationBlockPostoString(event.getWhoClicked().getLocation()) + "に設定しました"));
        } else {
            selectedTeam.setJoinLocation2(event.getWhoClicked().getLocation());
            event.getWhoClicked().sendMessage(Component.text("チーム" + getData().getAdminSelectedTeam() + "の参加エリアの終点を" + UIUtility.locationBlockPostoString(event.getWhoClicked().getLocation()) + "に設定しました"));
        }
    }
}
