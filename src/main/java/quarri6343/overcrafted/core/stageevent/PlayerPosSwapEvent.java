package quarri6343.overcrafted.core.stageevent;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.IStageEvent;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.core.data.constant.OCSoundData;

import java.util.*;

/**
 * 一定時間ごとにプレイヤーの位置をランダムにシャッフルする
 */
public class PlayerPosSwapEvent implements IStageEvent {
    
    private static final int frequency = 400;
    private static final int durationBetweenGameEnd = 200;
    
    @Getter
    private final String eventName = "プレイヤーの位置シャッフル";
    
    private final List<Map<Player, Player>> swapTargetListMap = new ArrayList<>();

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }

    @Override
    public void onStart(World world) {
        swapTargetListMap.clear();
        for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
            swapTargetListMap.add(new HashMap<>());
        }
    }

    @Override
    public void onTick(int count) {
        if(count == 0 || count > (getData().getSelectedStage().get().getTime() * 20 - durationBetweenGameEnd))
            return;

        if((count + 100) % frequency == 0){
            swapTargetListMap.forEach(Map::clear);
            for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
                IOCTeam team = getData().getTeams().getTeam(i);

                List<Player> playersToSwap = new ArrayList<>();
                List<Player> swapTarget = new ArrayList<>();
                for (int j = 0; j < team.getPlayersSize(); j++) {
                    if(!team.getPlayer(j).isDead()){
                        playersToSwap.add(team.getPlayer(j));
                        swapTarget.add(team.getPlayer(j));
                    }
                }
                Collections.shuffle(swapTarget);
                for (int k = 0; k < playersToSwap.size(); k++) {
                    swapTargetListMap.get(i).put(playersToSwap.get(k), swapTarget.get(k));
                }
            }
        }

        for (int i = 1; i <= 5; i++) {
            if((count + 20 * i) % frequency == 0){
                for (Map<Player, Player> playerPlayerMap : swapTargetListMap) {
                    playerPlayerMap.forEach((player, player2) -> {
                        player.playSound(OCSoundData.countDownSound);
                        player.sendActionBar(Component.text("あなたは次")
                                .append(Component.text(player2.getName()).color(NamedTextColor.RED))
                                .append(Component.text("の場所に行きます！")));
                    });
                }
                return;
            }
        }
        
        if(count % frequency == 0){
            for (Map<Player, Player> playerPlayerMap : swapTargetListMap) {
                List<Location> swapLocation = new ArrayList<>();
                playerPlayerMap.forEach((player, player2) -> swapLocation.add(player2.getLocation()));
                int j = 0;
                for (Map.Entry<Player, Player> playerPlayerEntry: playerPlayerMap.entrySet()){
                    playerPlayerEntry.getKey().teleport(swapLocation.get(j));
                    playerPlayerEntry.getKey().playSound(OCSoundData.teleportSound);
                    j++;
                }
            }
        }
    }

    @Override
    public void onEnd() {
    }
}
