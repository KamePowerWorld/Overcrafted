package quarri6343.overcrafted.impl.block;

import org.bukkit.block.Block;

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

    public void cancelProcessing(Block block, boolean removeFromMap);
    
    public void cancelAllProcesses();
}
