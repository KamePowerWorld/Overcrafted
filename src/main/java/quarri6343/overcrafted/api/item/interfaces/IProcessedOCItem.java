package quarri6343.overcrafted.api.item.interfaces;

import quarri6343.overcrafted.impl.block.BlockProcessor;
import quarri6343.overcrafted.impl.item.OCItems;

public interface IProcessedOCItem {
    
    public OCItems getIngredient();
    
    public BlockProcessor getType();
}
