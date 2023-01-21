package quarri6343.overcrafted.common.event;

import org.bukkit.event.player.PlayerInteractEvent;

public interface IPlayerInteractEventHandler {

    /**
     * プレイヤーがオブジェクトに干渉した時呼ばれる
     */
    public void onPlayerInteract(PlayerInteractEvent event);
}
