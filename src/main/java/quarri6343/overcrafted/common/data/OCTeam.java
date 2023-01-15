package quarri6343.overcrafted.common.data;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.utils.ItemCreator;

import javax.annotation.Nullable;
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

    public OrderBox orderBox = new OrderBox();

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
        if(containsPlayer(player))
            removePlayer(player, false);

        players.add(new OCPlayer(player));
    }

    public Player getPlayer(int index) {
        return players.get(index).entity;
    }

    public void removePlayer(Player player, boolean restoreStats) {
        OCPlayer playerToRemove = players.stream().filter(urPlayer -> urPlayer.entity.equals(player)).findFirst().orElse(null);
        if (playerToRemove == null) {
            return;
        }

        if(restoreStats)
            playerToRemove.restoreStats();
        players.remove(playerToRemove);
    }

    public void removeAllPlayer(boolean restoreStats) {
        if(restoreStats){
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
        return players.stream().filter(urPlayer -> urPlayer.entity.equals(player)).findFirst().orElse(null) != null;
    }

    public List<TextComponent> playerNamesToText() {
        return players.stream().map(player1 -> Component.text(player1.entity.getName()).color(NamedTextColor.YELLOW)).toList();
    }
}
