package quarri6343.overcrafted.core.object;

import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.api.object.IOCPlayer;

import javax.annotation.Nonnull;

public class OCPlayer implements IOCPlayer {

    /**
     * プレイヤーのエンティティ
     */
    @Getter
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
    
    public void restoreStats() {
        entity.setGameMode(lastGameMode);
        entity.getInventory().setContents(lastInventory);
    }
    
    public void dropExcessiveItems() {
        ItemStack mainHandItem = entity.getInventory().getItem(0);
        if (mainHandItem != null && mainHandItem.getAmount() > 1) {
            try {
                mainHandItem.setAmount(mainHandItem.getAmount() - 1);
                Item item = entity.getWorld().dropItemNaturally(entity.getLocation(), mainHandItem);
                item.setCanPlayerPickup(false);
            } finally {
                mainHandItem.setAmount(1);
                entity.getInventory().setItem(0, mainHandItem);
            }
        }

        ItemStack offHandItem = entity.getInventory().getItemInOffHand();
        if (offHandItem.getAmount() > 0) {
            entity.getInventory().setItemInOffHand(null);
            Item item = entity.getWorld().dropItemNaturally(entity.getLocation(), offHandItem);
            item.setCanPlayerPickup(false);
        }
    }

    public void teleport(Location location) {
        entity.teleport(location);
    }
}
