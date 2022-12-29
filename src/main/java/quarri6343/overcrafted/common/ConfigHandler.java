package quarri6343.overcrafted.common;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.OCData;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;

/**
 * コンフィグファイルを読み書きする
 */
public class ConfigHandler {

    private static final String orderBoxLocationPath = "orderBox";
    
    public ConfigHandler() {
    }

    /**
     * コンフィグファイル内のデータをデータクラスにコピーする
     */
    public void loadConfig() {
        JavaPlugin plugin = Overcrafted.getInstance();
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();

//        loadTeams(config);
        loadMisc(config);
    }

    /**
     * コンフィグからその他データをロードする
     *
     * @param config コンフィグ
     */
    @ParametersAreNonnullByDefault
    private void loadMisc(FileConfiguration config) {
        OCData data = Overcrafted.getInstance().getData();

        data.orderBox.location = config.getLocation(orderBoxLocationPath);
    }

    /**
     * データクラスの中身をコンフィグにセーブする
     */
    public void saveConfig() {
        resetConfig();//古いデータが混在しないように一旦コンフィグを消す

        JavaPlugin plugin = Overcrafted.getInstance();
        FileConfiguration config = plugin.getConfig();

//        saveTeams(config);
        saveMisc(config);

        plugin.saveConfig();
    }

    @ParametersAreNonnullByDefault
    private void saveMisc(FileConfiguration config) {
        OCData data = Overcrafted.getInstance().getData();
        config.set(orderBoxLocationPath, data.orderBox.location);
    }

    /**
     * コンフィグを全て削除する
     */
    public void resetConfig() {
        JavaPlugin plugin = Overcrafted.getInstance();
        File configFile = new File(plugin.getDataFolder(), "config.yml");

        if (configFile.delete()) {
            plugin.saveDefaultConfig();
            plugin.reloadConfig();
        }
    }
}
