package quarri6343.overcrafted.api.item.interfaces;

import org.bukkit.event.player.PlayerInteractEvent;

/**
 * OCItemがこれを実装することでそのアイテムが左クリックされた時にイベントを受け取ることができる
 */
public interface ILeftClickEventHandler {

    /**
     * 固有アイテムが左クリックされた際に発火されるイベント
     * @param e イベント
     */
    void onLeftClick(PlayerInteractEvent e);
}
