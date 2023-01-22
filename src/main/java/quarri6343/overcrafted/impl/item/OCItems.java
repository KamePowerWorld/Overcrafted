package quarri6343.overcrafted.impl.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.api.item.*;
import quarri6343.overcrafted.api.item.interfaces.IOCItem;
import quarri6343.overcrafted.common.data.OCResourcePackData;
import quarri6343.overcrafted.impl.block.BlockProcessor;

import javax.annotation.Nullable;
import java.util.Arrays;

import static quarri6343.overcrafted.impl.block.OCBlocks.*;

public enum OCItems {
    WOOD(new SuppliableOCItem(Component.text("原木"), Material.OAK_WOOD, "wood", 0)),
    COBBLESTONE(new SuppliableOCItem(Component.text("丸石"), Material.COBBLESTONE, "cobblestone", 0)),
    IRON_ORE(new SuppliableOCItem(Component.text("鉄鉱石"), Material.IRON_ORE, "iron_ore", 0)),

    FURNACE(new ProcessedOCItem(Component.text("かまど"), Material.FURNACE, "furnace", 0, (BlockProcessor)CRAFTING.get(), COBBLESTONE)),
    IRON_INGOT(new ProcessedOCItem(Component.text("鉄インゴット"), Material.IRON_INGOT, "iron_ingot", 0, (BlockProcessor)SMELTING.get(), IRON_ORE)),
    MINECART(new ProcessedOCItem(Component.text("トロッコ"), Material.MINECART, "minecart", 0, (BlockProcessor)CRAFTING.get(), IRON_INGOT)),
    PLANK(new ProcessedOCItem(Component.text("木材"), Material.OAK_PLANKS, "plank", 0, (BlockProcessor)CRAFTING.get(), WOOD)),
    STICK(new ProcessedOCItem(Component.text("棒"), Material.STICK, "stick", 0, (BlockProcessor)CRAFTING.get(), PLANK)),
    CHARCOAL(new ProcessedOCItem(Component.text("木炭"), Material.CHARCOAL, "charcoal", 0, (BlockProcessor)SMELTING.get(), WOOD)),
    TORCH(new CombinedOCItem(Component.text("松明"), Material.TORCH, "torch", 0, STICK, CHARCOAL)),
    DIRTY_DISH(new OCItem(Component.text("汚い皿"), Material.PAPER, "dirty_dish", 2)),
    DISH(new ProcessedOCItem(Component.text("皿"), Material.PAPER, "dish", 1, (BlockProcessor)WASHING.get(), DIRTY_DISH)),

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
    DISH_MINECART(new SubmittableOCItem(Component.text("トロッコの載った皿"), Material.PAPER, "dish_minecart", 5, DISH, IRON_INGOT, 15, OCResourcePackData.MenuFont.MINECART.get_char())),
    DISH_TORCH(new SubmittableOCItem(Component.text("松明の載った皿"), Material.PAPER, "dish_torch", 6, DISH, TORCH, 5, OCResourcePackData.MenuFont.TORCH.get_char())),

    ADMIN_MENU(new AdminMenu());

    private final OCItem ocItem;

    OCItems(OCItem ocItem) {
        this.ocItem = ocItem;
    }

    public OCItem get() {
        return ocItem;
    }

    /**
     * アイテムが固有アイテムであった場合その実体を固有アイテムクラスに変換する<br>
     * この際実体特有のNBTなどは失われる
     * @param itemStack 変換したいアイテム
     * @return 変換された固有アイテム
     */
    @Nullable
    public static IOCItem toOCItem(ItemStack itemStack){
        OCItems iocItem = Arrays.stream(OCItems.values()).filter(e-> e.get().isSimilar(itemStack)).findFirst().orElse(null);
        return iocItem == null ? null : iocItem.get();
    }

    /**
     * 固有アイテムの召喚コマンドを固有アイテムに変換する
     * @param command 召喚コマンド
     * @return 変換された固有アイテム
     */
    @Nullable
    public static IOCItem commandToOCItem(String command){
        OCItems iocItem = Arrays.stream(OCItems.values()).filter(e -> e.get().getInternalName().equals(command)).findFirst().orElse(null);
        return iocItem == null ? null : iocItem.get();
    }
}
