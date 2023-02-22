package quarri6343.overcrafted.api.item;

/**
 * 加工されすぎたOCItem
 */
public interface IBurntOCItem {

    /**
     * 加工されすぎる前の、既に加工されたアイテム
     * @return
     */
    public IProcessedOCItem getIngredient();
}
