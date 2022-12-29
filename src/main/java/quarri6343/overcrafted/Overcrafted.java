package quarri6343.overcrafted;

import org.bukkit.plugin.java.JavaPlugin;
import quarri6343.overcrafted.common.ConfigHandler;
import quarri6343.overcrafted.common.PlayerEventHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.impl.CommandOvercrafted;

public final class Overcrafted extends JavaPlugin {

    private OCData data;
    
    private ConfigHandler config;

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
        data = new OCData();
        config = new ConfigHandler();
        config.loadConfig();
        new CommandOvercrafted();
        new PlayerEventHandler();
    }

    @Override
    public void onDisable() {
        config.saveConfig();
    }

    public OCData getData() {
        return data;
    }
}
