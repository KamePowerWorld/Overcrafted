package quarri6343.overcrafted.impl.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.api.item.IOCItem;
import quarri6343.overcrafted.api.item.IProcessedOCItem;
import quarri6343.overcrafted.core.OCItem;
import quarri6343.overcrafted.core.data.constant.OCResourcePackData;
import quarri6343.overcrafted.api.block.IBlockProcessor;

import javax.annotation.Nullable;
import java.util.Arrays;

import static quarri6343.overcrafted.impl.block.OCBlocks.*;

/**
 * アイテムレジストリ
 */
public enum OCItems {
    WOOD(new SuppliableOCItem(Component.text("原木"), Material.OAK_LOG, "wood", 0)),
    COBBLESTONE(new SuppliableOCItem(Component.text("丸石"), Material.COBBLESTONE, "cobblestone", 0)),
    IRON_ORE(new SuppliableOCItem(Component.text("鉄鉱石"), Material.IRON_ORE, "iron_ore", 0)),
    GOLD_ORE(new SuppliableOCItem(Component.text("金鉱石"), Material.GOLD_ORE, "gold_ore", 0)),
    LEAVES(new SuppliableOCItem(Component.text("葉"), Material.OAK_LEAVES, "leaves", 0)),
    SOUL_SAND(new SuppliableOCItem(Component.text("ソウルサンド"), Material.SOUL_SAND, "soul_sand", 0)),
    WITHER_SKULL(new SuppliableOCItem(Component.text("ウィザースケルトンの頭"), Material.WITHER_SKELETON_SKULL, "wither_skull", 0)),

