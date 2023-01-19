package quarri6343.overcrafted.common.order;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.DishMenu;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCResourcePackData;
import quarri6343.overcrafted.common.data.OCResourcePackData.MenuFont;
import quarri6343.overcrafted.common.data.OCTeam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BossBarHandler {
    
    private static Map<OCTeam, BossBar> bossBarMap = new HashMap<>();
    
    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }
    
    public static void displayDishMenu(OCTeam team, List<DishMenu> dishMenuList){
        Component text = Component.text(MenuFont.BONUS.get_char() + MenuFont.SPACE.get_char()).font(OCResourcePackData.menuFontName);
        for (DishMenu dishMenu : dishMenuList){
            text = text.append(Component.text(dishMenu.toUnicode() + MenuFont.SPACE.get_char() + MenuFont.SPACE.get_char()).font(OCResourcePackData.menuFontName));
        }
        
        BossBar bossBar = bossBarMap.get(team);
        if(bossBar == null){
            bossBar = BossBar.bossBar(text, 1, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
            bossBarMap.put(team, bossBar);

            for (int i = 0; i < team.getPlayersSize(); i++) {
                team.getPlayer(i).showBossBar(bossBar);
            }
        }
        else{
            bossBar.name(text);
        }
    }
    
    public static void hideEverything(){
        for (int i = 0; i < getData().teams.getTeamsLength(); i++) {
            OCTeam team = getData().teams.getTeam(i);
            BossBar bossBar = bossBarMap.get(team);
            if(bossBar != null){
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
