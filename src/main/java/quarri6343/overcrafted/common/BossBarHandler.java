package quarri6343.overcrafted.common;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BossBarHandler {
    
    private static BossBar bossBar;
    
    public static void initiate(){
        Component name = Component.text("\uE004 \uE000  \uE001  \uE002  \uE003").font(Key.key("menu"));
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
