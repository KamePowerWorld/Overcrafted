package quarri6343.overcrafted.impl.ui;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.item.interfaces.ISupplier;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.impl.item.OCItems;

/**
 * プレイヤーにアイテムを供給するGUI
 */
public class UISupplyMenu {

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    public static void openUI(Player player) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text("補給所").color(NamedTextColor.GRAY))
                .rows(3)
                .pageSize(27)
                .disableAllInteractions()
                .create();
        
        OCItems[] ocItems = OCItems.values();
        for (int i = 0; i < OCItems.values().length; i++) {
            if(!(ocItems[i].get() instanceof ISupplier))
                continue;

            int finalI = i;
            GuiItem supplyButton = new GuiItem((ocItems[i].get().getItemStack()),
                    event -> ((ISupplier)(ocItems[finalI].get())).onSupply((Player) event.getWhoClicked()));
            gui.setItem(i, supplyButton);
        }

        gui.open(player);
    }
}
