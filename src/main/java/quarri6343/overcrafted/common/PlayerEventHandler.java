package quarri6343.overcrafted.common;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
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
        if(event.getHand() != EquipmentSlot.HAND)
            return;
        
        if (event.getItem() != null) {
            if (event.getItem().getType().equals(Material.STICK)
                    && event.getItem().getItemMeta().getDisplayName().equals(menuItemName)) {
                processHandheldAdminMenu(event);
            }
            else if(event.getItem().getType().equals(Material.PAPER) 
                    && event.getItem().getItemMeta().getDisplayName().equals(DishHandler.dishItemName)){
                processHandheldDish(event);
            }
        }
    }

    /**
     * 手持ちの管理メニューを識別してそれに応じたイベントを起こす
     * @param event
     */
    private void processHandheldAdminMenu(PlayerInteractEvent event){
        if(!event.getPlayer().isOp())
            return;
        
        UIAdminMenu.openUI(event.getPlayer());
        event.setCancelled(true);
    }

    /**
     * 手持ちの皿を識別してそれに応じたイベントを起こす
     * @param event
     */
    private void processHandheldDish(PlayerInteractEvent event){
        event.setCancelled(true);
        
        if(event.getItem().getAmount() != 1){
            event.getPlayer().sendMessage(Component.text("どうやって皿を複数枚持ったの?"));
            return;
        }
        
        DishMenu dishMenu = DishHandler.decodeOrder(event.getItem());
        if(dishMenu == null){
            return;
        }
        
        if(DishHandler.isOrderCompleted(event.getItem())){
            if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.RED_BED){
                //ここに納品完了時の処理
                event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
            }
            else{
                event.getPlayer().sendMessage(Component.text("赤いベッドを右クリックして納品しよう"));
            }
        }
        else{
            if(event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Container){
                Inventory inventory = ((Container)event.getClickedBlock().getState()).getInventory();
                if(inventory.containsAtLeast(dishMenu.getProduct(), dishMenu.getProduct().getAmount())){
                    inventory.removeItemAnySlot(dishMenu.getProduct());
                    event.getPlayer().setItemInHand(DishHandler.encodeOrderAsCompleted(dishMenu));
                }
            }else{
                event.getPlayer().sendMessage(OvercraftedUtils.getItemInfoasText(dishMenu.getProduct()).append(Component.text(" が入ったブロックを皿を持って右クリックしよう")));
            }
        }
    }
}
