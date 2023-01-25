package quarri6343.overcrafted.impl.titlegui;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class Cursor{
    
    private final Player player;
    private final float initYaw;
    private final float initPitch;
    private float lastYaw;
    private float yawOffset;
    
    public int currentHeight;
    
    public Cursor(Player player){
        this.player = player;
        this.initYaw = player.getLocation().getYaw();
        this.initPitch = player.getLocation().getPitch();
    }
    
    public void show() {
        if (!player.isOnline()) {
            return;
        }

        float pitch = player.getLocation().getPitch();
        float pitchDiff = -(pitch - initPitch);
        currentHeight = 7;
        if(pitchDiff >= 0){
            currentHeight += pitchDiff / 10;
        }
        else {
            currentHeight -= Math.abs(pitchDiff) / 10;
        }
        StringBuilder builder = new StringBuilder().append(((char)('\uE000' + currentHeight)));

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
    }
}
