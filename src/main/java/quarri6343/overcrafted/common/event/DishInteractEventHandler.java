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
import quarri6343.overcrafted.common.DishHandler;
import quarri6343.overcrafted.common.data.DishMenu;
import quarri6343.overcrafted.common.ScoreBoardHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.utils.OvercraftedUtils;

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
        
        if (!(event.getItem() != null && event.getItem().getType().equals(Material.PAPER)
                && DishHandler.isDish(event.getItem())))
            return;

        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;

        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null) {
            event.getPlayer().sendMessage(Component.text("あなたはチームに所属していないため、料理することができません"));
            return;
        }

        event.setCancelled(true);

        if (event.getItem().getAmount() != 1) {
            event.getPlayer().sendMessage(Component.text("皿は一枚ずつ持ってください"));
            return;
        }

        DishMenu dishMenu = DishHandler.decodeOrder(event.getItem());
        if (dishMenu == null) {
            event.getPlayer().sendMessage(Component.text("この皿には何も注文票が載っていない..."));
            return;
        }

        if (DishHandler.isDirty(event.getItem())) {
            tryWashDish(event, dishMenu);
            return;
        }

        if (DishHandler.isOrderCompleted(event.getItem())) {
            trySubmitOrder(event, team, dishMenu);
        } else {
            tryCompleteOrder(event, dishMenu);
        }
    }


    /**
     * プレイヤーが手に持っている皿をカウンターに提出することを試みる
     *
     * @param event
     * @param team
     */
    private void trySubmitOrder(PlayerInteractEvent event, OCTeam team, DishMenu menu) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.RED_BED) {
            if(team.orderBox.addItem(DishHandler.encodeRandomOrderOnDirtyDish())){
                event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
                ScoreBoardHandler.addScore(team, menu.getScore());
            }
            else{
                event.getPlayer().sendMessage(Component.text("注文箱が一杯だ!"));
            }

        } else {
            event.getPlayer().sendMessage(Component.text("赤いベッドを右クリックして納品しよう"));
        }
    }

    /**
     * プレイヤーが手に持っている皿の注文を満たすことを試みる
     *
     * @param event
     * @param dishMenu
     */
    private void tryCompleteOrder(PlayerInteractEvent event, DishMenu dishMenu) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Container) {
            Inventory inventory = ((Container) event.getClickedBlock().getState()).getInventory();
            if (inventory.containsAtLeast(dishMenu.getProduct(), dishMenu.getProduct().getAmount())) {
                inventory.removeItemAnySlot(dishMenu.getProduct());
                event.getPlayer().setItemInHand(DishHandler.encodeOrderAsCompleted(dishMenu));
            }
            return;
        }

        if (event.getClickedBlock() != null && (event.getClickedBlock().getType() == Material.CAULDRON || event.getClickedBlock().getType() == Material.WATER_CAULDRON))
            return;

        event.getPlayer().sendMessage(OvercraftedUtils.getItemInfoasText(dishMenu.getProduct()).append(Component.text(" が入ったブロックを皿を持って右クリックしよう")));
    }

    /**
     * プレイヤーが手に持っている汚い皿を洗うことを試みる
     *
     * @param event
     * @param dishMenu
     */
    private void tryWashDish(PlayerInteractEvent event, DishMenu dishMenu) {
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

        event.getPlayer().setItemInHand(DishHandler.encodeOrder(dishMenu));
        event.getClickedBlock().getState().update();
    }
}
