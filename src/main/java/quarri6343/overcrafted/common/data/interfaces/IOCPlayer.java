package quarri6343.overcrafted.common.data.interfaces;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * チームに所属するプレイヤー
 */
public interface IOCPlayer {
    
    public Player getEntity();

    /**
     * プレイヤーの状態をチーム参加前に戻す
     */
    public void restoreStats();

    /**
     * プレイヤーがアイテムを2つ以上持っていた場合、余剰分をドロップさせる
     */
    public void dropExcessiveItems();

    /**
     * プレイヤーをテレポートさせる
     * @param location
     */
    public void teleport(Location location);
}
