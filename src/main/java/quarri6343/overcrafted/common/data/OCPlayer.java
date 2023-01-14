package quarri6343.overcrafted.common.data;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * チームに所属するプレイヤーのデータクラス
 */
public class OCPlayer {

    public final Player entity;

    /**
     * プレイヤーがチームに入る前のゲームモード
     */
    private final GameMode lastGameMode;

    /**
     * プレイヤーがチームに入る前のインベントリ
     */
    private final ItemStack[] lastInventory;

    public OCPlayer(@Nonnull Player entity) {
        this.entity = entity;
        this.lastGameMode = entity.getGameMode();
        this.lastInventory = entity.getInventory().getContents();
    }

    /**
     * プレイヤーの状態をチーム参加前に戻す
     */
    public void restoreStats() {
        entity.setGameMode(lastGameMode);
        entity.getInventory().setContents(lastInventory);
    }
}
