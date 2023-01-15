package quarri6343.overcrafted.common.event;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.data.OrderBox;
import quarri6343.overcrafted.common.logic.OCLogic;

import java.util.ArrayList;
import java.util.List;

public class PlayerEventHandler implements Listener {
    
    private final List<IPlayerInteractEventHandler> playerInteractEventHandlers = new ArrayList<>();

    public PlayerEventHandler() {
        Overcrafted.getInstance().getServer().getPluginManager().registerEvents(this, Overcrafted.getInstance());
    }

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }

    /**
     * playerInteractが発生した時に呼び出されたいハンドラを登録する
     * @param playerInteractEventHandler
     */
    public void registerHandler(IPlayerInteractEventHandler playerInteractEventHandler){
        playerInteractEventHandlers.add(playerInteractEventHandler);
    }
    
    @org.bukkit.event.EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        processHandheldItem(event);
    }

    /**
     * 手持ちのアイテムの処理を登録されているクラスに移譲する
     */
    private void processHandheldItem(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND)
            return;

        for (IPlayerInteractEventHandler playerInteractEventHandler : playerInteractEventHandlers) {
            playerInteractEventHandler.onPlayerInteract(event);
        }
    }

    @org.bukkit.event.EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        processOrderBoxDestruction(event);
    }

    /**
     * もし設置されている注文箱が壊された時、注文箱に処理を委譲する
     *
     * @param event
     */
    private void processOrderBoxDestruction(BlockBreakEvent event) {
        for (int i = 0; i < getData().teams.getTeamsLength(); i++) {
            OCTeam team = getData().teams.getTeam(i);

            OrderBox orderBox = team.orderBox;
            if (event.getBlock().equals(orderBox.location.getBlock()) && orderBox.isPlaced()) {
                event.setCancelled(true);
                orderBox.destroy();
                return;
            }
        }
    }

    @org.bukkit.event.EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        removeInvalidItem(event);
    }

    /**
     * プレイヤーが死んだときインベントリに入っている無効アイテムがドロップしないようにする
     *
     * @param event
     */
    private void removeInvalidItem(PlayerDeathEvent event) {
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;
        
        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null)
            return;
        
        event.getDrops().removeIf(itemStack -> itemStack.getType().equals(OCData.invalidItem.getType()));
    }

    @org.bukkit.event.EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        resetPlayerStatus(event);
    }

    /**
     * プレイヤーがリスポーンした時状態をリセットする
     *
     * @param event
     */
    private void resetPlayerStatus(PlayerRespawnEvent event) {
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;

        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null)
            return;
        
        team.setUpGameEnvforPlayer(event.getPlayer());
    }

    @org.bukkit.event.EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        blockInvalidSlotDrop(event);
    }

    /**
     * スロットをロックしているアイテムのドロップを阻止する
     *
     * @param event
     */
    private void blockInvalidSlotDrop(PlayerDropItemEvent event) {
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return;
        
        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null)
            return;

        if (event.getItemDrop().getItemStack().getType() == OCData.invalidItem.getType())
            event.setCancelled(true);
    }

    @org.bukkit.event.EventHandler
    public void onPlayerPickUp(PlayerAttemptPickupItemEvent event) {
        blockPickingUpExcessiveItems(event);
    }

    /**
     * プレイヤーがアイテムを1個より多く持たないように拾う量を調整する
     */
    private void blockPickingUpExcessiveItems(PlayerAttemptPickupItemEvent event){
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return;

        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null)
            return;
        
        if(event.getPlayer().getInventory().getItem(0) != null){
            event.setCancelled(true);
            return;
        }
        else if(event.getItem().getItemStack().getAmount() > 1){
            ItemStack itemOnGround = event.getItem().getItemStack();
            itemOnGround.setAmount(itemOnGround.getAmount() - 1);
            event.getItem().setItemStack(itemOnGround);
            event.setCancelled(true);
            itemOnGround.setAmount(1);
            event.getPlayer().getInventory().addItem(itemOnGround);
        }
    }

    @org.bukkit.event.EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        blockSwapHandItem(event);
    }

    /**
     * ゲーム中メインハンドとオフハンドを入れ替えることを禁止する
     */
    private void blockSwapHandItem(PlayerSwapHandItemsEvent event){
        if(getLogic().gameStatus != OCLogic.GameStatus.INACTIVE){
            event.setCancelled(true);
        }
    }
}
