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
     *
     * @param block アイテムが加工されているブロック
     * @return 処理可能かどうか
     */
    public boolean canProcess(Block block);

    /**
     * アイテムの加工を1tick分進める
     *
     * @param block アイテムが加工されているブロック
     */
    public void continueProcessing(Block block);

    /**
     * アイテムの加工をキャンセルし、（もしいれば）プレイヤーに現在の加工状況が保存されたアイテムを渡す
     * (現在机からアイテムを拾った時のイベントを上書きするのに用いられている)
     * 
     * @param block アイテムが加工されているブロック
     * @param player 加工をキャンセルしたプレイヤー(いなくても可)
     * @param removeFromMap ブロックに保存されている加工の進捗をリセットするかどうか
     */
    public void cancelProcessing(Block block, Player player, boolean removeFromMap);

    /**
     * 全てのブロックの加工をキャンセルする
     */
    public void cancelAllProcesses();
    
    public String getProgressionNBTID();
    
    public Sound getProcessingSound();
    
    public Particle getProcessingParticle();
}
