package quarri6343.overcrafted.common.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.order.OrderHandler;

/**
 * GUIを通じてプレイヤーに無限に供給される素材アイテムの一覧
 */
public enum Supply {
    WOOD(new ItemStack(Material.OAK_WOOD, 1)),
    COBBLESTONE(new ItemStack(Material.COBBLESTONE, 1)),
    IRON_ORE(new ItemStack(Material.IRON_ORE, 1)),
    BUCKET(new ItemStack(Material.BUCKET, 1));

    /**
     * 素材アイテム
     */
    private final ItemStack supply;

    Supply(ItemStack supply) {
        if(supply.getAmount() > 1)
            Overcrafted.getInstance().getLogger().severe("材料のスタックサイズは1にしてください");
        
        this.supply = supply;
    }

    public ItemStack getItemStack() {
        return supply;
    }
}
