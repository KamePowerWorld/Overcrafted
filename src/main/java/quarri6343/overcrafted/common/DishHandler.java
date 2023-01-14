package quarri6343.overcrafted.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.utils.ItemCreator;
import quarri6343.overcrafted.utils.OvercraftedUtils;

import java.util.Random;

public class DishHandler {

    private static final String dishNBTID = "isDish";
    private static final String menuNBTID = "menuID";
    private static final String completeNBTID = "isCompleted";
    private static final String dirtyNBTID = "isDirty";
    private static final Component dishItemName = Component.text("皿").decoration(TextDecoration.ITALIC, false);
    private static final Component dirtyDishItemName = Component.text("汚い皿").color(NamedTextColor.DARK_PURPLE).decoration(TextDecoration.ITALIC, false);

    /**
     * アイテムが皿であるかどうか
     *
     * @param dishCandidate
     * @return 皿であるかどうか
     */
    public static boolean isDish(ItemStack dishCandidate) {
        Boolean isDish = new ItemCreator(dishCandidate).getBooleanNBT(dishNBTID);
        if (isDish == null)
            return false;

        return isDish;
    }

    /**
     * ランダムな注文を皿にエンコードする
     *
     * @return
     */
    public static ItemStack encodeRandomOrder() {
        DishMenu[] menus = DishMenu.values();
        return encodeOrder(menus[new Random().nextInt(menus.length)]);
    }

    /**
     * 皿で納品させたい注文を皿(paper)上にエンコードする
     *
     * @param productStack 注文票に載せるアイテムリスト
     * @return 皿のアイテム
     */
    public static ItemStack encodeOrder(DishMenu productStack) {
        return new ItemCreator(Material.PAPER).setName(dishItemName)
                .setLore(OvercraftedUtils.getItemInfoasText(productStack.getProduct()))
                .setBooleanNBT(dishNBTID, true)
                .setIntNBT(menuNBTID, productStack.ordinal()).create();
    }

    /**
     * 皿に注文とそれが満たされた状態であることを記録する
     *
     * @param productStack 注文票に載せるアイテムリスト
     * @return 皿のアイテム
     */
    public static ItemStack encodeOrderAsCompleted(DishMenu productStack) {
        return new ItemCreator(Material.PAPER).setName(dishItemName)
                .setLore(Component.text("注文された品が載っている").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
                .setBooleanNBT(dishNBTID, true)
                .setIntNBT(menuNBTID, productStack.ordinal())
                .setBooleanNBT(completeNBTID, true).create();
    }

    /**
     * 皿に書かれた注文票の内容を抽出する
     *
     * @param dishCandidate 皿のアイテム
     * @return 注文の種類
     */
    public static DishMenu decodeOrder(ItemStack dishCandidate) {
        if (!isDish(dishCandidate)) {
            throw new IllegalArgumentException();
        }

        Integer index = new ItemCreator(dishCandidate).getIntNBT(menuNBTID);
        if (index == null)
            return null;

        DishMenu[] menus = DishMenu.values();
        return menus[index];
    }

    /**
     * 皿の注文が満たされたかどうか
     *
     * @param dishCandidate 皿のアイテム
     * @return 皿の注文が満たされたかどうか
     */
    public static boolean isOrderCompleted(ItemStack dishCandidate) {
        if (!isDish(dishCandidate)) {
            throw new IllegalArgumentException();
        }

        Boolean isCompleted = new ItemCreator(dishCandidate).getBooleanNBT(completeNBTID);
        if (isCompleted == null)
            return false;

        return isCompleted;
    }

    /**
     * ランダムな注文を汚い皿にエンコードする
     *
     * @return
     */
    public static ItemStack encodeRandomOrderOnDirtyDish() {
        DishMenu[] menus = DishMenu.values();
        return encodeOrderOnDirtyDish(menus[new Random().nextInt(menus.length)]);
    }

    /**
     * 皿で納品させたい注文を汚い皿上にエンコードする
     *
     * @param productStack 注文票に載せるアイテムリスト
     * @return 皿のアイテム
     */
    public static ItemStack encodeOrderOnDirtyDish(DishMenu productStack) {
        return new ItemCreator(Material.PAPER).setName(dirtyDishItemName)
                .setLore(Component.text("汚すぎて何が書かれているか読み取れない!").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
                .setBooleanNBT(dishNBTID, true)
                .setIntNBT(menuNBTID, productStack.ordinal())
                .setBooleanNBT(dirtyNBTID, true).create();
    }

    /**
     * 皿が汚いかどうか
     *
     * @param dishCandidate 皿のアイテム
     * @return 汚いかどうか
     */
    public static boolean isDirty(ItemStack dishCandidate) {
        if (!isDish(dishCandidate)) {
            throw new IllegalArgumentException();
        }

        Boolean isDirty = new ItemCreator(dishCandidate).getBooleanNBT(dirtyNBTID);
        if (isDirty == null)
            return false;

        return isDirty;
    }
}
