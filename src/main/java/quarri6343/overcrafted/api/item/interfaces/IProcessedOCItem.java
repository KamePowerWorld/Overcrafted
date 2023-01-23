package quarri6343.overcrafted.api.item.interfaces;

import quarri6343.overcrafted.impl.block.IBlockProcessor;
import quarri6343.overcrafted.impl.item.OCItems;

/**
 * 一種類のアイテムを特定のブロックで加工することで得られる製品のアイテム
 */
public interface IProcessedOCItem {

    /**
     * 材料を取得
     *
     * @return 材料
     */
    public OCItems getIngredient();

    /**
     * 材料を加工するブロックを取得
     *
     * @return 加工するブロック
     */
    public IBlockProcessor getProcessType();
}
