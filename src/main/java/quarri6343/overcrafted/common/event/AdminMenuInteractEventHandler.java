package quarri6343.overcrafted.common.event;

import com.google.common.base.Objects;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.impl.ui.UIAdminMenu;

/**
 * 手持ちの管理メニューを識別してそれに応じたイベントを起こす
 *
 */
public class AdminMenuInteractEventHandler implements IPlayerInteractEventHandler {
    
    public static final Component menuItemName = Component.text("Overcrafted管理メニュー");
    
    public AdminMenuInteractEventHandler(){
        Overcrafted.getInstance().getPlayerEventHandler().registerHandler(this);
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!(event.getItem() != null && event.getItem().getType().equals(Material.STICK)
                && Objects.equal(event.getItem().getItemMeta().displayName(), menuItemName)))
            return;

        if (!event.getPlayer().isOp())
            return;

        UIAdminMenu.openUI(event.getPlayer());
        event.setCancelled(true);
    }
}
