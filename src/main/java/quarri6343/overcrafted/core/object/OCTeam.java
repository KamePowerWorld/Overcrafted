package quarri6343.overcrafted.core.object;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.impl.OCStages;
import quarri6343.overcrafted.impl.item.OCItems;
import quarri6343.overcrafted.impl.item.StackedDish;
import quarri6343.overcrafted.api.object.IDishPile;
import quarri6343.overcrafted.api.object.IOCPlayer;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.utils.OverCraftedUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OCTeam implements IOCTeam {

    /**
     * チームの名前
     */
    @Getter
    private final String name;

    /**
     * チームの色
     */
    @Getter
    private final String color;

    /**
     * チームのステージごとのゲーム開始地点
     */
    @Getter
    private List<Location> startLocations = new ArrayList<>(Collections.nCopies(OCStages.values().length, null));

    /**
     * チームの参加エリアの始点
     */
    @Getter @Setter
    private Location joinLocation1;

    /**
     * チームの参加エリアの終点
     */
    @Getter @Setter
    private Location joinLocation2;

    /**
     * チームのステージごとの綺麗な皿置き場
     */
    @Getter
    private final List<IDishPile> cleanDishPiles = new ArrayList<>();

    /**
     * チームのステージごとの汚い皿置き場
     */
    @Getter
    private final List<IDishPile> dirtyDishPiles = new ArrayList<>();

    private final List<IOCPlayer> players = new ArrayList<>();

    public OCTeam(String name, String color) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (NamedTextColor.NAMES.value(color) == null) {
            throw new IllegalArgumentException();
        }

        this.name = name;
        this.color = color;

        for (int i = 0; i < OCStages.values().length; i++) {
            cleanDishPiles.add(new DishPile(StackedDish.StackedDishType.CLEAN));
            dirtyDishPiles.add(new DishPile(StackedDish.StackedDishType.DIRTY));
        }
    }

    public void setUpGameEnvforPlayer(Player player, int stageID) {
        if (!containsPlayer(player))
            return;

        player.teleport(startLocations.get(stageID));
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().setContents(new ItemStack[]{});
        player.setSaturation(5f);
        player.setFoodLevel(20);
    }
    
    public Location initializeRespawnedPlayer(Player player, int stageID){
        if (!containsPlayer(player))
            return null;
        
        returnPlayerDish(player, stageID);
        player.getInventory().setContents(new ItemStack[]{});
        
        player.setSaturation(5f);
        player.setFoodLevel(20);
        
        return startLocations.get(stageID);
    }
    
    public void returnPlayerDish(Player player, int stageID){
        if (!containsPlayer(player))
            return;
        
        if(player.getInventory().contains(OCItems.DISH.get().getItemStack())){
            cleanDishPiles.get(stageID).addDish();
        }
        else if(player.getInventory().contains(OCItems.DIRTY_DISH.get().getItemStack())){
            dirtyDishPiles.get(stageID).addDish();
        }
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
        IOCPlayer playerToRemove = players.stream().filter(urPlayer -> urPlayer.getEntity().equals(player)).findFirst().orElse(null);
        if (playerToRemove == null) {
            return;
        }

        if (restoreStats)
            playerToRemove.restoreStats();
        players.remove(playerToRemove);
    }

    public void removeAllPlayer(boolean restoreStats) {
        if (restoreStats) {
            for (IOCPlayer player : players) {
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
        players.forEach(IOCPlayer::dropExcessiveItems);
    }

    public void teleportPlayersToLobby() {
        if (joinLocation1 == null || joinLocation2 == null)
            return;

        Location centerLocation = OverCraftedUtil.getCenterLocation(joinLocation1, joinLocation2);
        for (int j = 0; j < getPlayersSize(); j++) {
            getPlayer(j).teleport(centerLocation);
        }
    }

    public void teleportPlayerToLobby(Player player) {
        if(!containsPlayer(player))
            return;
        
        if (joinLocation1 == null || joinLocation2 == null)
            return;

        Location centerLocation = OverCraftedUtil.getCenterLocation(joinLocation1, joinLocation2);
        player.teleport(centerLocation);
    }
    
    public void setStartLocations(List<Location> locations){
        if(locations.size() != OCStages.values().length)
            throw new IllegalArgumentException();
        
        this.startLocations = locations;
    }
}
