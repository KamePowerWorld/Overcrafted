package quarri6343.overcrafted.common;

import com.google.common.base.Objects;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.data.OrderBox;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.impl.ui.UIAdminMenu;
import quarri6343.overcrafted.utils.OvercraftedUtils;

public class PlayerEventHandler implements Listener {

    public static final Component menuItemName = Component.text("Overcrafted管理メニュー");

    public PlayerEventHandler() {
        Overcrafted.getInstance().getServer().getPluginManager().registerEvents(this, Overcrafted.getInstance());
    }

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }
    
    private static OCLogic getLogic() { return Overcrafted.getInstance().getLogic(); }
    
    @org.bukkit.event.EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        processHandheldItem(event);
    }

    /**
     * 手持ちのアイテムを識別してそれに対応したguiを開く
     */
    private void processHandheldItem(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND)
            return;

        if (event.getItem() != null) {
            if (event.getItem().getType().equals(Material.STICK)
                    && Objects.equal(event.getItem().getItemMeta().displayName(), menuItemName)) {
                processHandheldAdminMenu(event);
            } else if (event.getItem().getType().equals(Material.PAPER)
                    && DishHandler.isDish(event.getItem())) {
                processHandheldDish(event);
            }
        }
    }

    /**
     * 手持ちの管理メニューを識別してそれに応じたイベントを起こす
     *
     * @param event
     */
    private void processHandheldAdminMenu(PlayerInteractEvent event) {
        if (!event.getPlayer().isOp())
            return;

        UIAdminMenu.openUI(event.getPlayer());
        event.setCancelled(true);
    }

    /**
     * 手持ちの皿を識別してそれに応じたイベントを起こす
     *
     * @param event
     */
    private void processHandheldDish(PlayerInteractEvent event) {
        if(getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;
        
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

        if (DishHandler.isDirty(event.getItem())){
            tryWashDish(event, dishMenu);
            return;
        }

        if (DishHandler.isOrderCompleted(event.getItem())) {
            trySubmitOrder(event, dishMenu);
        } else {
            tryCompleteOrder(event, dishMenu);
        }
    }

    /**
     * プレイヤーが手に持っている皿をカウンターに提出することを試みる
     *
     * @param event
     * @param menu
     */
    private void trySubmitOrder(PlayerInteractEvent event, DishMenu menu) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.RED_BED) {
            OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
            if(team == null){
                event.getPlayer().sendMessage(Component.text("あなたはチームに所属していません"));
                return;
            }
            
            event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
            ScoreBoardHandler.addScore(team);
            team.orderBox.addRandomDirtyDish();
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

        if(event.getClickedBlock() != null && (event.getClickedBlock().getType() == Material.CAULDRON || event.getClickedBlock().getType() == Material.WATER_CAULDRON))
            return;
        
        event.getPlayer().sendMessage(OvercraftedUtils.getItemInfoasText(dishMenu.getProduct()).append(Component.text(" が入ったブロックを皿を持って右クリックしよう")));
    }

    /**
     * プレイヤーが手に持っている汚い皿を洗うことを試みる
     * @param event
     * @param dishMenu
     */
    private void tryWashDish(PlayerInteractEvent event, DishMenu dishMenu){
        if(getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;
        
        if(event.getClickedBlock() == null || !Objects.equal(event.getClickedBlock().getType(), Material.WATER_CAULDRON)){
            event.getPlayer().sendMessage(Component.text("水の入った大釜を右クリックして洗おう"));
            return;
        }

        Levelled cauldronData = (Levelled) event.getClickedBlock().getBlockData();
        if(cauldronData.getLevel() == cauldronData.getMinimumLevel()){
            event.getClickedBlock().getWorld().setType(event.getClickedBlock().getLocation(), Material.CAULDRON);
        }
        else{
            cauldronData.setLevel(cauldronData.getLevel() - 1);
            event.getClickedBlock().setBlockData(cauldronData);
        }
        
        event.getPlayer().setItemInHand(DishHandler.encodeOrder(dishMenu));
        event.getClickedBlock().getState().update();
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
    public void onPlayerDeath(PlayerDeathEvent event){
        removeInvalidItem(event);
    }

    /**
     * プレイヤーが死んだときインベントリに入っている無効アイテムがドロップしないようにする
     * @param event
     */
    private void removeInvalidItem(PlayerDeathEvent event){
        if(getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;
        
        event.getDrops().removeIf(itemStack -> itemStack.getType().equals(OCData.invalidItem.getType()));
    }

    @org.bukkit.event.EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        resetPlayerStatus(event);
    }

    /**
     * プレイヤーがリスポーンした時状態をリセットする
     * @param event
     */
    private void resetPlayerStatus(PlayerRespawnEvent event){
        if(getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;

        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if(team == null)
            return;
        team.setUpGameEnvforPlayer(event.getPlayer());
    }

    @org.bukkit.event.EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        blockInvalidSlotDrop(event);
    }

    /**
     * スロットをロックしているアイテムのドロップを阻止する
     * @param event
     */
    private void blockInvalidSlotDrop(PlayerDropItemEvent event) {
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE
                || getLogic().gameStatus == OCLogic.GameStatus.BEGINNING)
            return;

        if (event.getItemDrop().getItemStack().getType() == OCData.invalidItem.getType())
            event.setCancelled(true);
    }
}
