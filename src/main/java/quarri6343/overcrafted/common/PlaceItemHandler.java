package quarri6343.overcrafted.common;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.logic.OCLogic;

import java.util.HashMap;
import java.util.Map;

public class PlaceItemHandler {

    private static Map<Block, Material> placedItemMap = new HashMap<>();

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }

    public static boolean placeItem(Block block, Material material) {
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return false;
        
        if(placedItemMap.containsKey(block))
            return false;
        
        Location location = block.getLocation();
        location.add(block.getX() > 0 ? -0.5 : 0.5, 1.1, block.getZ() > 0 ? -0.5 : 0.5);
        Item item = block.getWorld().dropItem(location, new ItemStack(material));
        item.teleport(location);
        item.setVelocity(new Vector().zero());
        item.setCanPlayerPickup(false);
        item.setCanMobPickup(false);

        placedItemMap.put(block, material);
        
        return true;
    }

    public static Material pickUpItem(Block block) {
        Material material = placedItemMap.get(block);
        if (material == null)
            return null;

        Location location = block.getLocation();
        location.add(block.getX() > 0 ? -0.5 : 0.5, 1.1, block.getZ() > 0 ? -0.5 : 0.5);
        location.getNearbyEntities(0.01, 0.01, 0.01).forEach(entity -> {
            if (entity.getType() == EntityType.DROPPED_ITEM) entity.remove();
        });
        placedItemMap.remove(block);
        return material;
    }

    public static void clear() {
        placedItemMap.forEach((block, material) -> block.getLocation().getNearbyEntities(2, 2, 2).forEach(entity -> {
            if (entity.getType() == EntityType.DROPPED_ITEM) entity.remove();
        }));
        placedItemMap.clear();
    }
}
