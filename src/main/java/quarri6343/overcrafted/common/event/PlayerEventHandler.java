package quarri6343.overcrafted.common.event;

import it.unimi.dsi.fastutil.Pair;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.item.interfaces.ICombinedOCItem;
import quarri6343.overcrafted.api.item.interfaces.IOCItem;
import quarri6343.overcrafted.api.item.interfaces.ISupplier;
import quarri6343.overcrafted.common.GlobalTeamHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCResourcePackData;
import quarri6343.overcrafted.common.data.interfaces.IDishPile;
import quarri6343.overcrafted.common.data.interfaces.IOCTeam;
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
    public void onHangingBreak(HangingBreakEvent event) {
        stopItemFrameDeathWhileGameIsActive(event);
    }

    /**
     * バグを起こさないためゲーム中アイテムフレームの破壊を阻止する
     *
     * @param event
     */
    private void stopItemFrameDeathWhileGameIsActive(HangingBreakEvent event) {
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return;

        event.setCancelled(true);
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

        IOCTeam team = getData().getTeams().getTeamByPlayer(event.getPlayer());
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

        IOCTeam team = getData().getTeams().getTeamByPlayer(event.getPlayer());
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

        IOCTeam team = getData().getTeams().getTeamByPlayer(event.getPlayer());
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

        IOCTeam team = getData().getTeams().getTeamByPlayer(event.getPlayer());
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
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return;
        event.setCancelled(true);
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

    /**
     * 皿のエンティティがクリックされた時皿を拾うことを試みる
     */
    private void tryPickUpDish(PlayerInteractEntityEvent event) {
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return;

        IOCTeam team = getData().getTeams().getTeamByPlayer(event.getPlayer());
        if (team == null)
            return;

        if (event.getPlayer().getItemInHand().getType() != Material.AIR) {
            IOCItem ocItem1 = OCItems.toOCItem(event.getPlayer().getItemInHand());
            IOCItem ocItem2;
            if (event.getRightClicked() == team.getCleanDishPile().getDishPileEntity()) {
                ocItem2 = OCItems.DISH.get();
            } else {
                ocItem2 = OCItems.DIRTY_DISH.get();
            }

            for (OCItems ocItem : OCItems.values()) {
                if (!(ocItem.get() instanceof ICombinedOCItem)) {
                    continue;
                }

                Pair<OCItems, OCItems> ingredients = ((ICombinedOCItem) ocItem.get()).getIngredients();
                if ((ingredients.left().get().equals(ocItem1) && ingredients.right().get().equals(ocItem2))
                        || (ingredients.left().get().equals(ocItem2) && ingredients.right().get().equals(ocItem1))) {
                    if (event.getRightClicked() == team.getCleanDishPile().getDishPileEntity()) {
                        if (team.getCleanDishPile().removeDish())
                            event.getPlayer().setItemInHand(ocItem.get().getItemStack());
                    } else {
                        if (team.getDirtyDishPile().removeDish())
                            event.getPlayer().setItemInHand(ocItem.get().getItemStack());
                    }
                    return;
                }
            }
            return;
        }

        if (event.getRightClicked() == team.getCleanDishPile().getDishPileEntity()) {
            event.setCancelled(true);
            if (team.getCleanDishPile().removeDish())
                event.getPlayer().setItemInHand(OCItems.DISH.get().getItemStack());
        }

        if (event.getRightClicked() == team.getDirtyDishPile().getDishPileEntity()) {
            event.setCancelled(true);
            if (team.getDirtyDishPile().removeDish())
                event.getPlayer().setItemInHand(OCItems.DIRTY_DISH.get().getItemStack());
        }
    }

    @org.bukkit.event.EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        stopBlockBreakWhileGameIsActive(event);
    }

    /**
     * バグを起こさないためゲーム中のブロック破壊を阻止する
     */
    private void stopBlockBreakWhileGameIsActive(BlockBreakEvent event) {
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return;

        event.getPlayer().sendMessage(Component.text("ゲーム中はブロックを破壊できません！"));
        event.setCancelled(true);
    }
}
