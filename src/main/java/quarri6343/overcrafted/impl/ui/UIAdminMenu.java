package quarri6343.overcrafted.impl.ui;

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

        AdminMenuRow1.addElements(gui, player);
        AdminMenuRow2.addElements(gui, player);
        AdminMenuRow3.addElements(gui, player);

        gui.open(player);
    }
}
