package quarri6343.overcrafted.impl.task.game;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.block.IBlockProcessor;
import quarri6343.overcrafted.api.item.IRightClickEventHandler;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.api.block.IOCBlock;
import quarri6343.overcrafted.api.block.ISneakEventHandler;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.core.data.constant.OCCommonData;
import quarri6343.overcrafted.core.handler.PlaceItemHandler;
import quarri6343.overcrafted.core.handler.order.BossBarHandler;
import quarri6343.overcrafted.core.handler.order.ScoreBoardHandler;
import quarri6343.overcrafted.impl.block.BlockAutomaticProcessor;
import quarri6343.overcrafted.impl.block.BlockManualProcessor;
import quarri6343.overcrafted.impl.block.BlockTable;
import quarri6343.overcrafted.impl.block.OCBlocks;

import java.util.List;
import java.util.function.Consumer;

import static quarri6343.overcrafted.core.data.constant.OCCommonData.blockEventTriggerRange;

/**
 * 定期的に起動してゲームの状態を監視するrunnable
 */
public class GameActiveTask extends BukkitRunnable {

    private int count = 0;
    private final Consumer<IOCTeam> onGameSuccess;

    public GameActiveTask(Consumer<IOCTeam> onGameSuccess) {
        this.onGameSuccess = onGameSuccess;
    }

    @Override
    public void run() {
        count++;

        if (count % 20 == 0) {
            BossBarHandler.updateRemainingTime( ((float) getData().getSelectedStage().get().getTime() * 20 - (float)count) / ((float) getData().getSelectedStage().get().getTime() * 20));
            snapDroppedItemsOnTable();
        }

        if (count >= getData().getSelectedStage().get().getTime() * 20) {
            onGameSuccess.accept(ScoreBoardHandler.getHighestScoreTeam());
            cancel();
        }

        if (count % OCCommonData.checkInventoryInterval == 0) {
            getData().getTeams().clearExcessiveItemsFromAllTeam();
        }

        if(getData().getSelectedStage().get().getEvent() != null)
            getData().getSelectedStage().get().getEvent().onTick(count);

        triggerSneakEvent();
    }

    private void triggerSneakEvent() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(!onlinePlayer.isSneaking())
                continue;
            
            if (getData().getTeams().getTeamByPlayer(onlinePlayer) != null) {
                for (int i = -blockEventTriggerRange; i < blockEventTriggerRange + 1; i++) {
                    for (int j = -blockEventTriggerRange; j < blockEventTriggerRange + 1; j++) {
                        for (int k = -blockEventTriggerRange; k < blockEventTriggerRange + 1; k++) {
                            Block block = onlinePlayer.getLocation().getBlock().getRelative(i, j, k);
                            IOCBlock ocBlock = OCBlocks.toOCBlock(block);
                            if (ocBlock instanceof ISneakEventHandler)
                                ((ISneakEventHandler) ocBlock).whileSneaking(block);
                        }
                    }
                }
            }
        }
    }
    
    public void setCount(int count){
        if(count < 0 || count > getData().getSelectedStage().get().getTime() * 20){
            throw new IllegalArgumentException();
        }
        this.count = count;
    }

    private OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }

    /**
     * ドロップアイテムを下のブロックに干渉させる
     */
    private void snapDroppedItemsOnTable(){
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (getData().getTeams().getTeamByPlayer(onlinePlayer) != null) {
                List<Entity> entityList = onlinePlayer.getNearbyEntities(30,30,30);
                List<Entity> droppedItemList =  entityList.stream().filter(entity -> entity.getType() == EntityType.DROPPED_ITEM && entity.isOnGround()).toList();
                
                for (Entity droppedItem: droppedItemList) {
                    IOCBlock blockBeneathItem = OCBlocks.toOCBlock(droppedItem.getLocation().getBlock().getRelative(BlockFace.DOWN));
                    if(!(blockBeneathItem instanceof BlockTable)){
                        return;
                    }

                    if(PlaceItemHandler.placeItem(droppedItem.getLocation().getBlock().getRelative(BlockFace.DOWN), ((Item)droppedItem).getItemStack())){
                        droppedItem.remove();
                        
                        if(blockBeneathItem instanceof IBlockProcessor){
                            ((IBlockProcessor) blockBeneathItem).continueProcessing(droppedItem.getLocation().getBlock().getRelative(BlockFace.DOWN));
                        }
                    }
                }
            }
        }
    }
}
