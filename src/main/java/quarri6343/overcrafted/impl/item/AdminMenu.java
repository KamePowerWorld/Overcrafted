package quarri6343.overcrafted.impl.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import quarri6343.overcrafted.api.item.IRightClickEventHandler;
import quarri6343.overcrafted.core.OCItem;
import quarri6343.overcrafted.core.ui.UIAdminMenu;

/**
 * 管理者メニューアイテム
 */
public class AdminMenu extends OCItem implements IRightClickEventHandler {
    
    /**
     * 固有アイテムの型を作製する
     */
    public AdminMenu() {
        super(Component.text("Overcrafted管理メニュー"), Material.STICK, "adminmenu", 1);
    }

    @Override
    public void onRightClick(PlayerInteractEvent event) {
        if (!event.getPlayer().isOp())
            return;

        UIAdminMenu.openUI(event.getPlayer());
        event.setCancelled(true);
    }
}
