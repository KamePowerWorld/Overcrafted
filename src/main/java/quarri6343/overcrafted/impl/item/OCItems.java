package quarri6343.overcrafted.impl.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import quarri6343.overcrafted.api.item.*;
import quarri6343.overcrafted.api.item.interfaces.IProcessedOCItem;
import quarri6343.overcrafted.common.data.OCResourcePackData;

public enum OCItems {
    WOOD(new SuppliableOCItem(Component.text("原木"), Material.OAK_WOOD, "wood", 0)),
    COBBLESTONE(new SuppliableOCItem(Component.text("丸石"), Material.COBBLESTONE, "cobblestone", 0)),
    IRON_ORE(new SuppliableOCItem(Component.text("鉄鉱石"), Material.IRON_ORE, "iron_ore", 0)),

    FURNACE(new ProcessedOCItem(Component.text("かまど"), Material.FURNACE, "furnace", 0, IProcessedOCItem.ProcessType.CRAFTING, COBBLESTONE)),
    IRON_INGOT(new ProcessedOCItem(Component.text("鉄インゴット"), Material.IRON_INGOT, "iron_ingot", 0, IProcessedOCItem.ProcessType.SMELTING, IRON_ORE)),
    MINECART(new ProcessedOCItem(Component.text("トロッコ"), Material.MINECART, "minecart", 0, IProcessedOCItem.ProcessType.CRAFTING, IRON_INGOT)),
    PLANK(new ProcessedOCItem(Component.text("木材"), Material.OAK_PLANKS, "plank", 0, IProcessedOCItem.ProcessType.CRAFTING, WOOD)),
    STICK(new ProcessedOCItem(Component.text("棒"), Material.STICK, "stick", 0, IProcessedOCItem.ProcessType.CRAFTING, PLANK)),
    CHARCOAL(new ProcessedOCItem(Component.text("木炭"), Material.CHARCOAL, "charcoal", 0, IProcessedOCItem.ProcessType.SMELTING, WOOD)),
    TORCH(new CombinedOCItem(Component.text("松明"), Material.TORCH, "torch", 0, STICK, CHARCOAL)),
    DISH(new InteractableOCItem(Component.text("皿"), Material.PAPER, "dish", 1)),
    DIRTY_DISH(new WashableOCItem(Component.text("汚い皿"), Material.PAPER, "dish", 2)),

    DISH_STACKED_1(new StackedDish(Component.text("積まれた皿1"), "dish_stacked_1", 1, 1, StackedDish.StackedDishType.CLEAN)),
    DISH_STACKED_2(new StackedDish(Component.text("積まれた皿2"), "dish_stacked_2", 2, 2, StackedDish.StackedDishType.CLEAN)),
    DISH_STACKED_3(new StackedDish(Component.text("積まれた皿3"), "dish_stacked_3", 3, 3, StackedDish.StackedDishType.CLEAN)),
    DISH_STACKED_4(new StackedDish(Component.text("積まれた皿4"), "dish_stacked_4", 4, 4, StackedDish.StackedDishType.CLEAN)),
    DISH_STACKED_5(new StackedDish(Component.text("積まれた皿5"), "dish_stacked_5", 5, 5, StackedDish.StackedDishType.CLEAN)),
    DISH_STACKED_DIRTY_1(new StackedDish(Component.text("積まれた汚い皿1"), "dish_stacked_dirty_1", 6, 1, StackedDish.StackedDishType.DIRTY)),
    DISH_STACKED_DIRTY_2(new StackedDish(Component.text("積まれた汚い皿2"), "dish_stacked_dirty_2", 7, 2, StackedDish.StackedDishType.DIRTY)),
    DISH_STACKED_DIRTY_3(new StackedDish(Component.text("積まれた汚い皿3"), "dish_stacked_dirty_3", 8, 3, StackedDish.StackedDishType.DIRTY)),
    DISH_STACKED_DIRTY_4(new StackedDish(Component.text("積まれた汚い皿4"), "dish_stacked_dirty_4", 9, 4, StackedDish.StackedDishType.DIRTY)),
    DISH_STACKED_DIRTY_5(new StackedDish(Component.text("積まれた汚い皿5"), "dish_stacked_dirty_5", 10, 5, StackedDish.StackedDishType.DIRTY)),
    
    DISH_FURNACE(new SubmittableOCItem(Component.text("かまどの載った皿"), Material.PAPER, "dish_furnace", 3, DISH, FURNACE, 5, OCResourcePackData.MenuFont.FURNACE.get_char())),
    DISH_IRON_INGOT(new SubmittableOCItem(Component.text("鉄インゴットの載った皿"), Material.PAPER, "dish_iron_ingot", 4, DISH, IRON_INGOT, 3, OCResourcePackData.MenuFont.IRON.get_char())),
    DISH_MINECART(new SubmittableOCItem(Component.text("トロッコの載った皿"), Material.PAPER, "dish_minecart", 5, DISH, IRON_INGOT, 15,OCResourcePackData.MenuFont.MINECART.get_char())),
    DISH_TORCH(new SubmittableOCItem(Component.text("松明の載った皿"), Material.PAPER, "dish_torch", 6, DISH, TORCH, 5, OCResourcePackData.MenuFont.TORCH.get_char()));
    
    private final OCItem ocItem;

    OCItems(OCItem ocItem) {
        this.ocItem = ocItem;
        ItemManager.registerItem(ocItem);
    }

    public OCItem get() {
        return ocItem;
    }
}
