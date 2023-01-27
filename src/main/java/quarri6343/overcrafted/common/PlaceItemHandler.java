package quarri6343.overcrafted.common;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.logic.OCLogic;

import java.util.HashMap;
import java.util.Map;

/**
 * 直接拾えないアイテムをワールドに設置、回収するハンドラ
 */
public class PlaceItemHandler {

    private static Map<Block, ItemStack> placedItemMap = new HashMap<>();

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }

    /**
     * アイテムを設置する
     * @param block 設置対象のブロック
     * @param itemStack 設置するアイテムスタック
     * @return 設置できたかどうか
     */
    public static boolean placeItem(Block block, ItemStack itemStack) {
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return false;

        if (placedItemMap.containsKey(block))
            return false;
        
        if(itemStack == null)
            return false;

        if(block.getRelative(BlockFace.UP).getType() != Material.AIR){
            return false;
        }
        
        Location location = block.getLocation();
        location.add(0.5, 1.1, 0.5);
        Item item = block.getWorld().dropItem(location, itemStack);
        item.teleport(location);
        item.setVelocity(new Vector().zero());
        item.setCanPlayerPickup(false);
        item.setCanMobPickup(false);

        placedItemMap.put(block, itemStack);

        return true;
    }

    public static ItemStack getItem(Block block) {
        return placedItemMap.get(block);
    }

    /**
     * アイテムを拾う
     * @param block 拾う対象のブロック
     * @return 拾えたアイテム(拾えなかったらnull)
     */
    public static ItemStack pickUpItem(Block block) {
        ItemStack itemStack = placedItemMap.get(block);
        if (itemStack == null)
            return null;

        Location location = block.getLocation();
        location.add(0.5, 1.1, 0.5);
        location.getNearbyEntities(0.01, 0.01, 0.01).forEach(entity -> {
            if (entity.getType() == EntityType.DROPPED_ITEM) entity.remove();
        });
        placedItemMap.remove(block);
        return itemStack;
    }

    /**
     * 全ての設置済みアイテムを消去する
     */
    public static void clear() {
        placedItemMap.forEach((block, material) -> block.getLocation().getNearbyEntities(2, 2, 2).forEach(entity -> {
            if (entity.getType() == EntityType.DROPPED_ITEM) entity.remove();
        }));
        placedItemMap.clear();
    }
}
