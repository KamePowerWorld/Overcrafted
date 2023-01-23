package quarri6343.overcrafted.impl.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * 提出してスコアに変換可能なアイテム
 */
public interface ISubmittable {

    /**
     * 提出した時のスコア
     * @return スコア
     */
    int getScore();

    /**
     * アイテムを表すunicode
     * @return unicode
     */
    String toMenuUnicode();
}
