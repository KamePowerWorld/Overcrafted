package quarri6343.overcrafted.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.data.interfaces.IOCTeam;

import javax.annotation.ParametersAreNonnullByDefault;

public class MCTeams {

    public static void addPlayerToMCTeam(Player player, IOCTeam urTeam) {
        Team team = getBoard().getTeam(urTeam.getName());
        if (team == null)
            team = createMinecraftTeam(urTeam);

        if (!team.hasPlayer(player))
            team.addPlayer(player);
    }

    /**
     * 新しいminecraftのチームを作る
     */
    private static Team createMinecraftTeam(IOCTeam urTeam) {
        Team team = getBoard().registerNewTeam(urTeam.getName());
        team.color(NamedTextColor.NAMES.value(urTeam.getColor()));
        team.displayName(Component.text(urTeam.getName()).color(NamedTextColor.NAMES.value(urTeam.getColor())));
        team.setAllowFriendlyFire(false);

        return team;
    }

    /**
     * minecraftのチームを全て解散させる
     */
    public static void deleteMinecraftTeams() {
        for (int i = 0; i < getBoard().getTeams().size(); i++) {
            getBoard().getTeams().forEach(Team::unregister);
        }
    }

    /**
     * プレイヤーをチームから外す
     *
     * @param player プレイヤー名
     */
    @ParametersAreNonnullByDefault
    public static void removePlayerFromMCTeam(Player player) {
        Team team = getBoard().getPlayerTeam(player);
        if (team == null)
            return;

        team.removePlayer(player);

        if (team.getPlayers().size() == 0)
            team.unregister();
    }

    private static Scoreboard getBoard() {
        return Bukkit.getScoreboardManager().getMainScoreboard();
    }
}
