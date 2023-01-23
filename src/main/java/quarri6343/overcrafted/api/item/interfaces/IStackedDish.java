package quarri6343.overcrafted.api.item.interfaces;

import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.api.item.StackedDish;

/**
 * 積まれた皿の3Dモデルを表示するアイテム
 */
public interface IStackedDish extends IOCItem {

    /**
     * 積まれた枚数を取得
     * @return 積まれた枚数
     */
    public int getStackedNumber();

    /**
     * 積まれた皿の種類を取得
     * @return 積まれた皿の種類
     */
    public StackedDish.StackedDishType getType();
}
