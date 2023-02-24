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
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.api.object.IDishPile;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.core.OCLogic;
import quarri6343.overcrafted.impl.item.OCItems;
import quarri6343.overcrafted.utils.ItemCreator;

import static quarri6343.overcrafted.utils.UIUtility.*;

public class AdminMenuRow3 {

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }
    
    private static int getStageID(){
        return getData().getSelectedStage().ordinal();
    }

    public static void addElements(PaginatedGui gui, Player player) {
        ItemStack placeCleanDishPileItem = new ItemCreator(OCItems.DISH.get().getItemStack()).setName(
                Component.text(ChatColor.YELLOW + (getData().getSelectedStage() != null ? getData().getSelectedStage().get().getName() : "ステージ?")
                                + ChatColor.RESET + "でのチーム" + getData().getAdminSelectedTeam() + "の綺麗な皿置場の座標を設定")
                        .color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
                .addLore(getCleanDishPileLocationStats()).create();
        GuiItem placeCleanDishPileButton = new GuiItem(placeCleanDishPileItem,
                AdminMenuRow3::setUpCleanDishPile);
        gui.setItem(18, placeCleanDishPileButton);

        ItemStack placeDirtyDishPileItem = new ItemCreator(OCItems.DIRTY_DISH.get().getItemStack()).setName(
                        Component.text(ChatColor.YELLOW + (getData().getSelectedStage() != null ? getData().getSelectedStage().get().getName() : "ステージ?")
                                        + ChatColor.RESET + "でのチーム" + getData().getAdminSelectedTeam() + "の汚い皿置場の座標を設定")
                        .color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
                .addLore(getDirtyDishPileLocationStats()).create();
        GuiItem placeDirtyDishPileButton = new GuiItem(placeDirtyDishPileItem,
                AdminMenuRow3::setUpDirtyDishPile);
        gui.setItem(20, placeDirtyDishPileButton);

        GuiItem closeButton = new GuiItem(new ItemCreator(Material.BARRIER).setName(Component.text("閉じる")).create(),
                event -> gui.close(event.getWhoClicked()));
        gui.setItem(22, closeButton);

        GuiItem startButton = new GuiItem(new ItemCreator(Material.GREEN_WOOL)
                .setName(Component.text(getData().getSelectedStage() != null ? getData().getSelectedStage().get().getName() : "ゲーム").color(NamedTextColor.YELLOW)
                        .append(Component.text("を開始").color(NamedTextColor.WHITE)))
                .setLore(getCanStartGameDesc()).create(),
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
        
        if(getData().getSelectedStage() == null){
            return stageNotSelectedText;
        }

        return getLocDesc(team.getCleanDishPiles().get(getStageID()).getLocation());
    }


    /**
     * @return 汚い皿置き場の位置を設定するボタンに表示する現在の状況
     */
    private static TextComponent getDirtyDishPileLocationStats() {
        IOCTeam team = getData().getTeams().getTeamByName(getData().getAdminSelectedTeam());
        if (team == null) {
            return teamNotSelectedText;
        }

        if(getData().getSelectedStage() == null){
            return stageNotSelectedText;
        }

        return getLocDesc(team.getDirtyDishPiles().get(getStageID()).getLocation());
    }

    /**
     * 現在立っている場所で綺麗な皿置場を登録する
     *
     * @param event
     */
    public static void setUpCleanDishPile(InventoryClickEvent event) {
        Location playerLoc = event.getWhoClicked().getLocation();
        IOCTeam playerTeam = getData().getTeams().getTeamByName(getData().getAdminSelectedTeam());
        if (playerTeam == null) {
            event.getWhoClicked().sendMessage(teamNotSelectedText);
            return;
        }
        
        if(getData().getSelectedStage() == null){
            event.getWhoClicked().sendMessage(stageNotSelectedText);
            return;
        }

        if (getLogic().gameStatus != OCLogic.GameStatus.INACTIVE) {
            event.getWhoClicked().sendMessage(gameRunningText);
            return;
        }

        for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
            IOCTeam team = getData().getTeams().getTeam(i);
            
            if(team != playerTeam && team.getCleanDishPiles().get(getStageID()).getLocation() != null
                    && team.getCleanDishPiles().get(getStageID()).getLocation().getBlock().equals(playerLoc.getBlock())){
                event.getWhoClicked().sendMessage(Component.text("チーム" + team + "の綺麗な皿置場と場所が被っています"));
                return;
            }
            if(team.getDirtyDishPiles().get(getStageID()).getLocation() != null
                    && team.getDirtyDishPiles().get(getStageID()).getLocation().getBlock().equals(playerLoc.getBlock())){
                event.getWhoClicked().sendMessage(Component.text("チーム" + team + "の汚い皿置場と場所が被っています"));
                return;
            }
        }
        
        IDishPile cleanDishPile = playerTeam.getCleanDishPiles().get(getStageID());
        cleanDishPile.setLocation(playerLoc);
        event.getWhoClicked().sendMessage(Component.text("綺麗な皿置場を" + locationBlockPostoString(playerLoc) + "で登録しました"));
        UIAdminMenu.openUI((Player) event.getWhoClicked());
    }

    /**
     * 現在立っている場所で汚い皿置場を登録する
     *
     * @param event
     */
    public static void setUpDirtyDishPile(InventoryClickEvent event) {
        Location playerLoc = event.getWhoClicked().getLocation();
        IOCTeam playerTeam = getData().getTeams().getTeamByName(getData().getAdminSelectedTeam());
        if (playerTeam == null) {
            event.getWhoClicked().sendMessage(teamNotSelectedText);
            return;
        }

        if(getData().getSelectedStage() == null){
            event.getWhoClicked().sendMessage(stageNotSelectedText);
            return;
        }

        if (getLogic().gameStatus != OCLogic.GameStatus.INACTIVE) {
            event.getWhoClicked().sendMessage(gameRunningText);
            return;
        }

        for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
            IOCTeam team = getData().getTeams().getTeam(i);

            if(team.getCleanDishPiles().get(getStageID()).getLocation() != null
                    && team.getCleanDishPiles().get(getStageID()).getLocation().getBlock().equals(playerLoc.getBlock())){
                event.getWhoClicked().sendMessage(Component.text("チーム" + team.getName() + "の綺麗な皿置場と場所が被っています"));
                return;
            }
            if(team != playerTeam && team.getDirtyDishPiles().get(getStageID()).getLocation() != null
                    && team.getDirtyDishPiles().get(getStageID()).getLocation().getBlock().equals(playerLoc.getBlock())){
                event.getWhoClicked().sendMessage(Component.text("チーム" + team.getName() + "の汚い皿置場と場所が被っています"));
                return;
            }
        }

        IDishPile dirtyDishPile = playerTeam.getDirtyDishPiles().get(getStageID());
        dirtyDishPile.setLocation(playerLoc);
        event.getWhoClicked().sendMessage(Component.text("汚い皿置場を" + locationBlockPostoString(playerLoc) + "で登録しました"));
        UIAdminMenu.openUI((Player) event.getWhoClicked());
    }
}
