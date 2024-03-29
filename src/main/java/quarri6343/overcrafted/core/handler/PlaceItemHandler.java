package quarri6343.overcrafted.core.handler;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.OCLogic;
import quarri6343.overcrafted.core.data.constant.OCCommonData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

        if(block.getRelative(BlockFace.UP).getType() == Material.AIR){
            Location location = block.getLocation();
            location.add(0.5, 1.1, 0.5);
            Item item = block.getWorld().dropItem(location, itemStack);
            item.teleport(location);
            item.setVelocity(new Vector().zero());
            item.setCanPlayerPickup(false);
            item.setCanMobPickup(false);
            item.setGravity(false);
            item.setThrower(OCCommonData.placedItemTag);

            placedItemMap.put(block, itemStack);

            return true;
        }

        //水中に対応
        if(block.getRelative(BlockFace.UP).getType() == Material.WATER){
            Location location = block.getLocation();
            location.add(0.5, 1.1, 0.5);
            ItemFrame itemFrame = location.getWorld().spawn(location, ItemFrame.class);
            itemFrame.setFacingDirection(BlockFace.UP);
            itemFrame.setFixed(true);
            itemFrame.setVisible(false);
            itemFrame.setItem(itemStack);
            itemFrame.setCustomNameVisible(false);

            placedItemMap.put(block, itemStack);

            return true;
        }
        
        return false;
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
        location.getNearbyEntities(0.001, 0.001, 0.001).forEach(entity -> {
            if ((entity.getType() == EntityType.DROPPED_ITEM && ((Item)entity).getThrower() != null && ((Item)entity).getThrower().equals(OCCommonData.placedItemTag)) || entity.getType() == EntityType.ITEM_FRAME) entity.remove();
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
