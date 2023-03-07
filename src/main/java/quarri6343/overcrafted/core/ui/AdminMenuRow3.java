package quarri6343.overcrafted.core.ui;

import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.OCLogic;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.impl.item.OCItems;
import quarri6343.overcrafted.utils.ItemCreator;
import quarri6343.overcrafted.utils.UIUtility;

/**
 * 管理メニューの3行目
 */
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
        ItemStack setCraftingTimeItem = new ItemCreator(Material.CRAFTING_TABLE).setName(Component.text("手動加工にかかる時間"))
                .setLore(Component.text("現在: " + getData().getCraftingTime().get() + "ticks").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)).create();
        GuiItem setCraftingTimeButton = new GuiItem(setCraftingTimeItem,
                AdminMenuRow3::onSetCraftingTimeButton);
        gui.setItem(18, setCraftingTimeButton);
        ItemStack setCookingTimeItem = new ItemCreator(Material.FURNACE).setName(Component.text("自動加工にかかる時間"))
                .setLore(Component.text("現在: " + getData().getCookingTime().get() + "ticks").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)).create();
        GuiItem setCookingTimeButton = new GuiItem(setCookingTimeItem,
                AdminMenuRow3::onSetCookingTimeButton);
        gui.setItem(20, setCookingTimeButton);
        ItemStack setBurnTimeItem = new ItemCreator(OCItems.BURNT_IRON_INGOT.get().getItemStack()).setName(Component.text("加工されたアイテムが焦げるまでの時間"))
                .setLore(Component.text("現在: " + getData().getBurnTime().get() + "ticks").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)).create();
        GuiItem setBurnTimeButton = new GuiItem(setBurnTimeItem,
                AdminMenuRow3::onSetBurnTimeButton);
        gui.setItem(22, setBurnTimeButton);
        ItemStack setTipMultiplierItem = new ItemCreator(OCItems.GOLD_INGOT.get().getItemStack()).setName(Component.text("順序通りに納品した時のチップの倍率"))
                .setLore(Component.text("現在: +" + getData().getTipMultiplier().get() + "倍").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)).create();
        GuiItem setTipMultiplierButton = new GuiItem(setTipMultiplierItem,
                AdminMenuRow3::onSetTipMultiplierButton);
        gui.setItem(24, setTipMultiplierButton);
    }

    private static void onSetCraftingTimeButton(InventoryClickEvent event) {
        if(getLogic().gameStatus != OCLogic.GameStatus.INACTIVE){
            event.getWhoClicked().sendMessage(UIUtility.gameRunningText);
            return;
        }
        
        UINumberConfiguration.openUI((Player) event.getWhoClicked(),getData().getCraftingTime());
    }

    private static void onSetCookingTimeButton(InventoryClickEvent event) {
        if(getLogic().gameStatus != OCLogic.GameStatus.INACTIVE){
            event.getWhoClicked().sendMessage(UIUtility.gameRunningText);
            return;
        }

        UINumberConfiguration.openUI((Player) event.getWhoClicked(),getData().getCookingTime());
    }

    private static void onSetBurnTimeButton(InventoryClickEvent event) {
        if(getLogic().gameStatus != OCLogic.GameStatus.INACTIVE){
            event.getWhoClicked().sendMessage(UIUtility.gameRunningText);
            return;
        }

        UINumberConfiguration.openUI((Player) event.getWhoClicked(),getData().getBurnTime());
    }

    private static void onSetTipMultiplierButton(InventoryClickEvent event) {
        if(getLogic().gameStatus != OCLogic.GameStatus.INACTIVE){
            event.getWhoClicked().sendMessage(UIUtility.gameRunningText);
            return;
        }

        UINumberConfiguration.openUI((Player) event.getWhoClicked(),getData().getTipMultiplier());
    }
}
