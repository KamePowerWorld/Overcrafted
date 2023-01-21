package quarri6343.overcrafted.common.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.utils.ItemCreator;

import java.util.Collection;

/**
 * 皿置場
 */
public class DishPile {

    /**
     * 皿置場のワールド内での場所
     */
    public Location location = null;

    public OCResourcePackData.IDishModel[] iDishModels;

    private Entity dishPileEntity;

    /**
     * 今積みあがっている皿の枚数
     */
    private int dishNumber = 0;

    public DishPile(OCResourcePackData.IDishModel[] dishModels) {
        this.iDishModels = dishModels;
    }

    /**
     * 皿置場を設置
     */
    public void place() {
        if (location == null) {
            Overcrafted.getInstance().getLogger().severe("存在しない座標に皿置き場は置けません！");
            return;
        }

        destroy();
        if (dishNumber <= 0) {
            return;
        }

        int modelData = 0;
        for (OCResourcePackData.IDishModel dishModel : iDishModels) {
            if (dishModel.getStackedNumber() == dishNumber) {
                modelData = dishModel.getData();
                break;
            }
        }

        ItemFrame itemFrame = location.getWorld().spawn(location, ItemFrame.class);
        itemFrame.setFacingDirection(BlockFace.DOWN);
        itemFrame.setFixed(true);
        itemFrame.setVisible(false);
        itemFrame.setItem(new ItemCreator(Material.MUSIC_DISC_PIGSTEP).setCustomModelData(modelData).create());//placeholder
        dishPileEntity = itemFrame;
    }

    /**
     * 設置されている皿置場を消去
     */
    public void destroy() {
        if (location == null) {
            Overcrafted.getInstance().getLogger().severe("存在しない座標の皿置場は消せません！");
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

    public void setUp() {
        dishNumber = OCData.maxDishesNumber;
        place();
    }

    /**
     * 皿置場に皿を追加する
     *
     * @return アイテムの追加に成功したかどうか
     */
    public boolean addDish() {
        if (dishNumber + 1 > OCData.maxDishesNumber)
            return false;

        dishNumber++;
        place();

        return true;
    }

    /**
     * 皿置場から皿を取り除く
     *
     * @return
     */
    public boolean removeDish() {
        if (dishNumber - 1 < 0)
            return false;

        dishNumber--;
        place();

        return true;
    }

    public Entity getDishPileEntity() {
        return dishPileEntity;
    }
}
