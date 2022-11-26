package quarri6343.overcrafted.common;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.impl.UIAdminMenu;

public class PlayerEventHandler implements Listener {
    
    public static final String menuItemName = "Overcrafted管理メニュー";

    public PlayerEventHandler() {
        Overcrafted.getInstance().getServer().getPluginManager().registerEvents(this, Overcrafted.getInstance());
    }

    @org.bukkit.event.EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        processHandheldItem(event);
    }

    /**
     * 手持ちのアイテムを識別してそれに対応したguiを開く
     */
    private void processHandheldItem(PlayerInteractEvent event) {
        if (event.getItem() != null) {
            if (event.getItem().getType().equals(Material.STICK)
                    && event.getItem().getItemMeta().getDisplayName().equals(menuItemName) && event.getPlayer().isOp()) {
                UIAdminMenu.openUI(event.getPlayer());
                event.setCancelled(true);
            }
        }
    }

}
