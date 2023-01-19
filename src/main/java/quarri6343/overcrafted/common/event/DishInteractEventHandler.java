package quarri6343.overcrafted.common.event;

import com.google.common.base.Objects;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.order.OrderHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;

/**
 * 手持ちの皿を識別してそれに応じたイベントを起こす
 */
public class DishInteractEventHandler implements IPlayerInteractEventHandler {
    
    public DishInteractEventHandler(){
        Overcrafted.getInstance().getPlayerEventHandler().registerHandler(this);
    }

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.isCancelled())
            return;
        
        if (!(event.getItem() != null && OrderHandler.isDish(event.getItem())))
            return;

        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;

        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null) {
            event.getPlayer().sendMessage(Component.text("あなたはチームに所属していないため、皿を扱うことができません"));
            return;
        }

        event.setCancelled(true);

        if (event.getItem().getAmount() != 1) {
            event.getPlayer().sendMessage(Component.text("皿は一枚ずつ持ってください"));
            return;
        }

        if (OrderHandler.isDirty(event.getItem())) {
            tryWashDish(event);
            return;
        }

        Material material = OrderHandler.decodeOrder(event.getItem());
        if (material != null) {
            trySubmitOrder(event, team);
        } else {
            tryPutItemOnDish(event);
        }
    }


    /**
     * プレイヤーが手に持っている皿をカウンターに提出することを試みる
     */
    private void trySubmitOrder(PlayerInteractEvent event, OCTeam team) {
        Material material = OrderHandler.decodeOrder(event.getItem());
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.RED_BED) {
            if(OrderHandler.trySatisfyOrder(team, material)){
                event.getPlayer().setItemInHand(OrderHandler.getDirtyDish());
            }
            else{
                event.getPlayer().sendMessage(Component.text("皿に載っているアイテムは誰も注文していないようだ..."));
            }
        } else {
            event.getPlayer().sendMessage(Component.text("赤いベッドを右クリックして納品しよう"));
        }
    }

    /**
     * プレイヤーが手に持っている皿の注文を満たすことを試みる
     *
     * @param event
     */
    private void tryPutItemOnDish(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Container) {
            Inventory inventory = ((Container) event.getClickedBlock().getState()).getInventory();
            for (int i = 0; i < inventory.getSize(); i++) {
                if(inventory.getItem(i) == null)
                    continue;
                
                ItemStack newDish = OrderHandler.tryEncodeOrderOnDish(inventory.getItem(i).getType());
                if(newDish != null){
                    inventory.setItem(i, null);
                    event.getPlayer().setItemInHand(newDish);
                }
            }
            return;
        }

        if (event.getClickedBlock() != null && (event.getClickedBlock().getType() == Material.CAULDRON || event.getClickedBlock().getType() == Material.WATER_CAULDRON))
            return;

        event.getPlayer().sendMessage(Component.text(" 納品したいアイテムが入ったブロックを皿を持って右クリックしよう"));
    }

    /**
     * プレイヤーが手に持っている汚い皿を洗うことを試みる
     *
     * @param event
     */
    private void tryWashDish(PlayerInteractEvent event) {
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;

        if (event.getClickedBlock() == null || !Objects.equal(event.getClickedBlock().getType(), Material.WATER_CAULDRON)) {
            event.getPlayer().sendMessage(Component.text("水の入った大釜を右クリックして洗おう"));
            return;
        }

        Levelled cauldronData = (Levelled) event.getClickedBlock().getBlockData();
        if (cauldronData.getLevel() == cauldronData.getMinimumLevel()) {
            event.getClickedBlock().getWorld().setType(event.getClickedBlock().getLocation(), Material.CAULDRON);
        } else {
            cauldronData.setLevel(cauldronData.getLevel() - 1);
            event.getClickedBlock().setBlockData(cauldronData);
        }

        event.getPlayer().setItemInHand(OrderHandler.getDish());
        event.getClickedBlock().getState().update();
    }
}
