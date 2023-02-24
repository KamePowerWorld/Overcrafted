package quarri6343.overcrafted.core.ui;

import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.handler.GlobalTeamHandler;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.core.OCLogic;
import quarri6343.overcrafted.utils.ItemCreator;
import quarri6343.overcrafted.utils.UIUtility;

import static quarri6343.overcrafted.utils.UIUtility.*;

public class AdminMenuRow1 {
    private static final TextComponent setStartButtonGuide = Component.text("現在立っている場所が開始地点になります")
            .color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false);
    private static final TextComponent setEndButtonGuide = Component.text("現在立っている場所が終了地点になります")
            .color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false);

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }

    public static void addElements(PaginatedGui gui, Player player) {
        ItemStack createTeamItem = new ItemCreator(Material.WHITE_BANNER).setName(Component.text("新しいチームを作成"))
                .create();
        GuiItem createTeamButton = new GuiItem(createTeamItem,
                event -> UICreateTeam.openUI(player));
        gui.setItem(0, createTeamButton);

        GuiItem removeTeamButton;
        ItemStack removeTeamItem = new ItemCreator(Material.BLACK_BANNER).setName(Component.text("選択中のチームを削除"))
                .setLore(getRemoveTeamDesc()).create();
        removeTeamButton = new GuiItem(removeTeamItem, AdminMenuRow1::onRemoveTeamButton);
        gui.setItem(2, removeTeamButton);

        ItemStack selectTeamItem = new ItemCreator(Material.RESPAWN_ANCHOR).setName(Component.text("設定変更するチームを選択"))
                .create();
        GuiItem selectTeamButton = new GuiItem(selectTeamItem,
                event -> UIAdminSelectTeam.openUI(player));
        gui.setItem(4, selectTeamButton);

        ItemStack selectStageItem = new ItemCreator(Material.CARTOGRAPHY_TABLE).setName(Component.text("ステージを選択"))
                .create();
        GuiItem selectStageButton = new GuiItem(selectStageItem,
                AdminMenuRow1::onSelectStageButton);
        gui.setItem(6, selectStageButton);

        GuiItem setStartButton;
        ItemStack setStartItem = new ItemCreator(Material.FURNACE_MINECART).setName(Component.text(
                ChatColor.YELLOW + (getData().getSelectedStage() != null ? getData().getSelectedStage().get().getName() : "ステージ?") + ChatColor.RESET + "でのチーム" + getData().getAdminSelectedTeam() + "のゲーム開始地点を設定"))
                .addLore(getSetStartButtonStats()).addLore(setStartButtonGuide).create();
        setStartButton = new GuiItem(setStartItem,
                event -> {
                    onSetStartButton(event);
                    UIAdminMenu.openUI((Player) event.getWhoClicked());
                });
        gui.setItem(8, setStartButton);
    }

    /**
     * @return 初期位置を設定するボタンに表示する現在の状況
     */
    private static TextComponent getSetStartButtonStats() {
        IOCTeam team = getData().getTeams().getTeamByName(getData().getAdminSelectedTeam());
        if (team == null) {
            return teamNotSelectedText;
        }

        return getLocDesc(team.getStartLocations().get(getData().getSelectedStage().ordinal()));
    }

    /**
     * 初期位置を設定するボタンを押したときのイベント
     */
    private static void onSetStartButton(InventoryClickEvent event) {
        IOCTeam team = getData().getTeams().getTeamByName(getData().getAdminSelectedTeam());
        if (team == null) {
            event.getWhoClicked().sendMessage(teamNotSelectedText);
            return;
        }

        if (getLogic().gameStatus != OCLogic.GameStatus.INACTIVE) {
            event.getWhoClicked().sendMessage(gameRunningText);
            return;
        }

        Location startLocation = event.getWhoClicked().getLocation();

        team.getStartLocations().set(getData().getSelectedStage().ordinal(), startLocation);
        event.getWhoClicked().sendMessage(Component.text("開始地点を" + UIUtility.locationBlockPostoString(startLocation) + "に設定しました"));
        UIAdminMenu.openUI((Player) event.getWhoClicked());
    }

    /**
     * チームを削除するボタンを押したときのイベント
     */
    private static void onRemoveTeamButton(InventoryClickEvent event) {
        if (getLogic().gameStatus != OCLogic.GameStatus.INACTIVE) {
            event.getWhoClicked().sendMessage(gameRunningText);
            return;
        }

        if (getData().getAdminSelectedTeam().isEmpty()) {
            event.getWhoClicked().sendMessage(teamNotSelectedText);
            return;
        }

        IOCTeam team = getData().getTeams().getTeamByName(getData().getAdminSelectedTeam());
        if (team == null)
            return;

        GlobalTeamHandler.removeAllPlayerFromTeam(team, false);
        getData().getTeams().removeTeam(getData().getAdminSelectedTeam());
        event.getWhoClicked().sendMessage(Component.text("チーム" + getData().getAdminSelectedTeam() + "を削除しました").color(NamedTextColor.WHITE));
        getData().setAdminSelectedTeam("");
    }

    /**
     * @return チーム削除ボタンの説明文
     */
    private static TextComponent getRemoveTeamDesc() {
        if (getLogic().gameStatus != OCLogic.GameStatus.INACTIVE) {
            return gameRunningText;
        }

        if (getData().getAdminSelectedTeam().equals("")) {
            return teamNotSelectedText;
        }

        return Component.text("選択中のチーム:" + getData().getAdminSelectedTeam())
                .color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false);
    }

    /**
     * ステージを設定するボタンを押したときのイベント
     * @param event
     */
    private static void onSelectStageButton(InventoryClickEvent event){
        if (getLogic().gameStatus != OCLogic.GameStatus.INACTIVE) {
            event.getWhoClicked().sendMessage(gameRunningText);
            return;
        }

        UIAdminSelectStage.openUI((Player) event.getWhoClicked());
    }
}
