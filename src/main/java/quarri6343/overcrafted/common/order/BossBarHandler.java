package quarri6343.overcrafted.common.order;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCResourcePackData;
import quarri6343.overcrafted.common.data.OCResourcePackData.MenuFont;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.data.interfaces.IOCTeam;
import quarri6343.overcrafted.impl.item.ISubmittable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ボスバーの表示を制御する
 */
public class BossBarHandler {

    private static Map<IOCTeam, BossBar> bossBarMap = new HashMap<>();

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    /**
     * ボスバーに渡された提出可能なアイテムのアイコンリストを表示する
     * @param team ボスバーを表示させるチーム
     * @param dishMenuList メニューリスト
     */
    public static void displayDishMenu(IOCTeam team, List<ISubmittable> dishMenuList) {
        Component text = Component.text(MenuFont.BONUS.get_char() + MenuFont.SPACE.get_char()).font(OCResourcePackData.menuFontName);
        for (ISubmittable dishMenu : dishMenuList) {
            text = text.append(Component.text(dishMenu.toMenuUnicode() + MenuFont.SPACE.get_char() + MenuFont.SPACE.get_char()).font(OCResourcePackData.menuFontName));
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
     * 残り時間を更新する
     * @param progression
     */
    public static void updateRemainingTime(float progression){
        for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
            IOCTeam team = getData().getTeams().getTeam(i);
            BossBar bossBar = bossBarMap.get(team);
            if (bossBar != null) {
                bossBar.progress(progression);
            }
        }
    }

    /**
     * 全てのプレイヤーからボスバーの全てのオブジェクトを隠す
     */
    public static void hideEverything() {
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
}
