package quarri6343.overcrafted.impl.block;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.block.OCBlock;
import quarri6343.overcrafted.api.item.interfaces.IRightClickEventHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCSoundData;
import quarri6343.overcrafted.common.data.interfaces.IOCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.impl.ui.UISupplyMenu;

/**
 * アイテムをプレイヤーに供給するブロック
 */
public class BlockSupplier extends OCBlock implements IRightClickEventHandler {

    public BlockSupplier() {
        super(Material.OBSERVER);
    }

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }
    
    @Override
    public void onRightClick(PlayerInteractEvent event) {
        if (event.isCancelled())
            return;

        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;

        IOCTeam team = getData().getTeams().getTeamByPlayer(event.getPlayer());
        if (team == null)
            return;

        event.setCancelled(true);

        if (!event.getAction().isRightClick()) {
            return;
        }

        event.getPlayer().playSound(OCSoundData.openSupplierSound);
        UISupplyMenu.openUI(event.getPlayer());
    }
}
