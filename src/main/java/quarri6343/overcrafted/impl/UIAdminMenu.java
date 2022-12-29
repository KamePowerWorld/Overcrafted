package quarri6343.overcrafted.impl;

import dev.triumphteam.gui.guis.Gui;
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
import quarri6343.overcrafted.common.data.OrderBox;
import quarri6343.overcrafted.utils.ItemCreator;

import static quarri6343.overcrafted.utils.UIUtility.getLocDesc;
import static quarri6343.overcrafted.utils.UIUtility.locationBlockPostoString;

/**
 * プラグインの管理パネル
 */
public class UIAdminMenu {

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    public static void openUI(Player player) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text("管理メニュー").color(NamedTextColor.GRAY))
                .rows(3)
                .pageSize(27)
                .disableAllInteractions()
                .create();

        ItemStack placeChestItem = new ItemCreator(Material.CHEST).setName(Component.text("注文箱の座標を設定する")
                .color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
                .setLore(getorderBoxLocationStats()).create();
        GuiItem placeBeaconButton = new GuiItem(placeChestItem,
                UIAdminMenu::setUpOrderBox);
        gui.setItem(4, placeBeaconButton);
        
        GuiItem closeButton = new GuiItem(new ItemCreator(Material.BARRIER).setName(Component.text("閉じる")).create(),
                event -> gui.close(event.getWhoClicked()));
        gui.setItem(22, closeButton);

        gui.open(player);
    }

    /**
     * 現在立っている場所で注文箱を登録する
     * @param event
     */
    public static void setUpOrderBox(InventoryClickEvent event){
        OrderBox orderBox = getData().orderBox;
        orderBox.location = event.getWhoClicked().getLocation();
        orderBox.place();
        event.getWhoClicked().sendMessage(Component.text("注文箱を" + locationBlockPostoString(event.getWhoClicked().getLocation()) + "で登録しました"));
    }

    private static TextComponent getorderBoxLocationStats() {
        return getLocDesc(getData().orderBox.location);
    }
}
