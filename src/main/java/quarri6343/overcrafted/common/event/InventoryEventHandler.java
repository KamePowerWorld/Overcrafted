package quarri6343.overcrafted.common.event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;

public class InventoryEventHandler implements Listener {

    public InventoryEventHandler() {
        Overcrafted.getInstance().getServer().getPluginManager().registerEvents(this, Overcrafted.getInstance());
    }

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }

    @org.bukkit.event.EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return;

        if (!(event.getWhoClicked() instanceof Player))
            return;

        OCTeam team = getData().teams.getTeambyPlayer((Player) event.getWhoClicked());
        if (team == null)
            return;

        blockInvalidSlotInteraction(event);
        blockItemClick(event);
    }

    /**
     * ロックされているスロットへの干渉を阻止する
     *
     * @param event
     */
    private void blockInvalidSlotInteraction(InventoryClickEvent event) {
        if (event.isCancelled())
            return;

        if (event.getClickedInventory() == null)
            return;

        if (event.getClick().equals(ClickType.NUMBER_KEY)) { //handle number key click
            if (event.getHotbarButton() == 0) {
                return;
            }

            event.setCancelled(true);
            return;
        }

        if (event.getClickedInventory() == null || event.getClickedInventory().getType() != InventoryType.PLAYER)
            return;

        if (event.getSlot() > 0 && event.getSlot() < 36)
            event.setCancelled(true);
    }

    /**
     * アイテムが2つ以上インベントリでスタックされることを防ぐ
     */
    private void blockItemClick(InventoryClickEvent event) {
        if (event.isCancelled())
            return;

        if (event.getClickedInventory() == null)
            return;

        if (event.getClick().equals(ClickType.DOUBLE_CLICK)) {
            event.setCancelled(true);
            return;
        }
        
        if (event.getClick().equals(ClickType.NUMBER_KEY)) {
            blockNumberKeyClick(event);
        } else if (event.getClick().equals(ClickType.SHIFT_LEFT) || event.getClick().equals(ClickType.SHIFT_RIGHT)) {
            blockShiftKeyClick(event);
        } else if (!(event.getClickedInventory().getType() == InventoryType.PLAYER)) {
            blockTargetInventoryClick(event);
        } else {
            blockPlayerInventoryClick(event);
        }
        
    }
    
    private void blockNumberKeyClick(InventoryClickEvent event){
        ItemStack slotItem = event.getView().getTopInventory().getItem(event.getHotbarButton());
        if ((slotItem == null || slotItem.getType() == Material.AIR)) {
            return;
        }

        if (slotItem.getAmount() > 1)
            event.setCancelled(true);
    }
    
    private void blockShiftKeyClick(InventoryClickEvent event){
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null || currentItem.getType() == Material.AIR) {
            return;
        }

        if (currentItem.getAmount() > 1) {
            if(event.getClickedInventory().getType() == InventoryType.WORKBENCH && event.getSlot() == 0){
                return;
            }

            event.setCancelled(true);
            return;
        }

        if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
            int firstEmpty = event.getView().getTopInventory().firstEmpty();
            if (firstEmpty != -1){
                event.setCancelled(true);
                event.getView().getTopInventory().setItem(firstEmpty, currentItem);
                event.setCurrentItem(null);
            }
        } else {
            if (event.getView().getBottomInventory().containsAtLeast(currentItem, 1))
                event.setCancelled(true);
        }
    }
    
    private void blockTargetInventoryClick(InventoryClickEvent event){
        ItemStack currentItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();
        if (cursorItem == null || cursorItem.getType() == Material.AIR) {
            if (currentItem != null && currentItem.getAmount() > 1) {
                if(event.getClickedInventory().getType() == InventoryType.WORKBENCH && event.getSlot() == 0){
                    event.setCancelled(true);

                    ItemStack clickedItem = event.getClickedInventory().getItem(0);
                    if(clickedItem == null || clickedItem.getType() == Material.AIR)
                        return;

                    if(event.getView().getBottomInventory().firstEmpty() != -1){
                        try{
                            event.getView().getBottomInventory().addItem(clickedItem);
                        }
                        finally {
                            event.getClickedInventory().clear();
                        }
                    }
                    return;
                }

                event.setCancelled(true);

                if (event.getView().getBottomInventory().firstEmpty() != -1) {
                    currentItem.setAmount(currentItem.getAmount() - 1);
                    ItemStack itemToAdd = currentItem.clone();
                    itemToAdd.setAmount(1);
                    event.getView().getBottomInventory().addItem(itemToAdd);
                }
            }
            return;
        }

        if(currentItem == null || currentItem.getType() == Material.AIR)
            return;

        if (cursorItem.getType() == currentItem.getType())
            event.setCancelled(true);
    }
    
    private void blockPlayerInventoryClick(InventoryClickEvent event){
        ItemStack currentItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();
        if (cursorItem == null || cursorItem.getType() == Material.AIR) {
            if (currentItem != null && currentItem.getAmount() > 1) {
                event.setCancelled(true);

                if (event.getView().getTopInventory().firstEmpty() != -1) {
                    currentItem.setAmount(currentItem.getAmount() - 1);
                    ItemStack itemToAdd = currentItem.clone();
                    itemToAdd.setAmount(1);
                    event.getView().getTopInventory().addItem(itemToAdd);
                }
            }
            return;
        }

        if(currentItem == null || currentItem.getType() == Material.AIR)
            return;

        if (cursorItem.getType() == currentItem.getType())
            event.setCancelled(true);
    }

    @org.bukkit.event.EventHandler
    public void onInventoryMove(InventoryMoveItemEvent event) {
        blockInvalidSlotInteraction(event);
    }

    /**
     * ロックされているスロットへの干渉を阻止する
     *
     * @param event
     */
    private void blockInvalidSlotInteraction(InventoryMoveItemEvent event) {
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return;

        if (event.getSource().getType() != InventoryType.PLAYER && event.getDestination().getType() != InventoryType.PLAYER)
            return;

        if (event.getItem().getType() == OCData.invalidItem.getType())
            event.setCancelled(true);
    }

    @org.bukkit.event.EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        blockDragging(event);
    }

    /**
     * ゲーム中プレイヤーはアイテムを1個しか持てないため、ドラッグを阻止する
     *
     * @param event
     */
    private void blockDragging(InventoryDragEvent event) {
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return;

        if (!(event.getWhoClicked() instanceof Player))
            return;

        OCTeam team = getData().teams.getTeambyPlayer((Player) event.getWhoClicked());
        if (team == null)
            return;

        event.setCancelled(true);
    }
}
