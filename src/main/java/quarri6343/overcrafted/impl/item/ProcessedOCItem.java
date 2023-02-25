package quarri6343.overcrafted.impl.item;

import lombok.Getter;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import quarri6343.overcrafted.api.item.IProcessedOCItem;
import quarri6343.overcrafted.api.block.IBlockProcessor;
import quarri6343.overcrafted.core.OCItem;

/**
 * 加工された後のOCItem
 */
public class ProcessedOCItem extends OCItem implements IProcessedOCItem {

    /**
     * 材料を加工するブロック
     */
    @Getter
    private final IBlockProcessor processType;

    /**
     * 材料
     */
    @Getter
    private final OCItems ingredient;
    
    /**
     * 固有アイテムの型を作製する
     *
     * @param name            作りたい固有アイテムの名前(ユーザーが読むので必ず日本語にすること)
     * @param material        作りたい固有アイテムの元となるバニラアイテム
     * @param internalName    作りたい固有アイテムの内部的な名前<br>
     * @param customModelData カスタムモデルデータ
     */
    public ProcessedOCItem(TextComponent name, Material material, String internalName, int customModelData, IBlockProcessor processType, OCItems ingredinet) {
        super(name, material, internalName, customModelData);
        this.processType = processType;
        this.ingredient = ingredinet;
    }
}
