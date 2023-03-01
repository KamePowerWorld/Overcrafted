package quarri6343.overcrafted.core.stageevent;

import lombok.Getter;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Zombie;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.IStageEvent;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.core.object.OCTeam;

import java.util.ArrayList;
import java.util.List;

/**
 * ステージ中ゾンビが湧くイベント
 */
public class ZombieSpawnEvent implements IStageEvent {
    
    private static final int noonTime = 6000;
    private static final int nightTime = 18000;
    private static final int frequency = 400;
    @Getter
    private final String eventName = "ゾンビ襲撃";
    
    private static final List<Zombie> spawnedZombies = new ArrayList<>();
    private World world;

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }
    
    @Override
    public void onStart(World world) {
        this.world = world;
        world.setTime(nightTime);
        world.setDifficulty(Difficulty.EASY);
    }

    @Override
    public void onTick(int count) {
        if(count > 20 * 30 && count % frequency == 0){
            for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
                IOCTeam team = getData().getTeams().getTeam(i);
                Zombie zombie = world.spawn(team.getStartLocations().get(getData().getSelectedStage().ordinal()), Zombie.class);
                spawnedZombies.add(zombie);

                for (int j = 0; j < team.getPlayersSize(); j++) {
                    team.getPlayer(j).setSaturation(5f);
                    team.getPlayer(j).setFoodLevel(20);
                }
            }
        }
    }

    @Override
    public void onEnd() {
        world.setTime(noonTime);
        world.setDifficulty(Difficulty.PEACEFUL);
        for (Zombie spawnedZombie : spawnedZombies) {
            if(!spawnedZombie.isDead())
                spawnedZombie.setHealth(0);
        }
        spawnedZombies.clear();
    }
}
