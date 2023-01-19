package quarri6343.overcrafted.common;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import quarri6343.overcrafted.common.data.OCResourcePackData;
import quarri6343.overcrafted.common.data.OCResourcePackData.MenuFont;

public class BossBarHandler {
    
    private static BossBar bossBar;
    
    public static void initiate(){
        Component name = Component.text(MenuFont.BONUS.get_char() + MenuFont.SPACE.get_char() + MenuFont.TORCH.get_char()
        + MenuFont.SPACE.get_char() + MenuFont.FURNACE.get_char() + MenuFont.SPACE.get_char() + MenuFont.IRON.get_char()
        + MenuFont.SPACE.get_char() + MenuFont.MINECART.get_char()).font(OCResourcePackData.menuFontName);
        bossBar = BossBar.bossBar(name, 1, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
        for (Player player : Bukkit.getOnlinePlayers()){
            player.showBossBar(bossBar);
        }
    }
    
    public static void destroy(){
        for (Player player : Bukkit.getOnlinePlayers()){
            player.hideBossBar(bossBar);
        }
    }
}
