package quarri6343.overcrafted.common.data.interfaces;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Overcraftedを共に攻略するチームのデータ
 */
public interface IOCTeam {
    
    public String getName();
    
    public String getColor();
    
    public Location getStartLocation();
    
    public void setStartLocation(Location location);
    
    public Location getJoinLocation1();
    
    public void setJoinLocation1(Location location);
    
    public Location getJoinLocation2();
    
    public void setJoinLocation2(Location location);
    
    public IDishPile getCleanDishPile();
    
    public IDishPile getDirtyDishPile();

    /**
     * チームに所属しているプレイヤーの環境をゲーム開始に適した状態に変更する
     *
     * @param player チームに所属しているプレイヤー
     */
    public void setUpGameEnvforPlayer(Player player);

    public void addPlayer(Player player);

    public Player getPlayer(int index);
    
    public void removePlayer(Player player, boolean restoreStats);

    public void removeAllPlayer(boolean restoreStats);

    public int getPlayersSize();

    public boolean containsPlayer(Player player);

    public List<TextComponent> playerNamesToText();

    public void clearExcessiveItemsFromAllPlayers();

    public void teleportPlayerToLobby();
}
