package quarri6343.overcrafted.api.block;

import net.kyori.adventure.sound.Sound;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * アイテムを別のブロックに加工できるブロック
 */
public interface IBlockProcessor {

    /**
     * アイテムが載せられて処理可能な状態であるかを確認する
     * @param block
     * @return
     */
    public boolean canProcess(Block block);

    public void continueProcessing(Block block);

    public void cancelProcessing(Block block, Player player, boolean removeFromMap);
    
    public void cancelAllProcesses();
    
    public String getProgressionNBTID();
    
    public Sound getProcessingSound();
    
    public Particle getProcessingParticle();
}
