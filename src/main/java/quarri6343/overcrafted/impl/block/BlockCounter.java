package quarri6343.overcrafted.impl.block;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.item.IOCItem;
import quarri6343.overcrafted.api.item.IRightClickEventHandler;
import quarri6343.overcrafted.core.OCBlock;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.core.data.constant.OCCommonData;
import quarri6343.overcrafted.core.data.constant.OCSoundData;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.core.OCLogic;
import quarri6343.overcrafted.core.handler.OrderHandler;
import quarri6343.overcrafted.api.item.ISubmittableOCItem;
import quarri6343.overcrafted.impl.item.OCItems;

/**
 * 皿を提出してスコアにできるブロック
 */
public class BlockCounter extends OCBlock implements IRightClickEventHandler {

    public BlockCounter(Material material) {
        super(material);
    }

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }

    @Override
    public void onRightClick(PlayerInteractEvent event) {
        if(getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;
        
        IOCTeam team = getData().getTeams().getTeamByPlayer(event.getPlayer());
        if (team == null) {
            event.getPlayer().sendMessage(Component.text("あなたはチームに所属していないため、皿を扱うことができません"));
            return;
        }

        trySubmitOrder(event, team);
    }
    
    /**
     * プレイヤーが手に持っている皿をカウンターに提出することを試みる
     */
    private void trySubmitOrder(PlayerInteractEvent event, IOCTeam team) {
        IOCItem item = OCItems.toOCItem(event.getItem());
        if (!(item instanceof ISubmittableOCItem))
            return;

        if (OrderHandler.trySatisfyOrder(team, (ISubmittableOCItem) item)) {
            event.getPlayer().setItemInHand(null);
            
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(getData().getSelectedStage().get().isEnableDishGettingDirty()){
                        team.getDirtyDishPile().addDish();
                    }
                    else{
                        team.getCleanDishPile().addDish();
                    }
                }
            }.runTaskLater(Overcrafted.getInstance(), OCCommonData.dishReturnLag);
            
            for (int i = 0; i < team.getPlayersSize(); i++) {
                team.getPlayer(i).playSound(OCSoundData.submitSound);
            }
        } else {
            event.getPlayer().sendMessage(Component.text("皿に載っているアイテムは誰も注文していないようだ..."));
        }
    }
}
