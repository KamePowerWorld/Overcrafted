package quarri6343.overcrafted.common;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;

/**
 * コンフィグファイルを読み書きする
 */
public class ConfigHandler {

    public ConfigHandler() {
    }

    /**
     * コンフィグファイル内のデータをデータクラスにコピーする
     */
    public void loadConfig() {
        JavaPlugin plugin = Overcrafted.getInstance();
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();

        loadTeams(config);
        loadMisc(config);
    }

    /**
     * コンフィグからチームをロードする
     *
     * @param config コンフィグ
     */
    @ParametersAreNonnullByDefault
    private void loadTeams(FileConfiguration config) {
        OCData data = Overcrafted.getInstance().getData();
        data.teams.clearTeam();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String teamName = config.getString("team.name." + i);
            String teamColor = config.getString("team.color." + i);
            if (teamName == null || teamColor == null) {
                break;
            }

            data.teams.addTeam(teamName, teamColor);
            OCTeam newTeam = data.teams.getTeam(i);
            newTeam.setStartLocation(config.getLocation("team.startLocation." + i));
            newTeam.joinLocation1 = config.getLocation("team.joinLocation1." + i);
            newTeam.joinLocation2 = config.getLocation("team.joinLocation2." + i);
            newTeam.cleanDishPile.location = config.getLocation("team.cleanDishPile." + i);
            newTeam.dirtyDishPile.location = config.getLocation("team.dirtyDishPile." + i);
        }
    }

    /**
     * コンフィグからその他データをロードする
     *
     * @param config コンフィグ
     */
    @ParametersAreNonnullByDefault
    private void loadMisc(FileConfiguration config) {

    }

    /**
     * データクラスの中身をコンフィグにセーブする
     */
    public void saveConfig() {
        resetConfig();//古いデータが混在しないように一旦コンフィグを消す

        JavaPlugin plugin = Overcrafted.getInstance();
        FileConfiguration config = plugin.getConfig();

        saveTeams(config);
        saveMisc(config);

        plugin.saveConfig();
    }

    /**
     * メインクラスがロードしているチームクラスをコンフィグに保存する
     *
     * @param config コンフィグ
     */
    @ParametersAreNonnullByDefault
    private void saveTeams(FileConfiguration config) {
        OCData data = Overcrafted.getInstance().getData();
        for (int i = 0; i < data.teams.getTeamsLength(); i++) {
            config.set("team.name." + i, data.teams.getTeam(i).name);
            config.set("team.color." + i, data.teams.getTeam(i).color);
            config.set("team.startLocation." + i, data.teams.getTeam(i).getStartLocation());
            config.set("team.joinLocation1." + i, data.teams.getTeam(i).joinLocation1);
            config.set("team.joinLocation2." + i, data.teams.getTeam(i).joinLocation2);
            config.set("team.cleanDishPile." + i, data.teams.getTeam(i).cleanDishPile.location);
            config.set("team.dirtyDishPile." + i, data.teams.getTeam(i).dirtyDishPile.location);
        }
    }

    /**
     * その他データをコンフィグに保存する
     *
     * @param config
     */
    @ParametersAreNonnullByDefault
    private void saveMisc(FileConfiguration config) {
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
