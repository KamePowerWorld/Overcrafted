package quarri6343.overcrafted.common;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * 注文票に載せるメニューのリスト
 */
public enum DishMenu {
    TORCH(new ItemStack(Material.TORCH, 4)),
    FURNACE(new ItemStack(Material.FURNACE)),
    IRON(new ItemStack(Material.IRON_INGOT, 4)),
    MINECART(new ItemStack(Material.MINECART));
    
    private final ItemStack product;

    DishMenu(ItemStack product) {
        this.product = product;
    }

    public ItemStack getProduct() {
        return product;
    }
}
