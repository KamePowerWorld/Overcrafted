package quarri6343.overcrafted.api.item;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import quarri6343.overcrafted.api.item.interfaces.ICombinedOCItem;
import quarri6343.overcrafted.impl.item.OCItems;

public class CombinedOCItem extends OCItem implements ICombinedOCItem {
    
    private final OCItems ingredient1;
    private final OCItems ingredient2;
    
    /**
     * 固有アイテムの型を作製する
     *
     * @param name            作りたい固有アイテムの名前(ユーザーが読むので必ず日本語にすること)
     * @param material        作りたい固有アイテムの元となるバニラアイテム
     * @param internalName    作りたい固有アイテムの内部的な名前<br>
     * @param customModelData
     */
    public CombinedOCItem(TextComponent name, Material material, String internalName, int customModelData, OCItems ingredient1, OCItems ingredient2) {
        super(name, material, internalName, customModelData);
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
    }

    @Override
    public Pair<OCItems, OCItems> getIngredients() {
        return new ObjectObjectImmutablePair<OCItems, OCItems>(ingredient1, ingredient2);
    }
}
