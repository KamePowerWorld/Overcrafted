package quarri6343.overcrafted.api.item;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import quarri6343.overcrafted.api.item.interfaces.IStackedDish;

public class StackedDish extends OCItem implements IStackedDish {

    private final int stackedNumber;
    private final StackedDishType type;
    
    /**
     * 固有アイテムの型を作製する
     *
     * @param name            作りたい固有アイテムの名前(ユーザーが読むので必ず日本語にすること)
     * @param internalName    作りたい固有アイテムの内部的な名前<br>
     * @param customModelData
     */
    public StackedDish(TextComponent name, String internalName, int customModelData, int stackedNumber, StackedDishType type) {
        super(name, Material.MUSIC_DISC_PIGSTEP, internalName, customModelData);
        this.stackedNumber = stackedNumber;
        this.type = type;
    }

    public int getStackedNumber() {
        return stackedNumber;
    }
    
    public StackedDishType getType(){
        return type;
    }

    public enum StackedDishType {
        CLEAN,
        DIRTY
    }
}
