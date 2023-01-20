package quarri6343.overcrafted.common.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.utils.ItemCreator;

import java.util.Collection;

/**
 * 綺麗な皿置場
 */
public class CleanDishPile {

    /**
     * 皿置場のワールド内での場所
     */
    public Location location = null;

    /**
     * 皿置場を設置
     */
    public void place(int dishNumber) {
        if (location == null) {
            Overcrafted.getInstance().getLogger().severe("存在しない座標に皿置き場は置けません！");
            return;
        }

        ItemFrame itemFrame = location.getWorld().spawn(location, ItemFrame.class);
        itemFrame.setFacingDirection(BlockFace.DOWN);
        itemFrame.setFixed(true);
        itemFrame.setVisible(false);
        itemFrame.setItem(new ItemCreator(Material.MUSIC_DISC_PIGSTEP).setCustomModelData(1).create());//placeholder
    }

    /**
     * 設置されている皿置場を消去
     */
    public void destroy() {
        if (location == null) {
            Overcrafted.getInstance().getLogger().severe("存在しない座標の注文箱は壊せません！");
            return;
        }

        Collection<ItemFrame> itemFrames = location.getWorld().getNearbyEntitiesByType(ItemFrame.class, location, 2);
        itemFrames.forEach(Entity::remove);
    }

    /**
     * 皿置き場が設置されているかどうか
     */
    public boolean isPlaced() {
        if (location == null)
            return false;

        Block block = location.getWorld().getBlockAt(location);
        if (block.getType() != Material.ITEM_FRAME)
            return false;

        ItemFrame itemFrame = (ItemFrame) block.getState();
        return itemFrame.isFixed();
    }
    
    public void setUp(){
        place(OCData.dishesOnStart);
    }

    /**
     * 皿置場に皿を追加する
     * @return アイテムの追加に成功したかどうか
     */
    public boolean addDish() {
        return true; //未実装
    }
}
