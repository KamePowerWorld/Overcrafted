package quarri6343.overcrafted.core.handler;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.api.IOCTeam;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;

/**
 * コンフィグファイルを読み書きする
 */
public class Config {
    
    private static final String teamNameStr = "team.name.";
    private static final String teamColorStr = "team.color.";
    private static final String teamStartLocationStr = "team.startLocation.";
    private static final String teamJoinLocation1Str = "team.joinLocation1.";
    private static final String teamJoinLocation2Str = "team.joinLocation2.";
    private static final String teamCleanDishPileStr = "team.cleanDishPile.";
    private static final String teamDirtyDishPileStr = "team.dirtyDishPile.";
    
    public Config() {
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
        OCVariableData data = Overcrafted.getInstance().getData();
        data.getTeams().clearTeam();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String teamName = config.getString(teamNameStr + i);
            String teamColor = config.getString(teamColorStr + i);
            if (teamName == null || teamColor == null) {
                break;
            }

            data.getTeams().addTeam(teamName, teamColor);
            IOCTeam newTeam = data.getTeams().getTeam(i);
            newTeam.setStartLocation(config.getLocation(teamStartLocationStr + i));
            newTeam.setJoinLocation1(config.getLocation(teamJoinLocation1Str + i));
            newTeam.setJoinLocation2(config.getLocation(teamJoinLocation2Str + i));
            newTeam.getCleanDishPile().setLocation(config.getLocation(teamCleanDishPileStr + i));
            newTeam.getDirtyDishPile().setLocation(config.getLocation(teamDirtyDishPileStr + i));
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
        OCVariableData data = Overcrafted.getInstance().getData();
        for (int i = 0; i < data.getTeams().getTeamsLength(); i++) {
            config.set(teamNameStr + i, data.getTeams().getTeam(i).getName());
            config.set(teamColorStr + i, data.getTeams().getTeam(i).getColor());
            config.set(teamStartLocationStr + i, data.getTeams().getTeam(i).getStartLocation());
            config.set(teamJoinLocation1Str + i, data.getTeams().getTeam(i).getJoinLocation1());
            config.set(teamJoinLocation2Str + i, data.getTeams().getTeam(i).getJoinLocation2());
            config.set(teamCleanDishPileStr + i, data.getTeams().getTeam(i).getCleanDishPile().getLocation());
            config.set(teamDirtyDishPileStr + i, data.getTeams().getTeam(i).getDirtyDishPile().getLocation());
        }
    }

    /**
     * その他データをコンフィグに保存する
     *
     * @param config コンフィグ
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
