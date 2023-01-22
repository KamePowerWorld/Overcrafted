package quarri6343.overcrafted.common.event;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.item.interfaces.IOCItem;
import quarri6343.overcrafted.api.item.interfaces.ISupplier;
import quarri6343.overcrafted.common.GlobalTeamHandler;
import quarri6343.overcrafted.common.data.DishPile;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCResourcePackData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.impl.item.OCItems;

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

    @org.bukkit.event.EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        setResourcePack(event);
    }

    /**
     * プレイヤーにリソースパックをダウンロードさせる
     *
     * @param event
     */
    private void setResourcePack(PlayerJoinEvent event) {
        if (OCResourcePackData.packHash != null) {
            event.getPlayer().setResourcePack(OCResourcePackData.packURL, OCResourcePackData.packHash, true, Component.text("リソースパックを適用しないと遊べません"));
        }
    }

    /**
     * playerInteractが発生した時に呼び出されたいハンドラを登録する
     *
     * @param playerInteractEventHandler
     */
    public void registerHandler(IPlayerInteractEventHandler playerInteractEventHandler) {
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
    public void onEntityDeath(EntityDeathEvent event) {
        processDishPileDestruction(event);
    }

    /**
     * もし設置されている皿置場が壊された時、注文箱に処理を委譲する
     *
     * @param event
     */
    private void processDishPileDestruction(EntityDeathEvent event) {
        for (int i = 0; i < getData().teams.getTeamsLength(); i++) {
            OCTeam team = getData().teams.getTeam(i);

            DishPile cleanDishPile = team.cleanDishPile;
            if (event.getEntity().getLocation().getBlock().equals(cleanDishPile.location.getBlock()) && cleanDishPile.isPlaced()) {
                event.setCancelled(true);
                cleanDishPile.destroy();
                return;
            }

            DishPile dirtyDishPile = team.dirtyDishPile;
            if (event.getEntity().getLocation().getBlock().equals(dirtyDishPile.location.getBlock()) && dirtyDishPile.isPlaced()) {
                event.setCancelled(true);
                dirtyDishPile.destroy();
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
        blockInvalidItemDrop(event);
        throwItems(event);
    }

    /**
     * 原料ではないアイテムのドロップを阻止する
     *
     * @param event
     */
    private void blockInvalidItemDrop(PlayerDropItemEvent event) {
        if (event.isCancelled())
            return;

        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return;

        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null)
            return;

        IOCItem ocItem = OCItems.toOCItem(event.getItemDrop().getItemStack());
        if (ocItem != null && ocItem.equals(OCItems.ADMIN_MENU.get()))
            return;
        
        for (OCItems ocItems : OCItems.values()) {
            if (ocItems.get() instanceof ISupplier && event.getItemDrop().getItemStack().getType() == ocItems.get().getItemStack().getType())
                return;
        }

        event.setCancelled(true);

        if (event.getItemDrop().getItemStack().getType() != OCData.invalidItem.getType()) {
            event.getPlayer().sendMessage("原料でないアイテムは捨てられません");
        }
    }

    /**
     * スニークしていない時アイテムを投げる
     * https://github.com/KamePowerWorld/ThrowItemPluginからの流用
     *
     * @param event
     */
    private void throwItems(PlayerDropItemEvent event) {
        if (event.isCancelled())
            return;

        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return;

        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null)
            return;

        if (!event.getPlayer().isSneaking()) {
            Location pLoc = event.getPlayer().getEyeLocation();
            event.getItemDrop().setVelocity(pLoc.getDirection());
        }
        event.getItemDrop().setPickupDelay(OCData.thrownItemsPickupDelay);
    }

    @org.bukkit.event.EventHandler
    public void onPlayerPickUp(PlayerAttemptPickupItemEvent event) {
        blockPickingUpExcessiveItems(event);
    }

    /**
     * プレイヤーがアイテムを1個より多く持たないように拾う量を調整する
     */
    private void blockPickingUpExcessiveItems(PlayerAttemptPickupItemEvent event) {
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return;

        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null)
            return;

        if (event.getPlayer().getInventory().getItem(0) != null) {
            event.setCancelled(true);
            return;
        } else if (event.getItem().getItemStack().getAmount() > 1) {
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
    private void blockSwapHandItem(PlayerSwapHandItemsEvent event) {
        if (getLogic().gameStatus != OCLogic.GameStatus.INACTIVE) {
            event.setCancelled(true);
        }
    }

    @org.bukkit.event.EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        GlobalTeamHandler.removePlayerFromTeam(event.getPlayer(), true);
    }

    @org.bukkit.event.EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        tryPickUpDish(event);
    }

    private void tryPickUpDish(PlayerInteractEntityEvent event) {
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return;

        if (event.getPlayer().getItemInHand().getType() != Material.AIR)
            return;

        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null)
            return;

        if (event.getRightClicked() == team.cleanDishPile.getDishPileEntity()) {
            event.setCancelled(true);
            if (team.cleanDishPile.removeDish())
                event.getPlayer().setItemInHand(OCItems.DISH.get().getItemStack());
        }

        if (event.getRightClicked() == team.dirtyDishPile.getDishPileEntity()) {
            event.setCancelled(true);
            if (team.dirtyDishPile.removeDish())
                event.getPlayer().setItemInHand(OCItems.DIRTY_DISH.get().getItemStack());
        }
    }
}
