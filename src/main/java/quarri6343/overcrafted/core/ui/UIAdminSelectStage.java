package quarri6343.overcrafted.core.ui;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.item.ISubmittableOCItem;
import quarri6343.overcrafted.api.item.ISupplier;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.impl.OCStages;
import quarri6343.overcrafted.utils.ItemCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * プレイしたいステージを選択する用のGUI
 */
public class UIAdminSelectStage {

    public static void openUI(Player player) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text("ステージセレクタ").color(NamedTextColor.GRAY))
                .rows(3)
                .pageSize(27)
                .disableAllInteractions()
                .create();
        
        OCVariableData data = Overcrafted.getInstance().getData();
        
        for (int i = 0; i < OCStages.values().length; i++) {
            int finalI = i;
            List<Component> lores = new ArrayList<>();
            lores.add(Component.text("制限時間: ")
                    .color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
                    .append(Component.text(OCStages.values()[i].get().getTime() + "秒").color(NamedTextColor.WHITE)));

            lores.add(Component.text("納品対象: ")
                    .color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
            for (ISubmittableOCItem product : OCStages.values()[i].get().getProducts()) {
                lores.add(product.getName()
                        .color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
            }

            lores.add(Component.text("材料: ")
                    .color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
            for (ISupplier ingredient : OCStages.values()[i].get().getMaterials()) {
                lores.add(ingredient.getName()
                        .color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
            }
            
            lores.add(Component.text("皿洗い: ")
                    .color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
                    .append(Component.text(OCStages.values()[i].get().isEnableDishGettingDirty() ? "あり" : "なし").color(NamedTextColor.WHITE)));
            
            ItemStack stageSelectItem = new ItemCreator(Material.WHITE_WOOL)
                    .setName(Component.text(OCStages.values()[i].get().getName()).color(NamedTextColor.YELLOW).append(Component.text(" を選択").color(NamedTextColor.WHITE)))
                    .setLores(lores).create();
            GuiItem teamSelectButton = new GuiItem(stageSelectItem,
                    event -> {
                        data.setSelectedStage(OCStages.values()[finalI]);
                        event.getWhoClicked().sendMessage(Component.text(OCStages.values()[finalI].get().getName()).color(NamedTextColor.YELLOW).append(Component.text(" を選択しました").color(NamedTextColor.WHITE)));
                        UIAdminMenu.openUI((Player) event.getWhoClicked());
                    });
            gui.setItem(i, teamSelectButton);
        }

        gui.open(player);
    }
}
