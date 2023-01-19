package quarri6343.overcrafted.common.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.common.data.OCResourcePackData.MenuFont;

/**
 * 注文票に載せるメニューのリスト
 */
public enum DishMenu {
    TORCH(Material.TORCH, 5, MenuFont.TORCH.get_char()),
    FURNACE(Material.FURNACE, 5, MenuFont.FURNACE.get_char()),
    IRON(Material.IRON_INGOT, 3, MenuFont.IRON.get_char()),
    MINECART(Material.MINECART, 15, MenuFont.MINECART.get_char());

    /**
     * メニュー
     */
    private final Material product;

    /**
     * メニューを納品した時に得られるスコア
     */
    private final int score;

    /**
     * メニューを表すカスタムアイコンの文字コード
     */
    private final String iconUniCode;

    DishMenu(Material product, int score, String iconUniCode) {
        this.product = product;
        this.score = score;
        this.iconUniCode = iconUniCode;
    }

    public Material getProduct() {
        return product;
    }

    public int getScore() {
        return score;
    }

    public String toUnicode() {
        return iconUniCode;
    }
}
