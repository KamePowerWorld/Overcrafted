package quarri6343.overcrafted.impl;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.common.DishHandler;
import quarri6343.overcrafted.utils.ItemCreator;

/**
 * プラグインの管理パネル
 */
public class UIAdminMenu {

    public static void openUI(Player player) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text("管理メニュー").color(NamedTextColor.GRAY))
                .rows(3)
                .pageSize(27)
                .disableAllInteractions()
                .create();

        ItemStack placeChestItem = new ItemCreator(Material.CHEST).setName(Component.text("現在立っている場所に皿が入ったチェストを設置する")
                .color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)).create();
        GuiItem placeBeaconButton = new GuiItem(placeChestItem,
                UIAdminMenu::placeDishChest);
        gui.setItem(4, placeBeaconButton);
        
        GuiItem closeButton = new GuiItem(new ItemCreator(Material.BARRIER).setName(Component.text("閉じる")).create(),
                event -> gui.close(event.getWhoClicked()));
        gui.setItem(22, closeButton);

        gui.open(player);
    }

    /**
     * 皿が適当に入ったチェストを設置する
     */
    public static void placeDishChest(InventoryClickEvent event){
        event.getWhoClicked().getWorld().setType(event.getWhoClicked().getLocation(), Material.CHEST);
        Chest chest = (Chest) event.getWhoClicked().getWorld().getBlockAt(event.getWhoClicked().getLocation()).getState();
        chest.setCustomName("注文箱");
        
        for (int i = 0; i < 9; i++) {
            chest.getSnapshotInventory().setItem(i, DishHandler.encodeRandom()); //placeholder
        }
        chest.update(true, true);
    }
}
