package quarri6343.overcrafted.common.data.interfaces;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public interface IOCTeams {

    /**
     * チームを登録する
     *
     * @param name  チーム名
     * @param color 　チームの色
     */
    @ParametersAreNonnullByDefault
    public void addTeam(@NotNull String name, @NotNull String color);

    /**
     * チームを削除する
     *
     * @param name チーム名
     */
    @ParametersAreNonnullByDefault
    public void removeTeam(String name);

    /**
     * インデックスからチームを取得する
     *
     * @param index チームのインデックス
     * @return チーム
     */
    public @Nonnull
    IOCTeam getTeam(int index);

    /**
     * 名前からチームを取得する
     *
     * @param name チーム名
     * @return チーム
     */
    @ParametersAreNonnullByDefault
    public @Nullable IOCTeam getTeamByName(String name);

    /**
     * 色の文字列からチームを取得する
     *
     * @param color 色
     * @return チーム
     */
    @ParametersAreNonnullByDefault
    public @Nullable IOCTeam getTeamByColor(String color);

    /**
     * プレイヤー名からチームを取得する
     *
     * @param player プレイヤー名
     * @return チーム
     */
    @ParametersAreNonnullByDefault
    public @Nullable IOCTeam getTeamByPlayer(Player player);

    /**
     * チームの数を取得する
     */
    public int getTeamsLength();

    /**
     * チームを全削除する
     */
    public void clearTeam();

    /**
     * チームのプレイヤーを解散させる
     */
    public void disbandTeams(boolean restoreStats);

    /**
     * チームに参加している全てのプレイヤーをカウントする
     *
     * @return 全てのチームのプレイヤー数の合計
     */
    public int countAllPlayers();

    /**
     * 全てのチームの皿置場の中身を消去する
     */
    public void clearDishPile();

    /**
     * 全てのチームのプレイヤーが持ちすぎているアイテムをドロップさせる
     */
    public void clearExcessiveItemsFromAllTeam();

    /**
     * 全てのチームメンバーをチームに加入した位置にテレポートさせる
     */
    public void teleportTeamToLobby();
}
