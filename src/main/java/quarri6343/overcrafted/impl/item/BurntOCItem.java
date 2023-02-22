package quarri6343.overcrafted.impl.item;

import lombok.Getter;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import quarri6343.overcrafted.api.item.IBurntOCItem;
import quarri6343.overcrafted.api.item.IProcessedOCItem;
import quarri6343.overcrafted.core.OCItem;

public class BurntOCItem extends OCItem implements IBurntOCItem {
    
    @Getter
    private final IProcessedOCItem ingredient;
    
    /**
     * 　固有アイテムの型を作成する
     *
     * @param name            作りたい固有アイテムの名前(ユーザーが読むので必ず日本語にすること)
     * @param material        作りたい固有アイテムの元となるバニラアイテム
     * @param internalName    作りたい固有アイテムの内部的な名前<br>
     *                        召喚コマンドで使われるので必ず半角英数字にしてスペースの代わりに_を使うこと
     * @param customModelData 固有アイテムにセットするカスタムモデルデータ
     */
    public BurntOCItem(TextComponent name, Material material, String internalName, int customModelData, IProcessedOCItem ingredient) {
        super(name, material, internalName, customModelData);
        this.ingredient = ingredient;
    }
}
