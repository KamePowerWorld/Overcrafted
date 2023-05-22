package quarri6343.overcrafted.impl.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.impl.task.ConveyorRunnable;

import java.util.HashMap;
import java.util.Map;

public class BlockConveyor extends BlockTable {

    /**
     * コンベヤとそのタスクのマップ
     */
    private static final Map<Block, BukkitTask> progressionMap = new HashMap<>();

    public BlockConveyor() {
        super(Material.MAGENTA_GLAZED_TERRACOTTA);
        onPlace.add(block -> progressionMap.put(block, new ConveyorRunnable(this, block).runTaskTimer(Overcrafted.getInstance(), 20, 20)));
        onPickUp.add((block, player) -> {
            if(progressionMap.get(block) != null){
                progressionMap.get(block).cancel();
                progressionMap.remove(block);
            }
        });
    }
}
