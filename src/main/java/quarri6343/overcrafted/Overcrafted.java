package quarri6343.overcrafted;

import org.bukkit.plugin.java.JavaPlugin;
import quarri6343.overcrafted.common.PlayerEventHandler;
import quarri6343.overcrafted.impl.CommandOvercrafted;

public final class Overcrafted extends JavaPlugin {

    /**
     * シングルトンで管理されているこのクラスのインスタンス
     */
    private static Overcrafted instance;

    public Overcrafted() {
        instance = this;
    }

    public static Overcrafted getInstance() {
        return instance;
    }
    
    @Override
    public void onEnable() {
        new CommandOvercrafted();
        new PlayerEventHandler();
    }

    @Override
    public void onDisable() {

    }
}
