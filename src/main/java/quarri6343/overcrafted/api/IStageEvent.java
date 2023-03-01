package quarri6343.overcrafted.api;

import org.bukkit.World;

/**
 * ステージに発生するギミック
 */
public interface IStageEvent {

    /**
     * イベントの名前を取得する
     * @return イベントの名前
     */
    public String getEventName();
    
    /**
     * ステージが開始した時に呼び出される
     */
    public void onStart(World world);

    /**
     * ステージが進行中である時に呼び出される
     * @param count 現在の経過時間
     */
    public void onTick(int count);

    /**
     * ステージが終了した時に呼び出される
     */
    public void onEnd();
}
