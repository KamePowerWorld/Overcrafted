package quarri6343.overcrafted.common.logic;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCSoundData;

/**
 * ゲーム開始ボタンを押してからゲームが始まるまでの処理
 */
public class GameBeginRunnable extends BukkitRunnable {
    private final Runnable onGameBegin;
    private int count = 0;

    public GameBeginRunnable(Runnable onGameBegin) {
        this.onGameBegin = onGameBegin;
    }

    @Override
    public void run() {
        if (count < OCData.gameBeginCountdownLength) {
            if (count % 20 == 0) {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.showTitle(Title.title(Component.text(OCData.gameBeginCountdownLength / 20 - count / 20), Component.empty()));
                    player.playSound(OCSoundData.countDownSound);
                });
            }
            count++;
        } else {
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.playSound(OCSoundData.gameBeginSound);
            });
            onGameBegin.run();
            cancel();
        }
    }
}
