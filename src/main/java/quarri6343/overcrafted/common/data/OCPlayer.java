package quarri6343.overcrafted.common.data;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * チームに所属するプレイヤーのクラス
 */
public class OCPlayer {

    private final Player entity;

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

    public Player getEntity() {
        return entity;
    }

    /**
     * プレイヤーの状態をチーム参加前に戻す
     */
    public void restoreStats() {
        entity.setGameMode(lastGameMode);
        entity.getInventory().setContents(lastInventory);
    }

    /**
     * プレイヤーがアイテムを2つ以上持っていた場合、余剰分をドロップさせる
     */
    public void dropExcessiveItems() {
        ItemStack mainHandItem = entity.getInventory().getItem(0);
        if (mainHandItem != null && mainHandItem.getAmount() > 1) {
            try {
                mainHandItem.setAmount(mainHandItem.getAmount() - 1);
                entity.getWorld().dropItemNaturally(entity.getLocation(), mainHandItem);
            } finally {
                mainHandItem.setAmount(1);
                entity.getInventory().setItem(0, mainHandItem);
            }
        }

        ItemStack offHandItem = entity.getInventory().getItemInOffHand();
        if (offHandItem.getAmount() > 0) {
            entity.getInventory().setItemInOffHand(null);
            entity.getWorld().dropItemNaturally(entity.getLocation(), offHandItem);
        }
    }

    public void teleport(Location location) {
        entity.teleport(location);
    }
}
