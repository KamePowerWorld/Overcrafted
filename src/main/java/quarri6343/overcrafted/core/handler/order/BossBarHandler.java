package quarri6343.overcrafted.core.handler.order;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.core.data.constant.OCResourcePackData;
import quarri6343.overcrafted.core.data.constant.OCResourcePackData.MenuFont;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.api.item.ISubmittableOCItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ボスバーの表示を制御する
 */
public class BossBarHandler {

    private static Map<IOCTeam, BossBar> bossBarMap = new HashMap<>();

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }

    /**
     * 特定のチームの全てのプレイヤーにボスバーを登録もしくは更新し、チームとボスバーを紐づける
     * メニューが更新される時に呼び出される想定
     *
     * @param team         ボスバーを表示させるチーム
     * @param dishMenuList メニューリスト
     */
    public static void registerOrUpdateBossBar(IOCTeam team, List<ISubmittableOCItem> dishMenuList) {
        Component text = Component.text("               \uE000««««««««««««««««««").font(OCResourcePackData.bossBarFontName);
        text = text.append(Component.text(MenuFont.BONUS.get_char() + MenuFont.SPACE.get_char()).font(OCResourcePackData.menuFontName));
        for (ISubmittableOCItem dishMenu : dishMenuList) {
            text = text.append(Component.text(dishMenu.toMenuUnicode() + OCResourcePackData.ScoreFont.scoreToFont(dishMenu.getScore()) + MenuFont.SPACE.get_char() + MenuFont.SPACE.get_char()).font(OCResourcePackData.menuFontName));
        }

        BossBar bossBar = bossBarMap.get(team);
        if (bossBar == null) {
            bossBar = BossBar.bossBar(text, 1, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
            bossBarMap.put(team, bossBar);

            for (int i = 0; i < team.getPlayersSize(); i++) {
                team.getPlayer(i).showBossBar(bossBar);
            }
        } else {
            bossBar.name(text);
        }
    }

    /**
     * ボスバーが紐づけられているチームの、残り時間を更新する
     *
     * @param progression 設定したい残り時間
     */
    public static void updateRemainingTime(float progression) {
        for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
            IOCTeam team = getData().getTeams().getTeam(i);
            BossBar bossBar = bossBarMap.get(team);
            if (bossBar != null) {
                bossBar.progress(progression);
            }
        }
    }

    /**
     * 全てのプレイヤーからボスバーの全てのオブジェクトを隠し、チームとボスバーの紐づけを解除する
     */
    public static void hideBossBarFromEveryTeam() {
        for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
            IOCTeam team = getData().getTeams().getTeam(i);
            BossBar bossBar = bossBarMap.get(team);
            if (bossBar != null) {
                for (int j = 0; j < team.getPlayersSize(); j++) {
                    team.getPlayer(j).hideBossBar(bossBar);
                }
            }
        }

        bossBarMap = new HashMap<>();

        Bukkit.getServer().getBossBars().forEachRemaining(keyedBossBar -> {
            keyedBossBar.hide();
            keyedBossBar.removeAll();
        });
    }

    /**
     * あるプレイヤーのチームとボスバーが紐づけられていれば、ボスバーを表示する
     * (二重に表示される危険性があるので注意！)
     */
    public static void showBossBar(Player player) {
        IOCTeam team = getData().getTeams().getTeamByPlayer(player);
        if (team == null)
            return;

        BossBar bossBar = bossBarMap.get(team);
        if (bossBar != null) {
            player.showBossBar(bossBar);
        }
    }

    /**
     * あるプレイヤーに表示されているチームのボスバーを隠す
     */
    public static void hideBossBar(Player player) {
        IOCTeam team = getData().getTeams().getTeamByPlayer(player);
        if (team == null)
            return;

        BossBar bossBar = bossBarMap.get(team);
        if (bossBar != null) {
            player.hideBossBar(bossBar);
        }
    }
}
