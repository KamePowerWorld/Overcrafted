package quarri6343.overcrafted.common;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import quarri6343.overcrafted.Overcrafted;

import java.time.Duration;

//placeholder
public class CursorGui implements Listener {

    private static BukkitRunnable runnable;
    private static int id = 7;
    private static boolean clickCooldown = false;
    
    public CursorGui() {
        Overcrafted.getInstance().getServer().getPluginManager().registerEvents(this, Overcrafted.getInstance());
    }
    
    public static void show(Player player) {
        if (runnable != null) {
            return;
        }

        float initYaw = player.getLocation().getYaw();
        float initPitch = player.getLocation().getPitch();

        runnable = new BukkitRunnable() {

            private float lastYaw;
            private float yawOffset;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                float pitch = player.getLocation().getPitch();
                float pitchDiff = -(pitch - initPitch);
                id = 7;
                if(pitchDiff >= 0){
                    id += pitchDiff / 10;
                }
                else {
                    id -= Math.abs(pitchDiff) / 10;
                }
                StringBuilder builder = new StringBuilder().append(((char)('\uE000' + id)));
                
                float yaw = player.getLocation().getYaw();
                if (lastYaw > 140 && yaw < -140) {
                    yawOffset += 360;
                } else if (lastYaw < -140 && yaw > 140) {
                    yawOffset -= 360;
                }
                
                float yawDiff = (yaw - initYaw + yawOffset) * 2;
                if (yawDiff >= 0) {
                    for (int i = 0; i < yawDiff / 4; i++) {
                        builder.append("«");
                    }
                } else {
                    for (int i = 0; i < Math.abs(yawDiff) / 4; i++) {
                        builder.append(" ");
                    }
                }
                player.sendActionBar(Component.text(builder.toString()).font(Key.key("cursor")));
                lastYaw = yaw;
                
                if(!clickCooldown)
                    player.showTitle(Title.title(Component.text(id >= 8 && id <= 11 ? "タイトルを選択中" : "タイトル"), Component.text(id >= 5 && id <= 6 ? "サブタイトルを選択中" : "サブタイトル"), Title.Times.times(Duration.ZERO,Duration.ofSeconds(1),Duration.ZERO)));
            }
        };
        runnable.runTaskTimer(Overcrafted.getInstance(), 0, 1);
    }

    public static void hide(Player player) {
        if (runnable != null)
            runnable.cancel();
        runnable = null;
    }
    
    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event){
        if(runnable == null)
            return;
        
        if(id >= 8 && id <= 11){
            event.getPlayer().showTitle(Title.title(Component.text("タイトルがクリックされました"), Component.text("サブタイトル"), Title.Times.times(Duration.ZERO,Duration.ofSeconds(1),Duration.ZERO)));
            clickCooldown = true;
            new BukkitRunnable(){

                @Override
                public void run() {
                    clickCooldown = false;
                    cancel();
                }
            }.runTaskTimer(Overcrafted.getInstance(), 20, 1);
        }
        else if(id >= 5 && id <= 6){
            event.getPlayer().showTitle(Title.title(Component.text("タイトル"), Component.text("サブタイトルがクリックされました"), Title.Times.times(Duration.ZERO,Duration.ofSeconds(1),Duration.ZERO)));
            clickCooldown = true;
            new BukkitRunnable(){

                @Override
                public void run() {
                    clickCooldown = false;
                    cancel();
                }
            }.runTaskTimer(Overcrafted.getInstance(), 20, 1);
        }
    }
}
