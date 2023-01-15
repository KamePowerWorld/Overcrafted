package quarri6343.overcrafted.common.data;

import com.google.common.base.Objects;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.DishHandler;

/**
 * 注文箱
 */
public class OrderBox {

    /**
     * 注文箱のワールド内での場所
     */
    public Location location = null;

    private static final Component boxName = Component.text("注文箱");

    /**
     * 注文箱を設置
     */
    public void place() {
        if (location == null) {
            Overcrafted.getInstance().getLogger().severe("存在しない座標に注文箱は置けません！");
            return;
        }

        location.getWorld().setType(location, Material.CHEST);
        Chest chest = (Chest) location.getWorld().getBlockAt(location).getState();
        chest.customName(boxName);
        chest.update();
    }

    /**
     * 設置されている注文箱を中身ごと破壊
     */
    public void destroy() {
        if (location == null) {
            Overcrafted.getInstance().getLogger().severe("存在しない座標の注文箱は壊せません！");
            return;
        }

        location.getWorld().setType(location, Material.AIR);
    }

    /**
     * 注文箱が設置されているかどうか
     */
    public boolean isPlaced() {
        if (location == null)
            return false;

        Block block = location.getWorld().getBlockAt(location);
        if (block.getType() != Material.CHEST)
            return false;

        Chest chest = (Chest) block.getState();
        return Objects.equal(chest.customName(), boxName);
    }

    /**
     * 注文箱に任意のアイテムを追加する
     */
    public void addItem(ItemStack itemStack){
        if (!isPlaced())
            place();
        
        Chest chest = (Chest) location.getWorld().getBlockAt(location).getState();
        chest.getInventory().addItem(itemStack);
    }
}
