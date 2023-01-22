package quarri6343.overcrafted.api.item.interfaces;

import it.unimi.dsi.fastutil.Pair;
import quarri6343.overcrafted.impl.item.OCItems;

public interface ICombinedOCItem {
    
    Pair<OCItems, OCItems> getIngredients();
}