    FURNACE(new ProcessedOCItem(Component.text("かまど"), Material.FURNACE, "furnace", 0, (IBlockProcessor)CRAFTING.get(), COBBLESTONE)),
    IRON_INGOT(new ProcessedOCItem(Component.text("鉄インゴット"), Material.IRON_INGOT, "iron_ingot", 0, (IBlockProcessor)SMELTING.get(), IRON_ORE)),
    MINECART(new ProcessedOCItem(Component.text("トロッコ"), Material.MINECART, "minecart", 0, (IBlockProcessor)CRAFTING.get(), IRON_INGOT)),
    PLANK(new ProcessedOCItem(Component.text("木材"), Material.OAK_PLANKS, "plank", 0, (IBlockProcessor)CRAFTING.get(), WOOD)),
    STICK(new ProcessedOCItem(Component.text("棒"), Material.STICK, "stick", 0, (IBlockProcessor)CRAFTING.get(), PLANK)),
    CHARCOAL(new ProcessedOCItem(Component.text("木炭"), Material.CHARCOAL, "charcoal", 0, (IBlockProcessor)SMELTING.get(), WOOD)),
    TORCH(new CombinedOCItem(Component.text("松明"), Material.TORCH, "torch", 0, STICK, CHARCOAL)),
    DIRTY_DISH(new OCItem(Component.text("汚い皿"), Material.PAPER, "dirty_dish", 2)),
    DISH(new ProcessedOCItem(Component.text("皿"), Material.PAPER, "dish", 1, (IBlockProcessor)WASHING.get(), DIRTY_DISH)),
    BURNT_CHARCOAL(new BurntOCItem(Component.text("焦げた木炭"), Material.PAPER, "burnt_charcoal", 7, (IProcessedOCItem) CHARCOAL.get())),
    BURNT_IRON_INGOT(new BurntOCItem(Component.text("焦げた鉄"), Material.PAPER, "burnt_iron_ingot", 8, (IProcessedOCItem) IRON_INGOT.get())),
    IRON_AND_STICK(new CombinedOCItem(Component.text("鉄と棒"), Material.PAPER, "iron_and_stick", 16, STICK, IRON_INGOT)),
    IRON_SWORD(new ProcessedOCItem(Component.text("鉄の剣"), Material.IRON_SWORD, "iron_sword", 0, (IBlockProcessor)FORGING.get(), IRON_AND_STICK)),
    GOLD_INGOT(new ProcessedOCItem(Component.text("金インゴット"), Material.GOLD_INGOT, "gold_ingot", 0, (IBlockProcessor)SMELTING.get(), GOLD_ORE)),
    BURNT_GOLD_INGOT(new BurntOCItem(Component.text("焦げた金"), Material.PAPER, "burnt_gold_ingot", 10, (IProcessedOCItem) GOLD_INGOT.get())),
    APPLE(new ProcessedOCItem(Component.text("りんご"), Material.APPLE, "apple", 0, (IBlockProcessor)CRAFTING.get(), LEAVES)),
    APPLE_AND_GOLD(new CombinedOCItem(Component.text("りんごと金"), Material.PAPER, "apple_and_gold", 9, APPLE, GOLD_INGOT)),
    GOLDEN_APPLE(new ProcessedOCItem(Component.text("金のりんご"), Material.GOLDEN_APPLE, "golden_apple", 0, (IBlockProcessor)ENCHANTING.get(), APPLE_AND_GOLD)),
    BURNT_GOLDEN_APPLE(new BurntOCItem(Component.text("溶けた金りんご"), Material.PAPER, "burnt_golden_apple", 17, (IProcessedOCItem) GOLDEN_APPLE.get())),
    SOUL_SAND_BASE(new ProcessedOCItem(Component.text("ソウルサンドの台"), Material.PAPER, "soul_sand_base", 18, (IBlockProcessor)CRAFTING.get(), SOUL_SAND)),
    WITHER(new CombinedOCItem(Component.text("ウィザー"), Material.PAPER, "wither", 19, SOUL_SAND_BASE, WITHER_SKULL)),
    NETHER_STAR(new ProcessedOCItem(Component.text("ネザースター"), Material.NETHER_STAR, "nether_star", 0, (IBlockProcessor)ENCHANTING.get(), WITHER)),
    BURNT_NETHER_STAR(new BurntOCItem(Component.text("溶けたネザースター"), Material.PAPER, "burnt_nether_star", 11, (IProcessedOCItem) NETHER_STAR.get())),
    
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
    DISH_MINECART(new SubmittableOCItem(Component.text("トロッコの載った皿"), Material.PAPER, "dish_minecart", 5, DISH, MINECART, 15, OCResourcePackData.MenuFont.MINECART.get_char())),
    DISH_TORCH(new SubmittableOCItem(Component.text("松明の載った皿"), Material.PAPER, "dish_torch", 6, DISH, TORCH, 5, OCResourcePackData.MenuFont.TORCH.get_char())),
    DISH_IRON_SWORD(new SubmittableOCItem(Component.text("鉄の剣の載った皿"), Material.PAPER, "dish_iron_sword", 14, DISH, IRON_SWORD, 5, OCResourcePackData.MenuFont.IRON_SWORD.get_char())),
    DISH_GOLDEN_APPLE(new SubmittableOCItem(Component.text("金りんごの載った皿"), Material.PAPER, "dish_golden_apple", 13, DISH, GOLDEN_APPLE, 5, OCResourcePackData.MenuFont.GOLDEN_APPLE.get_char())),
    DISH_APPLE(new SubmittableOCItem(Component.text("りんごの載った皿"), Material.PAPER, "dish_apple", 12, DISH, APPLE, 5, OCResourcePackData.MenuFont.APPLE.get_char())),
    DISH_NETHER_STAR(new SubmittableOCItem(Component.text("ネザースターの載った皿"), Material.PAPER, "dish_nether_star", 15, DISH, NETHER_STAR, 5, OCResourcePackData.MenuFont.NETHER_STAR.get_char())),
    
    ADMIN_MENU(new AdminMenu());

    private final IOCItem ocItem;

    OCItems(IOCItem ocItem) {
        this.ocItem = ocItem;
    }

    public IOCItem get() {
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
