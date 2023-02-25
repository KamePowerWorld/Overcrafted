package quarri6343.overcrafted.impl.event;

import it.unimi.dsi.fastutil.Pair;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.IPlayerInteractEventHandler;
import quarri6343.overcrafted.api.item.ICombinedOCItem;
import quarri6343.overcrafted.api.item.IOCItem;
import quarri6343.overcrafted.api.item.ISupplier;
import quarri6343.overcrafted.core.handler.GlobalTeamHandler;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.core.data.constant.OCCommonData;
import quarri6343.overcrafted.core.data.constant.OCResourcePackData;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.core.OCLogic;
import quarri6343.overcrafted.impl.item.OCItems;
import quarri6343.overcrafted.utils.OverCraftedUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * プレイヤーに起こるイベントをキャッチするクラス
 */
public class PlayerEventHandler implements Listener {

    private final List<IPlayerInteractEventHandler> playerInteractEventHandlers = new ArrayList<>();

    public PlayerEventHandler() {
        Overcrafted.getInstance().getServer().getPluginManager().registerEvents(this, Overcrafted.getInstance());
    }

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }

    private static int getStageID(){
        return getData().getSelectedStage().ordinal();
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

        Location respawnLocation = team.initializeRespawnedPlayer(event.getPlayer(), getData().getSelectedStage().ordinal());
        event.setRespawnLocation(respawnLocation);
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

        event.getPlayer().sendActionBar(Component.text("原料でないアイテムは捨てられません"));
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
        event.getItemDrop().setPickupDelay(OCCommonData.thrownItemsPickupDelay);
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
        if(tryPickUpDish(event))
            return;
        
        blockEntityInteraction(event);
    }

    /**
     * 皿のエンティティがクリックされた時皿を拾うことを試みる
     */
    private boolean tryPickUpDish(PlayerInteractEntityEvent event) {
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return false;

        IOCTeam team = getData().getTeams().getTeamByPlayer(event.getPlayer());
        if (team == null)
            return false;

        if (event.getPlayer().getItemInHand().getType() != Material.AIR) {
            IOCItem ocItem1 = OCItems.toOCItem(event.getPlayer().getItemInHand());
            IOCItem ocItem2;
            if (event.getRightClicked() == team.getCleanDishPiles().get(getStageID()).getDishPileEntity()) {
                ocItem2 = OCItems.DISH.get();
            } else if(event.getRightClicked() == team.getDirtyDishPiles().get(getStageID()).getDishPileEntity()) {
                ocItem2 = OCItems.DIRTY_DISH.get();
            } else{
                return false;
            }

            for (OCItems ocItem : OCItems.values()) {
                if (!(ocItem.get() instanceof ICombinedOCItem)) {
                    continue;
                }

                Pair<OCItems, OCItems> ingredients = ((ICombinedOCItem) ocItem.get()).getIngredients();
                if ((ingredients.left().get().equals(ocItem1) && ingredients.right().get().equals(ocItem2))
                        || (ingredients.left().get().equals(ocItem2) && ingredients.right().get().equals(ocItem1))) {
                    event.setCancelled(true);
                    if (event.getRightClicked() == team.getCleanDishPiles().get(getStageID()).getDishPileEntity()) {
                        if (team.getCleanDishPiles().get(getStageID()).removeDish())
                            event.getPlayer().setItemInHand(ocItem.get().getItemStack());
                    } else {
                        if (team.getDirtyDishPiles().get(getStageID()).removeDish())
                            event.getPlayer().setItemInHand(ocItem.get().getItemStack());
                    }
                    return true;
                }
            }
            return true;
        }
        
        if(OverCraftedUtil.getInventoryItemCount(event.getPlayer().getInventory()) > 0)
            return false;

        if (event.getRightClicked() == team.getCleanDishPiles().get(getStageID()).getDishPileEntity()) {
            event.setCancelled(true);
            if (team.getCleanDishPiles().get(getStageID()).removeDish())
                event.getPlayer().setItemInHand(OCItems.DISH.get().getItemStack());
            return true;
        }

        if (event.getRightClicked() == team.getDirtyDishPiles().get(getStageID()).getDishPileEntity()) {
            event.setCancelled(true);
            if (team.getDirtyDishPiles().get(getStageID()).removeDish())
                event.getPlayer().setItemInHand(OCItems.DIRTY_DISH.get().getItemStack());
            return true;
        }
        
        return false;
    }

    /**
     * ゲーム中アイテムフレームの回転を含むエンティティへの干渉を阻止する
     * @param event
     */
    private void blockEntityInteraction(PlayerInteractEntityEvent event){
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return;
        
        event.setCancelled(true);
    }

    @org.bukkit.event.EventHandler
    public void onPlayerDamageEntity(EntityDamageByEntityEvent event){
        blockEntityDamage(event);
    }

    /**
     * ゲーム中アイテムフレーム内のアイテム取り出しを含むエンティティへの干渉を阻止する
     * @param event
     */
    private void blockEntityDamage(EntityDamageByEntityEvent event){
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return;

        event.setCancelled(true);
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

        event.getPlayer().sendActionBar(Component.text("ゲーム中はブロックを破壊できません！"));
        event.setCancelled(true);
    }
}
