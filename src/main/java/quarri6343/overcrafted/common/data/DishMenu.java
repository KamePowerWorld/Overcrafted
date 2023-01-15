package quarri6343.overcrafted.common.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * 注文票に載せるメニューのリスト
 */
public enum DishMenu {
    TORCH(new ItemStack(Material.TORCH, 4), 5),
    FURNACE(new ItemStack(Material.FURNACE), 5),
    IRON(new ItemStack(Material.IRON_INGOT, 4), 10),
    MINECART(new ItemStack(Material.MINECART), 15);

    /**
     * メニュー
     */
    private final ItemStack product;

    /**
     * メニューを納品した時に得られるスコア
     */
    private final int score;

    DishMenu(ItemStack product, int score) {
        this.product = product;
        this.score = score;
    }

    public ItemStack getProduct() {
        return product;
    }
    
    public int getScore(){
        return score;
    }
}
