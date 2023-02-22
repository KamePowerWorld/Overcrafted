package quarri6343.overcrafted.api;

import org.bukkit.event.player.PlayerInteractEvent;

/**
 * PlayerEventHandlerにこれを実装したクラスを登録することでイベントを受け取れる
 */
public interface IPlayerInteractEventHandler {

    /**
     * プレイヤーがオブジェクトに干渉した時呼ばれる
     */
    public void onPlayerInteract(PlayerInteractEvent event);
}
