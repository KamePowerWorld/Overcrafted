package quarri6343.overcrafted.impl.item;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import quarri6343.overcrafted.api.item.ISubmittableOCItem;

public class SubmittableOCItem extends CombinedOCItem implements ISubmittableOCItem {
    
    /**
     * メニューを納品した時に得られるスコア
     */
    private final int score;

    /**
     * ボスバーに表示されるメニューを表すカスタムアイコンの文字コード
     */
    private final String iconUniCode;
    
    /**
     * 固有アイテムの型を作製する
     *
     * @param name            作りたい固有アイテムの名前(ユーザーが読むので必ず日本語にすること)
     * @param material        作りたい固有アイテムの元となるバニラアイテム
     * @param internalName    作りたい固有アイテムの内部的な名前<br>
     * @param customModelData
     */
    public SubmittableOCItem(TextComponent name, Material material, String internalName, int customModelData, OCItems ingredient1, OCItems ingredient2, int score, String iconUniCode) {
        super(name, material, internalName, customModelData, ingredient1, ingredient2);
        this.score = score;
        this.iconUniCode = iconUniCode;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public String toMenuUnicode() {
        return iconUniCode;
    }
}
