package quarri6343.overcrafted;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import quarri6343.overcrafted.api.CommandBase;
import quarri6343.overcrafted.common.CursorGui;
import quarri6343.overcrafted.common.event.ItemEventHandler;
import quarri6343.overcrafted.common.ConfigHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.event.*;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.impl.command.*;
import quarri6343.overcrafted.utils.ResourcePackUtil;

public final class Overcrafted extends JavaPlugin {

    private OCData data;
    private OCLogic logic;

    private ConfigHandler config;

    private PlayerEventHandler playerEventHandler;

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
        new CommandForceJoin();
        new CommandForceLeave();
        new CommandReloadResourcePack();
        new CommandOCGive();
        new CommandBase("show"){
            @Override
            public boolean onCommand(CommandSender sender, @Nullable String[] arguments) {
                if(sender instanceof Player)
                    CursorGui.show((Player) sender);
                return true;
            }
        };
        new CommandBase("hide"){
            @Override
            public boolean onCommand(CommandSender sender, @Nullable String[] arguments) {
                if(sender instanceof Player)
                    CursorGui.hide((Player) sender);
                return true;
            }
        };

        playerEventHandler = new PlayerEventHandler();
        new InventoryEventHandler();
        new ItemEventHandler();
        new BlockEventHandler();
        new DishPileInteractEventHandler();

        ResourcePackUtil.reloadResourcePack();
        
        new CursorGui();
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

    public OCLogic getLogic() {
        return logic;
    }

    public PlayerEventHandler getPlayerEventHandler() {
        return playerEventHandler;
    }
}
