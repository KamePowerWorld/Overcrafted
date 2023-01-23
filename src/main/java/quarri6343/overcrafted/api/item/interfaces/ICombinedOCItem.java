package quarri6343.overcrafted.api.item.interfaces;

import it.unimi.dsi.fastutil.Pair;
import quarri6343.overcrafted.impl.item.OCItems;

/**
 * 二つのアイテムを机で合体させることにより作られるアイテム
 */
public interface ICombinedOCItem extends IOCItem {

    /**
     * 二つの材料を取得する
     *
     * @return 材料
     */
    Pair<OCItems, OCItems> getIngredients();
}
