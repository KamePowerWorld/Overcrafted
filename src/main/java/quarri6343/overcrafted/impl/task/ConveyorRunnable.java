package quarri6343.overcrafted.impl.task;

import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import quarri6343.overcrafted.core.handler.PlaceItemHandler;
import quarri6343.overcrafted.impl.block.BlockConveyor;

public class ConveyorRunnable extends BukkitRunnable {
    
    BlockConveyor blockConveyor;
    Block conveyorBlock;
    
    public ConveyorRunnable(BlockConveyor blockConveyor, Block conveyorBlock){
        this.blockConveyor = blockConveyor;
        this.conveyorBlock = conveyorBlock;
    }
    
    @Override
    public void run() {
        ItemStack itemOnConveyor = PlaceItemHandler.getItem(conveyorBlock);
        Block nextBlock = conveyorBlock.getRelative(((Directional)conveyorBlock.getBlockData()).getFacing().getOppositeFace());
        if(nextBlock.getType() != conveyorBlock.getType())
            return;
        
        if(PlaceItemHandler.placeItem(nextBlock,itemOnConveyor)){
            blockConveyor.onPlace.forEach(blockConsumer -> blockConsumer.accept(nextBlock));
            PlaceItemHandler.pickUpItem(conveyorBlock);
            blockConveyor.onPickUp.forEach(blockConsumer -> blockConsumer.accept(conveyorBlock, null));
        }
    }
}
