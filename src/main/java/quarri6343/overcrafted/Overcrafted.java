package quarri6343.overcrafted;

import org.bukkit.plugin.java.JavaPlugin;
import quarri6343.overcrafted.core.handler.Config;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.core.handler.PlaceItemHandler;
import quarri6343.overcrafted.impl.event.*;
import quarri6343.overcrafted.impl.task.game.GameEndTask;
import quarri6343.overcrafted.core.OCLogic;
import quarri6343.overcrafted.impl.command.*;
import quarri6343.overcrafted.impl.event.interact.BlockEventHandler;
import quarri6343.overcrafted.impl.event.interact.BlockPickUpEventHandler;
import quarri6343.overcrafted.impl.event.interact.DishPileInteractEventHandler;
import quarri6343.overcrafted.impl.event.interact.ItemEventHandler;
import quarri6343.overcrafted.utils.OverCraftedUtil;
import quarri6343.overcrafted.utils.ResourcePackUtil;

public final class Overcrafted extends JavaPlugin {

    private OCVariableData data;
    private OCLogic logic;

    private Config config;

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
        data = new OCVariableData();
        config = new Config();
        config.loadConfig();
        logic = new OCLogic();

        new CommandOvercrafted();
        new CommandForceJoin();
        new CommandForceLeave();
        new CommandReloadResourcePack();
        new CommandOCGive();
        new CommandLeftTime();

        playerEventHandler = new PlayerEventHandler();
        new InventoryEventHandler();
        new ItemEventHandler();
        new BlockEventHandler();
        new DishPileInteractEventHandler();
        new BlockPickUpEventHandler();

        ResourcePackUtil.reloadResourcePack();

        OverCraftedUtil.forceInit(GameEndTask.class);
        OverCraftedUtil.forceInit(PlaceItemHandler.class);
    }

    @Override
    public void onDisable() {
        config.saveConfig();

        if (logic.gameStatus != OCLogic.GameStatus.INACTIVE) {
            logic.endGame();
        }
    }

    public OCVariableData getData() {
        return data;
    }

    public OCLogic getLogic() {
        return logic;
    }

    public PlayerEventHandler getPlayerEventHandler() {
        return playerEventHandler;
    }
}
