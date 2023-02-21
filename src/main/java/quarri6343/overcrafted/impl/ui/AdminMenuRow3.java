package quarri6343.overcrafted.impl.ui;

import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.interfaces.IDishPile;
import quarri6343.overcrafted.common.data.interfaces.IOCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.impl.item.OCItems;
import quarri6343.overcrafted.utils.ItemCreator;

import static quarri6343.overcrafted.utils.UIUtility.*;

public class AdminMenuRow3 {

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }

    public static void addElements(PaginatedGui gui, Player player) {
        ItemStack placeCleanDishPileItem = new ItemCreator(OCItems.DISH.get().getItemStack()).setName(Component.text("チーム" + getData().getAdminSelectedTeam() + "の綺麗な皿置場座標を設定")
                        .color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
                .addLore(getCleanDishPileLocationStats()).create();
        GuiItem placeCleanDishPileButton = new GuiItem(placeCleanDishPileItem,
                AdminMenuRow3::setUpCleanDishPile);
        gui.setItem(18, placeCleanDishPileButton);

        ItemStack placeDirtyDishPileItem = new ItemCreator(OCItems.DIRTY_DISH.get().getItemStack()).setName(Component.text("チーム" + getData().getAdminSelectedTeam() + "の汚い皿置場座標を設定")
                        .color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
                .addLore(getDirtyDishPileLocationStats()).create();
        GuiItem placeDirtyDishPileButton = new GuiItem(placeDirtyDishPileItem,
                AdminMenuRow3::setUpDirtyDishPile);
        gui.setItem(20, placeDirtyDishPileButton);

        GuiItem closeButton = new GuiItem(new ItemCreator(Material.BARRIER).setName(Component.text("閉じる")).create(),
                event -> gui.close(event.getWhoClicked()));
        gui.setItem(22, closeButton);

        GuiItem startButton = new GuiItem(new ItemCreator(Material.GREEN_WOOL).setName(Component.text("ゲームを開始")).setLore(getCanStartGameDesc()).create(),
                event -> {
                    getLogic().startGame((Player) event.getWhoClicked());
                    UIAdminMenu.openUI((Player) event.getWhoClicked());
                });
        gui.setItem(24, startButton);
        GuiItem endButton = new GuiItem(new ItemCreator(Material.RED_WOOL).setName(Component.text("ゲームを強制終了")).setLore(getCanTerminateGameDesc()).create(),
                event -> {
                    getLogic().endGame((Player) event.getWhoClicked(), null, false, true);
                    UIAdminMenu.openUI((Player) event.getWhoClicked());
                });
        gui.setItem(26, endButton);
    }


    /**
     * @return 現在ゲームを開始できるかどうかを示した文
     */
    private static TextComponent getCanStartGameDesc() {
        return getLogic().gameStatus == OCLogic.GameStatus.INACTIVE ?
                Component.text("開始可能").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                : Component.text("ゲームが進行中です!").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false);
    }

    /**
     * @return 現在ゲームを終了できるかどうかを示した文
     */
    private static TextComponent getCanTerminateGameDesc() {
        return getLogic().gameStatus == OCLogic.GameStatus.ACTIVE ?
                Component.text("強制終了可能").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                : Component.text("強制終了できるゲームはありません").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false);
    }

    /**
     * @return 綺麗な皿置き場の位置を設定するボタンに表示する現在の状況
     */
    private static TextComponent getCleanDishPileLocationStats() {
        IOCTeam team = getData().getTeams().getTeamByName(getData().getAdminSelectedTeam());
        if (team == null) {
            return teamNotSelectedText;
        }

        return getLocDesc(team.getCleanDishPile().getLocation());
    }


    /**
     * @return 汚い皿置き場の位置を設定するボタンに表示する現在の状況
     */
    private static TextComponent getDirtyDishPileLocationStats() {
        IOCTeam team = getData().getTeams().getTeamByName(getData().getAdminSelectedTeam());
        if (team == null) {
            return teamNotSelectedText;
        }

        return getLocDesc(team.getDirtyDishPile().getLocation());
    }

    /**
     * 現在立っている場所で綺麗な皿置場を登録する
     *
     * @param event
     */
    public static void setUpCleanDishPile(InventoryClickEvent event) {
        IOCTeam playerTeam = getData().getTeams().getTeamByName(getData().getAdminSelectedTeam());
        if (playerTeam == null) {
            event.getWhoClicked().sendMessage(teamNotSelectedText);
            return;
        }

        if (getLogic().gameStatus != OCLogic.GameStatus.INACTIVE) {
            event.getWhoClicked().sendMessage(gameRunningText);
            return;
        }

        for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
            IOCTeam team = getData().getTeams().getTeam(i);
            
            if(team != playerTeam && team.getCleanDishPile().getLocation() != null
                    && team.getCleanDishPile().getLocation().getBlock().equals(event.getWhoClicked().getLocation().getBlock())){
                event.getWhoClicked().sendMessage(Component.text("チーム" + team + "の綺麗な皿置場と場所が被っています"));
                return;
            }
            if(team.getDirtyDishPile().getLocation() != null
                    && team.getDirtyDishPile().getLocation().getBlock().equals(event.getWhoClicked().getLocation().getBlock())){
                event.getWhoClicked().sendMessage(Component.text("チーム" + team + "の汚い皿置場と場所が被っています"));
                return;
            }
        }
        
        IDishPile cleanDishPile = playerTeam.getCleanDishPile();
        cleanDishPile.setLocation(event.getWhoClicked().getLocation());
        event.getWhoClicked().sendMessage(Component.text("綺麗な皿置場を" + locationBlockPostoString(event.getWhoClicked().getLocation()) + "で登録しました"));
        UIAdminMenu.openUI((Player) event.getWhoClicked());
    }

    /**
     * 現在立っている場所で汚い皿置場を登録する
     *
     * @param event
     */
    public static void setUpDirtyDishPile(InventoryClickEvent event) {
        IOCTeam playerTeam = getData().getTeams().getTeamByName(getData().getAdminSelectedTeam());
        if (playerTeam == null) {
            event.getWhoClicked().sendMessage(teamNotSelectedText);
            return;
        }

        if (getLogic().gameStatus != OCLogic.GameStatus.INACTIVE) {
            event.getWhoClicked().sendMessage(gameRunningText);
            return;
        }

        for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
            IOCTeam team = getData().getTeams().getTeam(i);

            if(team.getCleanDishPile().getLocation() != null
                    && team.getCleanDishPile().getLocation().getBlock().equals(event.getWhoClicked().getLocation().getBlock())){
                event.getWhoClicked().sendMessage(Component.text("チーム" + team.getName() + "の綺麗な皿置場と場所が被っています"));
                return;
            }
            if(team != playerTeam && team.getDirtyDishPile().getLocation() != null
                    && team.getDirtyDishPile().getLocation().getBlock().equals(event.getWhoClicked().getLocation().getBlock())){
                event.getWhoClicked().sendMessage(Component.text("チーム" + team.getName() + "の汚い皿置場と場所が被っています"));
                return;
            }
        }

        IDishPile dirtyDishPile = playerTeam.getDirtyDishPile();
        dirtyDishPile.setLocation(event.getWhoClicked().getLocation());
        event.getWhoClicked().sendMessage(Component.text("汚い皿置場を" + locationBlockPostoString(event.getWhoClicked().getLocation()) + "で登録しました"));
        UIAdminMenu.openUI((Player) event.getWhoClicked());
    }
}
