package quarri6343.overcrafted.core.stageevent;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.IStageEvent;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.core.data.constant.OCSoundData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 一定時間ごとにプレイヤーの位置をランダムにシャッフルする
 */
public class PlayerPosSwapEvent implements IStageEvent {
    
    private static final int frequency = 400;
    private static final int durationBetweenGameEnd = 200;
    
    @Getter
    private final String eventName = "プレイヤーの位置シャッフル";

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }

    @Override
    public void onStart(World world) {
    }

    @Override
    public void onTick(int count) {
        if(count == 0 || count > (getData().getSelectedStage().get().getTime() * 20 - durationBetweenGameEnd))
            return;

        for (int i = 1; i <= 5; i++) {
            if((count + 20 * i) % frequency == 0){
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.playSound(OCSoundData.countDownSound);
                });
                return;
            }
        }
        
        if(count % frequency == 0){
            for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
                IOCTeam team = getData().getTeams().getTeam(i);

                List<Player> playersToSwap = new ArrayList<>();
                List<Location> swapTarget = new ArrayList<>();
                for (int j = 0; j < team.getPlayersSize(); j++) {
                    if(!team.getPlayer(j).isDead()){
                        playersToSwap.add(team.getPlayer(j));
                        swapTarget.add(team.getPlayer(j).getLocation());
                    }
                }
                Collections.shuffle(swapTarget);
                for (int k = 0; k < playersToSwap.size(); k++) {
                    playersToSwap.get(k).teleport(swapTarget.get(k));
                }
            }
        }
    }

    @Override
    public void onEnd() {
    }
}
