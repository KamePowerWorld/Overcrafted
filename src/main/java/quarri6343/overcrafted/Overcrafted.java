package quarri6343.overcrafted;

import org.bukkit.plugin.java.JavaPlugin;
import quarri6343.overcrafted.common.ConfigHandler;
import quarri6343.overcrafted.common.InventoryEventHandler;
import quarri6343.overcrafted.common.PlayerEventHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.impl.command.CommandOvercrafted;

public final class Overcrafted extends JavaPlugin {

    private OCData data;
    private OCLogic logic;
    
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
        logic = new OCLogic();
        new CommandOvercrafted();
        new PlayerEventHandler();
        new InventoryEventHandler();
    }

    @Override
    public void onDisable() {
        config.saveConfig();

        if (logic.gameStatus != OCLogic.GameStatus.INACTIVE) {
            logic.endGame();
        }
    }

    public OCData getData() {
        return data;
    }
    public OCLogic getLogic() {return logic; }
}
