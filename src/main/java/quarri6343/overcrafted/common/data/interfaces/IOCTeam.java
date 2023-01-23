package quarri6343.overcrafted.common.data.interfaces;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Overcraftedを共に攻略するチームのデータ
 */
public interface IOCTeam {

    public String getName();

    public String getColor();

    public Location getStartLocation();

    public void setStartLocation(Location location);

    public Location getJoinLocation1();

    public void setJoinLocation1(Location location);

    public Location getJoinLocation2();

    public void setJoinLocation2(Location location);

    public IDishPile getCleanDishPile();

    public IDishPile getDirtyDishPile();

    /**
     * チームに所属しているプレイヤーの環境をゲーム開始に適した状態に変更する
     *
     * @param player チームに所属しているプレイヤー
     */
    public void setUpGameEnvforPlayer(Player player);

    /**
     * チームにプレイヤーを追加
     *
     * @param player プレイヤーエンティティ
     */
    public void addPlayer(Player player);

    /**
     * チームのプレイヤーを取得
     *
     * @param index プレイヤーのインデックス
     * @return プレイヤーエンティティ
     */
    public Player getPlayer(int index);

    /**
     * チームからプレイヤーを削除
     *
     * @param player       プレイヤーエンティティ
     * @param restoreStats プレイヤーの所持品をチーム参加前に復元するかどうか
     */
    public void removePlayer(Player player, boolean restoreStats);

    /**
     * チームから全てのプレイヤーを削除
     *
     * @param restoreStats
     */
    public void removeAllPlayer(boolean restoreStats);

    /**
     * チームのプレイヤーのサイズを取得
     *
     * @return
     */
    public int getPlayersSize();

    /**
     * チームにプレイヤーが含まれるか取得
     *
     * @param player プレイヤーエンティティ
     * @return チームにプレイヤーが含まれるか
     */
    public boolean containsPlayer(Player player);

    /**
     * チームの全ての所属プレイヤーの名前をテキストにする
     *
     * @return テキストのリスト
     */
    public List<TextComponent> playerNamesToText();

    /**
     * チームの全てのプレイヤーが過剰に持っている所持品をドロップさせる
     */
    public void clearExcessiveItemsFromAllPlayers();

    /**
     * チームの全てのプレイヤーを参加地点に飛ばす
     */
    public void teleportPlayerToLobby();
}
