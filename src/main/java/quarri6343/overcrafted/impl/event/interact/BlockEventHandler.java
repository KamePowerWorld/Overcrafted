package quarri6343.overcrafted.impl.event.interact;

import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.IPlayerInteractEventHandler;
import quarri6343.overcrafted.api.block.IOCBlock;
import quarri6343.overcrafted.api.item.ILeftClickEventHandler;
import quarri6343.overcrafted.api.item.IRightClickEventHandler;
import quarri6343.overcrafted.impl.block.OCBlocks;

/**
 * 全てのOCBlocksクラスに右クリックと左クリックのイベントを渡す
 */
public class BlockEventHandler implements Listener, IPlayerInteractEventHandler {
    
    public BlockEventHandler(){
        Overcrafted.getInstance().getPlayerEventHandler().registerHandler(this);
    }
    
    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getClickedBlock() ==null)
            return;
        
        IOCBlock clickedOCBlock = OCBlocks.toOCBlock(event.getClickedBlock());

        if (clickedOCBlock != null) {
            Action action = event.getAction();

            if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
                if(clickedOCBlock instanceof IRightClickEventHandler)
                    ((IRightClickEventHandler) clickedOCBlock).onRightClick(event);
            } else if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
                if(clickedOCBlock instanceof ILeftClickEventHandler)
                    ((ILeftClickEventHandler) clickedOCBlock).onLeftClick(event);
            }
        }
    }
}
