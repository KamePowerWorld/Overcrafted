package quarri6343.overcrafted.core;

import lombok.Data;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.api.item.IOCItem;
import quarri6343.overcrafted.utils.ItemCreator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@Data
public class OCItem implements IOCItem {

    /**
     * アイテムの表示名。金床で変更できる
     */
    private final TextComponent name;

    /**
     * アイテムの元となるバニラアイテムの種類
     */
    private final Material material;

    /**
     * アイテムのインスタンスのテンプレート、つまり型<br>
     * アイテムを作製した時これがまず渡されるので必要に応じてインスタンスの固有データを流し込む<br>
     * 注意：絶対に変更してはいけないので必ずクローンして使うこと
     */
    private final ItemStack itemStackTemplate;

    /**
     * 主に召喚コマンドで用いられる内部的なアイテム名<br>
     */
    private final String internalName;

    /**
     * アイテムのカスタムモデルデータ(0に設定した場合なし)
     */
    private final int customModelData;

    /**
     *　固有アイテムの型を作成する
     * 
     * @param name 作りたい固有アイテムの名前(ユーザーが読むので必ず日本語にすること)
     * @param material 作りたい固有アイテムの元となるバニラアイテム
     * @param internalName 作りたい固有アイテムの内部的な名前<br>
     *                     召喚コマンドで使われるので必ず半角英数字にしてスペースの代わりに_を使うこと
     * @param customModelData 固有アイテムにセットするカスタムモデルデータ
     */
    @ParametersAreNonnullByDefault
    public OCItem(TextComponent name, Material material, String internalName, int customModelData) {
        this.name = name;
        this.material = material;
        this.internalName = internalName;
        this.customModelData = customModelData;
        itemStackTemplate = createItem();
    }

    /**
     * 固有アイテムの型の実体を作製する内部的な関数
     *
     * @return 作られたアイテムの型の実体
     */
    private ItemStack createItem() {
        return new ItemCreator(material)
                .setName(name)
                .setStrNBT("OCID", internalName)
                .hideEnchantment()
                .setCustomModelData(customModelData)
                .setUnbreakable(false)
                .create();
    }

    public void onGiveCommand(CommandSender sender, String[] argments) {
        Player player = (Player) sender;
        player.getInventory().addItem(itemStackTemplate);
    }

    @Nonnull
    public ItemStack getItemStack() {
        return itemStackTemplate.clone();
    }

    public boolean isSimilar(@Nullable ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().equals(Material.AIR))
            return false;

        String ID = new ItemCreator(itemStack).getStrNBT("OCID");

        if(ID == null)
            return false;

        return ID.equals(internalName);
    }
}
