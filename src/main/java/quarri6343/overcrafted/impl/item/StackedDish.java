package quarri6343.overcrafted.impl.item;

import lombok.Getter;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import quarri6343.overcrafted.api.item.IStackedDish;
import quarri6343.overcrafted.core.OCItem;

public class StackedDish extends OCItem implements IStackedDish {

    /**
     * 積まれた皿の数
     */
    @Getter
    private final int stackedNumber;

    /**
     * 積まれた皿の種類
     */
    @Getter
    private final StackedDishType type;
    
    /**
     * 固有アイテムの型を作製する
     *
     * @param name            作りたい固有アイテムの名前(ユーザーが読むので必ず日本語にすること)
     * @param internalName    作りたい固有アイテムの内部的な名前<br>
     * @param customModelData カスタムモデルデータ
     */
    public StackedDish(TextComponent name, String internalName, int customModelData, int stackedNumber, StackedDishType type) {
        super(name, Material.MUSIC_DISC_PIGSTEP, internalName, customModelData);
        this.stackedNumber = stackedNumber;
        this.type = type;
    }

    /**
     * 積まれた皿の種類
     */
    public enum StackedDishType {
        CLEAN,
        DIRTY
    }
}
