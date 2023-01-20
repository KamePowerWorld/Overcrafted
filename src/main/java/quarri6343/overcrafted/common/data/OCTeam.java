package quarri6343.overcrafted.common.data;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.utils.OvercraftedUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * UnRedstoneを共に攻略するチームのデータクラス
 */
public class OCTeam {

    public final String name;
    public final String color;

    private Location startLocation;

    public Location joinLocation1;
    public Location joinLocation2;

    public final CleanDishPile cleanDishPile = new CleanDishPile();

    private final List<OCPlayer> players = new ArrayList<>();

    public OCTeam(String name, String color) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (NamedTextColor.NAMES.value(color) == null) {
            throw new IllegalArgumentException();
        }

        this.name = name;
        this.color = color;
    }

    public Location getStartLocation() {
        return startLocation;
    }
    
    /**
     * チームに所属しているプレイヤーの環境をゲーム開始に適した状態に変更する
     *
     * @param player チームに所属しているプレイヤー
     */
    public void setUpGameEnvforPlayer(Player player) {
        if (!containsPlayer(player))
            return;

        player.teleport(startLocation);
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().setContents(new ItemStack[]{});
        player.setSaturation(5f);
        player.setFoodLevel(20);

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            if (i > 0 && i < 36)
                player.getInventory().setItem(i, OCData.invalidItem);
        }
    }

    public void setStartLocation(Location location) {
        startLocation = location;
    }

    public void addPlayer(Player player) {
        if (containsPlayer(player))
            removePlayer(player, false);

        players.add(new OCPlayer(player));
    }

    public Player getPlayer(int index) {
        return players.get(index).getEntity();
    }

    public void removePlayer(Player player, boolean restoreStats) {
        OCPlayer playerToRemove = players.stream().filter(urPlayer -> urPlayer.getEntity().equals(player)).findFirst().orElse(null);
        if (playerToRemove == null) {
            return;
        }

        if (restoreStats)
            playerToRemove.restoreStats();
        players.remove(playerToRemove);
    }

    public void removeAllPlayer(boolean restoreStats) {
        if (restoreStats) {
            for (OCPlayer player : players) {
                player.restoreStats();
            }
        }
        players.clear();
    }

    public int getPlayersSize() {
        return players.size();
    }

    public boolean containsPlayer(Player player) {
        return players.stream().filter(urPlayer -> urPlayer.getEntity().equals(player)).findFirst().orElse(null) != null;
    }

    public List<TextComponent> playerNamesToText() {
        return players.stream().map(player1 -> Component.text(player1.getEntity().getName()).color(NamedTextColor.YELLOW)).toList();
    }

    public void clearExcessiveItemsFromAllPlayers() {
        players.forEach(OCPlayer::dropExcessiveItems);
    }

    public void teleportPlayerToLobby() {
        if (joinLocation1 == null || joinLocation2 == null)
            return;

        Location centerLocation = OvercraftedUtils.getCenterLocation(joinLocation1, joinLocation2);
        for (int j = 0; j < getPlayersSize(); j++) {
            getPlayer(j).teleport(centerLocation);
        }
    }
}
