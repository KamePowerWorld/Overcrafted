package quarri6343.overcrafted.common;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.impl.UIAdminMenu;
import quarri6343.overcrafted.utils.OvercraftedUtils;

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
            else if(event.getItem().getType().equals(Material.PAPER) 
                    && event.getItem().getItemMeta().getDisplayName().equals(DishHandler.dishItemName)){
                DishMenu dishMenu = DishHandler.decode(event.getItem());
                if(dishMenu != null){
                    event.getPlayer().sendMessage(OvercraftedUtils.getItemInfoasText(dishMenu.getProduct()).append(Component.text(" が入ったチェストを皿を持って右クリック")));
                    event.setCancelled(true);
                }
            }
        }
    }

}
