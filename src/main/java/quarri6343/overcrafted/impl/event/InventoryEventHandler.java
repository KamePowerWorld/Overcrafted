package quarri6343.overcrafted.impl.event;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.api.IOCTeam;
import quarri6343.overcrafted.core.OCLogic;

/**
 * プレイヤーインベントリ関係のイベントハンドラ
 */
public class InventoryEventHandler implements Listener {

    public InventoryEventHandler() {
        Overcrafted.getInstance().getServer().getPluginManager().registerEvents(this, Overcrafted.getInstance());
    }

    private static OCVariableData getData() {
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
