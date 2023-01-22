package quarri6343.overcrafted.common.event;

import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.item.interfaces.ILeftClickEventHandler;
import quarri6343.overcrafted.api.item.interfaces.IOCItem;
import quarri6343.overcrafted.api.item.interfaces.IRightClickEventHandler;
import quarri6343.overcrafted.impl.item.OCItems;

/**
 * プレイヤーがアイテムにまつわるイベントを起こしたときそれを検知してそれぞれのアイテムクラスに伝える
 */
public class ItemEventHandler implements Listener, IPlayerInteractEventHandler {

    public ItemEventHandler() {
        Overcrafted.getInstance().getPlayerEventHandler().registerHandler(this);
    }

    /**
     * プレイヤーがアイテムを右クリック/左クリックしたときにメインハンドに持つアイテムの特殊効果を発動させる
     * オフハンドの特殊効果はブロックされる
     * @param e イベント
     */
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getItem() ==null)
            return;

        PlayerInventory inv = e.getPlayer().getInventory();
        ItemStack handItem = inv.getItemInMainHand();
        ItemStack offhandItem = inv.getItemInOffHand();
        IOCItem handOCItem = OCItems.toOCItem(handItem);

        if (handOCItem != null) {
            Action action = e.getAction();

            if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
                if(handOCItem instanceof IRightClickEventHandler)
                    ((IRightClickEventHandler) handOCItem).onRightClick(e);
            } else if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
                if(handOCItem instanceof ILeftClickEventHandler)
                    ((ILeftClickEventHandler) handOCItem).onLeftClick(e);
            }
        }
    }
}
