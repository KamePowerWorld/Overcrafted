package quarri6343.overcrafted.api.object;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * 皿置場
 */
public interface IDishPile {

    /**
     * 皿置場を設置
     */
    public void place();
    
    public void reset();

    /**
     * 設置されている皿置場を消去
     */
    public void destroy();

    /**
     * 皿置き場が設置されているかどうか
     */
    public boolean isPlaced();

    /**
     * 皿置き場の皿の数を最大にし、設置する
     */
    public void setUp();

    /**
     * 皿置場に皿を追加する
     *
     * @return アイテムの追加に成功したかどうか
     */
    public boolean addDish();

    /**
     * 皿置場から皿を取り除く
     *
     * @return
     */
    public boolean removeDish();

    /**
     * 積まれた皿の額縁エンティティを取得
     * @return
     */
    public Entity getDishPileEntity();

    /**
     * 積まれた皿の座標を取得
     * @return
     */
    public Location getLocation();

    /**
     * 積まれた皿の座標を設定
     * @param location
     */
    public void setLocation(Location location);
}