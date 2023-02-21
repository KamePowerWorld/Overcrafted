package quarri6343.overcrafted.common.event;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.interfaces.IOCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;

/**
 * プレイヤーインベントリ関係のイベントハンドラ
 */
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
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return;

        IOCTeam team = getData().getTeams().getTeamByPlayer((Player) event.getPlayer());
        if (team == null)
            return;
        
        if(event.getInventory().getType() == InventoryType.PLAYER || event.getInventory().getType() == InventoryType.CHEST){
            return;
        }

        event.setCancelled(true);
        event.getPlayer().sendActionBar(Component.text("ゲーム中はコンテナを開けません！"));
    }
}
