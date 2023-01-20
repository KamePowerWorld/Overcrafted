package quarri6343.overcrafted.common.data;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

/**
 * チームに対する操作を行うクラス
 */
public class OCTeams {

    /**
     * ゲーム上に存在する全てのチーム
     */
    private final List<OCTeam> teams = new ArrayList<>();

    /**
     * チームを登録する
     *
     * @param name  チーム名
     * @param color 　チームの色
     */
    @ParametersAreNonnullByDefault
    public void addTeam(@NotNull String name, @NotNull String color) {
        if (getTeambyName(name) != null) {
            throw new IllegalArgumentException();
        }

        if (getTeambyName(color) != null) {
            throw new IllegalArgumentException();
        }

        teams.add(new OCTeam(name, color));
    }

    /**
     * チームを削除する
     *
     * @param name チーム名
     */
    @ParametersAreNonnullByDefault
    public void removeTeam(String name) {
        teams.removeIf(team -> team.name.equals(name));
    }

    /**
     * インデックスからチームを取得する
     *
     * @param index チームのインデックス
     * @return チーム
     */
    public @Nonnull
    OCTeam getTeam(int index) {
        return teams.get(index);
    }

    /**
     * 名前からチームを取得する
     *
     * @param name チーム名
     * @return チーム
     */
    @ParametersAreNonnullByDefault
    public @Nullable OCTeam getTeambyName(String name) {
        return teams.stream().filter(v -> v.name.equals(name)).findFirst().orElse(null);
    }

    /**
     * 色の文字列からチームを取得する
     *
     * @param color 色
     * @return チーム
     */
    @ParametersAreNonnullByDefault
    public @Nullable OCTeam getTeambyColor(String color) {
        return teams.stream().filter(v -> v.color.equals(color)).findFirst().orElse(null);
    }

    @ParametersAreNonnullByDefault
    public @Nullable OCTeam getTeambyPlayer(Player player) {
        return teams.stream().filter(v -> v.containsPlayer(player)).findFirst().orElse(null);
    }


    /**
     * チームの数を取得する
     */
    public int getTeamsLength() {
        return teams.size();
    }

    /**
     * チームを全削除する
     */
    public void clearTeam() {
        teams.clear();
    }

    /**
     * チームのプレイヤーを解散させる
     */
    public void disbandTeams(boolean restoreStats) {
        for (int i = 0; i < getTeamsLength(); i++) {
            getTeam(i).removeAllPlayer(restoreStats);
        }
    }

    /**
     * チームに参加している全てのプレイヤーをカウントする
     *
     * @return 全てのチームのプレイヤー数の合計
     */
    public int countAllPlayers() {
        int playerCount = 0;
        for (int i = 0; i < getTeamsLength(); i++) {
            playerCount += getTeam(i).getPlayersSize();
        }

        return playerCount;
    }

    /**
     * 全てのチームの皿置場の中身を消去する
     */
    public void clearDishPile() {
        for (int i = 0; i < getTeamsLength(); i++) {
            getTeam(i).cleanDishPile.destroy();
        }
    }

    /**
     * 全てのチームのプレイヤーが持ちすぎているアイテムをドロップさせる
     */
    public void clearExcessiveItemsFromAllTeam() {
        for (int i = 0; i < getTeamsLength(); i++) {
            getTeam(i).clearExcessiveItemsFromAllPlayers();
        }
    }

    /**
     * 全てのチームメンバーをチームに加入した位置にテレポートさせる
     */
    public void teleportTeamToLobby() {
        for (int i = 0; i < getTeamsLength(); i++) {
            getTeam(i).teleportPlayerToLobby();
        }
    }
}
