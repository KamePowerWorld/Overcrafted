package quarri6343.overcrafted.common.order;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.DishMenu;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCResourcePackData.CustomDishModel;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.utils.ItemCreator;

import java.util.*;

/**
 * 注文のハンドラ
 */
public class OrderHandler {

    private static final int orderNumber = 5;

    private static Map<OCTeam, List<DishMenu>> ordersMap = new HashMap<>();

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }

    public static void generateRandomOrders() {
        BossBarHandler.hideEverything();

        for (int i = 0; i < getData().teams.getTeamsLength(); i++) {
            List<DishMenu> orders = new ArrayList<>();
            DishMenu[] menus = DishMenu.values();
            for (int j = 0; j < orderNumber; j++) {
                orders.add(menus[new Random().nextInt(menus.length)]);
            }
            ordersMap.put(getData().teams.getTeam(i), orders);
            BossBarHandler.displayDishMenu(getData().teams.getTeam(i), orders);
        }
        ScoreBoardHandler.initialize();
    }

    public static void generateRandomOrder(OCTeam team) {
        List<DishMenu> orders = ordersMap.get(team);
        DishMenu[] menus = DishMenu.values();
        orders.add(menus[new Random().nextInt(menus.length)]);
        BossBarHandler.displayDishMenu(team, orders);
    }

    public static boolean canSatisfyOrder(OCTeam team, Material material) {
        if (material == null)
            return false;

        List<DishMenu> orders = ordersMap.get(team);
        if (orders.stream().filter(dishMenu -> dishMenu.getProduct() == material).findFirst().orElse(null) != null) {
            return true;
        }

        return false;
    }

    public static boolean trySatisfyOrder(OCTeam team, Material material) {
        if (!canSatisfyOrder(team, material))
            return false;

        List<DishMenu> orders = ordersMap.get(team);
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getProduct() == material) {
                ScoreBoardHandler.addScore(team, orders.get(i).getScore());
                orders.remove(i);
                generateRandomOrder(team);
                return true;
            }
        }

        return false;
    }

    public static void clearOrders() {
        ordersMap.clear();
        ScoreBoardHandler.destroy();
        BossBarHandler.hideEverything();
    }

    /**
     * アイテムが皿であるかどうか
     *
     * @param dishCandidate
     * @return 皿であるかどうか
     */
    public static boolean isDish(ItemStack dishCandidate) {
        if (dishCandidate.getType() != Material.PAPER)
            return false;

        ItemMeta meta = dishCandidate.getItemMeta();
        if (!meta.hasCustomModelData()) {
            return false;
        }

        return true;
    }

    /**
     * 皿に搭載可能なアイテムを載せる
     *
     * @param candidate
     * @return アイテムの載った皿
     */
    public static ItemStack tryEncodeOrderOnDish(Material candidate) {
        if (candidate == Material.AIR)
            return null;

        CustomDishModel[] model = CustomDishModel.values();
        for (CustomDishModel customDishModel : model) {
            if (customDishModel.getMaterialOnDish() == candidate) {
                return new ItemCreator(Material.PAPER).setCustomModelData(customDishModel.getData()).create();
            }
        }

        return null;
    }

    /**
     * 皿に載せられたアイテムを抽出する
     *
     * @param dishCandidate 皿のアイテム
     * @return 注文の種類
     */
    public static Material decodeOrder(ItemStack dishCandidate) {
        if (!isDish(dishCandidate)) {
            throw new IllegalArgumentException();
        }

        int customModelData = new ItemCreator(dishCandidate).getCustomModelData();
        CustomDishModel[] model = CustomDishModel.values();
        for (CustomDishModel customDishModel : model) {
            if (customDishModel.getData() == customModelData) {
                return customDishModel.getMaterialOnDish();
            }
        }

        return null;
    }

    /**
     * 皿アイテムを取得する
     *
     * @return
     */
    public static ItemStack getDish() {
        return new ItemCreator(Material.PAPER).setCustomModelData(CustomDishModel.DISH.getData()).create();
    }

    /**
     * 汚い皿アイテムを取得する
     *
     * @return
     */
    public static ItemStack getDirtyDish() {
        return new ItemCreator(Material.PAPER).setCustomModelData(CustomDishModel.DISH_DIRTY.getData()).create();
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

        int customModelData = new ItemCreator(dishCandidate).getCustomModelData();
        return customModelData == CustomDishModel.DISH_DIRTY.getData();
    }
}
