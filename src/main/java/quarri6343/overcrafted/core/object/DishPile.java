package quarri6343.overcrafted.core.object;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.data.constant.OCCommonData;
import quarri6343.overcrafted.impl.item.StackedDish;
import quarri6343.overcrafted.api.item.IStackedDish;
import quarri6343.overcrafted.api.IDishPile;
import quarri6343.overcrafted.impl.item.OCItems;

import java.util.Collection;

public class DishPile implements IDishPile {

    /**
     * 皿置場のワールド内での場所
     */
    @Getter
    @Setter
    private Location location = null;

    private final StackedDish.StackedDishType type;

    @Getter
    private Entity dishPileEntity;

    /**
     * 今積みあがっている皿の枚数
     */
    private int dishNumber = 0;

    public DishPile(StackedDish.StackedDishType type) {
        this.type = type;
    }
    
    public void place() {
        if (location == null) {
            Overcrafted.getInstance().getLogger().severe("存在しない座標に皿置き場は置けません！");
            return;
        }

        destroy();
        if (dishNumber <= 0) {
            return;
        }

        ItemStack itemStack = null;
        for (OCItems item : OCItems.values()) {
            if (item.get() instanceof IStackedDish && ((IStackedDish)item.get()).getType() == type
                && ((IStackedDish)item.get()).getStackedNumber() == dishNumber) {
                itemStack = ((IStackedDish)item.get()).getItemStack();
                break;
            }
        }
        
        if(itemStack == null)
            Overcrafted.getInstance().getLogger().severe("積まれた皿のグラフィックに対応するアイテムが見つかりません！");

        ItemFrame itemFrame = location.getWorld().spawn(location, ItemFrame.class);
        itemFrame.setFacingDirection(BlockFace.UP);
        itemFrame.setFixed(true);
        itemFrame.setVisible(false);
        itemFrame.setItem(itemStack);
        itemFrame.setCustomNameVisible(false);
        dishPileEntity = itemFrame;
    }
    
    public void destroy() {
        if (location == null) {
            Overcrafted.getInstance().getLogger().severe("存在しない座標の皿置場は消せません！");
            return;
        }

        Collection<ItemFrame> itemFrames = location.getWorld().getNearbyEntitiesByType(ItemFrame.class, location, 2);
        itemFrames.forEach(Entity::remove);
    }
    
    public boolean isPlaced() {
        if (location == null)
            return false;

        Block block = location.getWorld().getBlockAt(location);
        if (block.getType() != Material.ITEM_FRAME)
            return false;

        ItemFrame itemFrame = (ItemFrame) block.getState();
        return itemFrame.isFixed();
    }

    public void setUp() {
        dishNumber = OCCommonData.maxDishesNumber;
        place();
    }
    
    public boolean addDish() {
        if (dishNumber + 1 > OCCommonData.maxDishesNumber)
            return false;

        dishNumber++;
        place();

        return true;
    }
    
    public boolean removeDish() {
        if (dishNumber - 1 < 0)
            return false;

        dishNumber--;
        place();

        return true;
    }
}
