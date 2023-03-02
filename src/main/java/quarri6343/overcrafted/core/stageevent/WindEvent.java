package quarri6343.overcrafted.core.stageevent;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.IStageEvent;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.core.data.constant.OCSoundData;

import java.util.Random;

public class WindEvent implements IStageEvent {

    private static final int frequency = 600;
    private static final int duration = 200;
    private static final int durationBetweenGameEnd = 200;

    private static boolean isActive = false;
    private static Direction direction = Direction.FRONT;

    @Getter
    private final String eventName = "強風";

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }

    @Override
    public void onStart(World world) {
        isActive = false;
    }

    @Override
    public void onTick(int count) {
        if (count == 0 || count > (getData().getSelectedStage().get().getTime() * 20 - durationBetweenGameEnd))
            return;

        for (int i = 1; i <= 5; i++) {
            if ((count + 20 * i) % frequency == 0) {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.playSound(OCSoundData.countDownSound);
                });
                return;
            }
        }

        if (count % frequency == 0) {
            isActive = true;
            direction = Direction.values()[new Random().nextInt(Direction.values().length)];
        }
        
        if((count - duration) % frequency == 0){
            isActive = false;
        }
        
        if(isActive){
            for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
                IOCTeam team = getData().getTeams().getTeam(i);
                
                for (int j = 0; j < team.getPlayersSize(); j++) {
                    if(count % 5 == 0)
                        team.getPlayer(j).playSound(OCSoundData.windSound);
                    team.getPlayer(j).setVelocity(team.getPlayer(j).getVelocity().add(direction.getVector()));
                }
            }
        }
    }

    @Override
    public void onEnd() {
        isActive = false;
    }
    
    public enum Direction{
        FRONT(new Vector(0,0,0.05)),
        BACK(new Vector(0,0,-0.05)),
        RIGHT(new Vector(0.05,0,0)),
        LEFT(new Vector(-0.05,0,0.05));
        
        private final Vector vector;
        
        Direction(Vector vector){
            this.vector = vector;
        }
        
        public Vector getVector(){
            return vector;
        }
    }
}
