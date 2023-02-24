package quarri6343.overcrafted.core.object;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.api.object.IOCTeams;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

/**
 * チームに対する操作を行うクラス
 */
public class OCTeams implements IOCTeams {

    /**
     * ゲーム上に存在する全てのチーム
     */
    private final List<IOCTeam> teams = new ArrayList<>();
    
    @ParametersAreNonnullByDefault
    public void addTeam(@NotNull String name, @NotNull String color) {
        if (getTeamByName(name) != null) {
            throw new IllegalArgumentException();
        }

        if (getTeamByName(color) != null) {
            throw new IllegalArgumentException();
        }

        teams.add(new OCTeam(name, color));
    }
    
    @ParametersAreNonnullByDefault
    public void removeTeam(String name) {
        teams.removeIf(team -> team.getName().equals(name));
    }
    
    public @Nonnull IOCTeam getTeam(int index) {
        return teams.get(index);
    }
    
    @ParametersAreNonnullByDefault
    public @Nullable IOCTeam getTeamByName(String name) {
        return teams.stream().filter(v -> v.getName().equals(name)).findFirst().orElse(null);
    }
    
    @ParametersAreNonnullByDefault
    public @Nullable IOCTeam getTeamByColor(String color) {
        return teams.stream().filter(v -> v.getColor().equals(color)).findFirst().orElse(null);
    }

    @ParametersAreNonnullByDefault
    public @Nullable IOCTeam getTeamByPlayer(Player player) {
        return teams.stream().filter(v -> v.containsPlayer(player)).findFirst().orElse(null);
    }

    public int getTeamsLength() {
        return teams.size();
    }
    
    public void clearTeam() {
        teams.clear();
    }
    
    public void disbandTeams(boolean restoreStats) {
        for (int i = 0; i < getTeamsLength(); i++) {
            getTeam(i).removeAllPlayer(restoreStats);
        }
    }

    public int countAllPlayers() {
        int playerCount = 0;
        for (int i = 0; i < getTeamsLength(); i++) {
            playerCount += getTeam(i).getPlayersSize();
        }

        return playerCount;
    }

    public void clearDishPile(int stageID) {
        for (int i = 0; i < getTeamsLength(); i++) {
            getTeam(i).getCleanDishPiles().get(stageID).destroy();
            getTeam(i).getDirtyDishPiles().get(stageID).destroy();
        }
    }
    
    public void clearExcessiveItemsFromAllTeam() {
        for (int i = 0; i < getTeamsLength(); i++) {
            getTeam(i).clearExcessiveItemsFromAllPlayers();
        }
    }

    public void teleportTeamToLobby() {
        for (int i = 0; i < getTeamsLength(); i++) {
            getTeam(i).teleportPlayerToLobby();
        }
    }
}
