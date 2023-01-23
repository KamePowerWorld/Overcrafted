package quarri6343.overcrafted.api.item.interfaces;

import org.bukkit.event.player.PlayerInteractEvent;

/**
 * OCItemがこれを実装することでそのアイテムが右クリックされた時にイベントを受け取ることができる
 */
public interface IRightClickEventHandler {

    /**
     * 固有アイテムが右クリックされた際に発火されるイベント
     *
     * @param e イベント
     */
    void onRightClick(PlayerInteractEvent e);
}