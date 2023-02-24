package quarri6343.overcrafted.core.handler;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.object.IDishPile;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.core.object.DishPile;
import quarri6343.overcrafted.impl.OCStages;
import quarri6343.overcrafted.impl.item.StackedDish;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    private static final String highScoreStr = "highScore.";
    private static final String teamStageStr = ".stage.";
    
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
            List<Location> startLocations = new ArrayList<>();
            for (int j = 0; j < OCStages.values().length; j++) {
                startLocations.add(config.getLocation(teamStartLocationStr + i + teamStageStr + j));
            }
            newTeam.setStartLocations(startLocations);
            newTeam.setJoinLocation1(config.getLocation(teamJoinLocation1Str + i));
            newTeam.setJoinLocation2(config.getLocation(teamJoinLocation2Str + i));
            for (int j = 0; j < OCStages.values().length; j++) {
                newTeam.getCleanDishPiles().get(j).setLocation(config.getLocation(teamCleanDishPileStr + i + teamStageStr + j));
                newTeam.getDirtyDishPiles().get(j).setLocation(config.getLocation(teamDirtyDishPileStr + i + teamStageStr + j));
            }
        }
    }

    /**
     * コンフィグからその他データをロードする
     *
     * @param config コンフィグ
     */
    @ParametersAreNonnullByDefault
    private void loadMisc(FileConfiguration config) {
        for (int i = 0; i < OCStages.values().length; i++) {
            OCStages.values()[i].get().setHighScore(config.getInt(highScoreStr + i));
        }
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
            for (int j = 0; j < OCStages.values().length; j++) {
                config.set(teamStartLocationStr + i + teamStageStr + j, data.getTeams().getTeam(i).getStartLocations().get(j));
            }
            config.set(teamJoinLocation1Str + i, data.getTeams().getTeam(i).getJoinLocation1());
            config.set(teamJoinLocation2Str + i, data.getTeams().getTeam(i).getJoinLocation2());
            for (int j = 0; j < OCStages.values().length; j++) {
                config.set(teamCleanDishPileStr + i + teamStageStr + j, data.getTeams().getTeam(i).getCleanDishPiles().get(j).getLocation());
                config.set(teamDirtyDishPileStr + i + teamStageStr + j, data.getTeams().getTeam(i).getDirtyDishPiles().get(j).getLocation());
            }
        }
    }

    /**
     * その他データをコンフィグに保存する
     *
     * @param config コンフィグ
     */
    @ParametersAreNonnullByDefault
    private void saveMisc(FileConfiguration config) {
        for (int i = 0; i < OCStages.values().length; i++) {
            config.set(highScoreStr + i, OCStages.values()[i].get().getHighScore());
        }
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
