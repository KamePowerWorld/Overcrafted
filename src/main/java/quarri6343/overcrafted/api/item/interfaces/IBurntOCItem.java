package quarri6343.overcrafted.api.item.interfaces;

/**
 * 加工されすぎたOCItem
 */
public interface IBurntOCItem {

    /**
     * 加工されすぎる前のアイテム
     * @return
     */
    public IProcessedOCItem getIngredient();
}
