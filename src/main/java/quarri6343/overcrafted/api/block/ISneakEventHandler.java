package quarri6343.overcrafted.api.block;

import org.bukkit.block.Block;

/**
 * これを実装した時プレイヤーがブロックの近くでスニークした時のイベントを受け取れる
 * イベントが発動する距離はデータクラスで設定可能
 */
public interface ISneakEventHandler {

    /**
     * 誰かプレイヤーがスニークしている間毎tick起きるイベント
     * @param block スニークイベントが発動したブロック
     */
    void whileSneaking(Block block);
}
