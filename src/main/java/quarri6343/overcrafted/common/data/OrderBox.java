package quarri6343.overcrafted.common.data;

import com.google.common.base.Objects;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;

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
     * 注文箱の中身を消去する
     */
    public void clear() {
        Chest chest = (Chest) location.getWorld().getBlockAt(location).getState();
        chest.getInventory().clear();
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
     * 注文箱の最初に空いているスロットに任意のアイテムを追加する
     * @param itemStack 追加したいアイテム
     * @return アイテムの追加に成功したかどうか
     */
    public boolean addItem(ItemStack itemStack) {
        if (!isPlaced())
            place();

        if (itemStack.getAmount() > 1)
            Overcrafted.getInstance().getLogger().severe("注文箱に入れられるアイテムのスタックサイズは1までです");

        Chest chest = (Chest) location.getWorld().getBlockAt(location).getState();
        ItemStack[] contents = chest.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] == null) {
                contents[i] = itemStack;
                chest.getInventory().setContents(contents);
                return true;
            }
        }

        return false;
    }
}
