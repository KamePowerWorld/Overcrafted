package quarri6343.overcrafted.api.item;

import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.api.item.interfaces.IOCItem;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

/**
 * このプラグイン固有のアイテムの管理クラス<br>
 * 通常のアイテムや召喚コマンドを固有アイテムの型に変換することができる
 */
public class ItemManager {

    /**
     * このプラグインが管理する固有アイテムのリスト
     */
    private static List<IOCItem> items = new ArrayList<>();

    /**
     * 固有アイテムを登録する(登録しないと存在しないことになる)
     * @param item 登録したい固有アイテム
     */
    @ParametersAreNonnullByDefault
    public static void registerItem(IOCItem item){
        items.add(item);
    }

    /**
     * アイテムが固有アイテムであった場合その実体を固有アイテムクラスに変換する<br>
     * この際実体特有のNBTなどは失われる
     * @param itemStack 変換したいアイテム
     * @return 変換された固有アイテム
     */
    @Nullable
    public static IOCItem toOCItem(ItemStack itemStack){
        return items.stream().filter(e-> e.isSimilar(itemStack)).findFirst().orElse(null);
    }

    /**
     * 固有アイテムの召喚コマンドを固有アイテムに変換する
     * @param command 召喚コマンド
     * @return 変換された固有アイテム
     */
    @Nullable
    public static IOCItem commandToOCItem(String command){
        return items.stream().filter(e -> e.getInternalName().equals(command)).findFirst().orElse(null);
    }

    /**
     * 全ての登録されたアイテムを取得
     * @return 全ての登録アイテム
     */
    @Nullable
    public static List<IOCItem> getAllRegisteredItems(){
        return items;
    }
}
