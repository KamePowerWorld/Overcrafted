package quarri6343.overcrafted.impl.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import quarri6343.overcrafted.api.item.ISupplier;
import quarri6343.overcrafted.core.OCItem;
import quarri6343.overcrafted.core.data.constant.OCSoundData;
import quarri6343.overcrafted.utils.OverCraftedUtil;

/**
 * プレイヤーに供給できるOCItem
 */
public class SuppliableOCItem extends OCItem implements ISupplier {

    /**
     * 固有アイテムの型を作製する
     *
     * @param name            作りたい固有アイテムの名前(ユーザーが読むので必ず日本語にすること)
     * @param material        作りたい固有アイテムの元となるバニラアイテム
     * @param internalName    作りたい固有アイテムの内部的な名前<br>
     * @param customModelData カスタムモデルデータ
     */
    public SuppliableOCItem(TextComponent name, Material material, String internalName, int customModelData) {
        super(name, material, internalName, customModelData);
    }

    @Override
    public void onSupply(Player player) {
        if(OverCraftedUtil.getInventoryItemCount(player.getInventory()) > 0){
            player.sendActionBar(Component.text("これ以上持てない！"));
            return;
        }
        
        player.playSound(OCSoundData.supplyItemSound);
        player.setItemInHand(getItemStack());
    }
}
