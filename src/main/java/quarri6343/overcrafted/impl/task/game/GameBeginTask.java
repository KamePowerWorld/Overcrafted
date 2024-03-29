package quarri6343.overcrafted.impl.task.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.core.data.constant.OCCommonData;
import quarri6343.overcrafted.core.data.constant.OCSoundData;

/**
 * ゲーム開始ボタンを押してからゲームが始まるまでの処理
 */
public class GameBeginTask extends BukkitRunnable {
    private final Runnable onGameBegin;
    private int count = 0;

    public GameBeginTask(Runnable onGameBegin) {
        this.onGameBegin = onGameBegin;
    }

    @Override
    public void run() {
        if (count < OCCommonData.gameBeginCountdownLength) {
            if (count % 20 == 0) {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.showTitle(Title.title(Component.text(OCCommonData.gameBeginCountdownLength / 20 - count / 20), Component.empty()));
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
