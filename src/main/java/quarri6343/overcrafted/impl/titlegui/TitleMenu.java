package quarri6343.overcrafted.impl.titlegui;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.utils.OverCraftedUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TitleMenu implements Listener {

    private static final Map<Player, Cursor> cursorMap = new HashMap<>();

    private static final List<Player> clickCooldownList = new ArrayList<>();
    public static final Map<Player, BukkitRunnable> runnableMap = new HashMap<>();

    public TitleMenu() {
        Overcrafted.getInstance().getServer().getPluginManager().registerEvents(this, Overcrafted.getInstance());
    }

    public static void show(Player player) {
        if (runnableMap.get(player) != null) {
            return;
        }

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                if (cursorMap.get(player) == null)
                    cursorMap.put(player, new Cursor(player));
                cursorMap.get(player).show();
                int currentHeight = cursorMap.get(player).currentHeight;
                if (!clickCooldownList.contains(player)){
                    //                    player.showTitle(Title.title(Component.text( currentHeight>= 8 && currentHeight <= 11 ? "タイトルを選択中" : "タイトル"), 
//                            Component.text(currentHeight >= 5 && currentHeight <= 6 ? "サブタイトルを選択中" : "サブタイトル"), 
//                            Title.Times.times(Duration.ZERO,Duration.ofSeconds(1),Duration.ZERO)));
                    Component subtitle = Component.text("");
                    for (int i = 1; i < 9; i++) {
                        subtitle = subtitle.append(Component.text(OverCraftedUtils.fixedLengthString("サブタイトルテスト" + i, 12)).font(Key.key("customui_" + i))
                                .append(Component.text("««  ").font(Key.key("menu"))));
                    }
                    player.showTitle(Title.title(Component.text(""),
                            subtitle,
                            Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ZERO)));
                }
            }
        };
        runnable.runTaskTimer(Overcrafted.getInstance(), 0, 1);
        runnableMap.put(player, runnable);
    }

    public static void hide(Player player) {
        if (runnableMap.containsKey(player))
            runnableMap.get(player).cancel();
        runnableMap.remove(player);
        cursorMap.remove(player);
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        if (runnableMap.get(event.getPlayer()) == null)
            return;

        Cursor cursor = cursorMap.get(event.getPlayer());
        if (cursor == null)
            return;

        if (cursor.currentHeight >= 8 && cursor.currentHeight <= 11) {
            event.getPlayer().showTitle(Title.title(Component.text("タイトルがクリックされました"), Component.text("サブタイトル"), Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ZERO)));
            clickCooldownList.add(event.getPlayer());
            new BukkitRunnable() {
                @Override
                public void run() {
                    clickCooldownList.remove(event.getPlayer());
                    cancel();
                }
            }.runTaskTimer(Overcrafted.getInstance(), 20, 1);
        } else if (cursor.currentHeight >= 5 && cursor.currentHeight <= 6) {
            event.getPlayer().showTitle(Title.title(Component.text("タイトル"), Component.text("サブタイトルがクリックされました"), Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ZERO)));
            clickCooldownList.add(event.getPlayer());
            new BukkitRunnable() {
                @Override
                public void run() {
                    clickCooldownList.remove(event.getPlayer());
                    cancel();
                }
            }.runTaskTimer(Overcrafted.getInstance(), 20, 1);
        }
    }
}
