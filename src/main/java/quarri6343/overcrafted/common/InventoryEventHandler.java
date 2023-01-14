package quarri6343.overcrafted.common;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
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
        blockInvalidSlotInteraction(event);
    }

    /**
     * ロックされているスロットへの干渉を阻止する
     *
     * @param event
     */
    private void blockInvalidSlotInteraction(InventoryClickEvent event) {
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return;

        if(!(event.getWhoClicked() instanceof Player))
            return;
        
        OCTeam team = getData().teams.getTeambyPlayer((Player) event.getWhoClicked());
        if (team == null)
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

    @org.bukkit.event.EventHandler
    public void onInventoryMove(InventoryMoveItemEvent event) {
        blockInvalidSlotInteraction(event);
    }

    private void blockInvalidSlotInteraction(InventoryMoveItemEvent event) {
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return;
        
        if (event.getSource().getType() != InventoryType.PLAYER && event.getDestination().getType() != InventoryType.PLAYER)
            return;

        if (event.getItem().getType() == OCData.invalidItem.getType())
            event.setCancelled(true);
    }
}
