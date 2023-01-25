package quarri6343.overcrafted.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.SimplePluginManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;

/**
 * 雑多な関数をまとめたクラス
 */
public class OverCraftedUtils {

    /**
     * プラグインマネージャからコマンドマップを取得する
     *
     * @return 取得したコマンドマップ
     */
    @Nullable
    public static CommandMap getCommandMap() {
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);

                return (CommandMap) field.get(Bukkit.getPluginManager());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * アイテムの名称(ローカライズ済み)と個数をテキストコンポーネントに変換する
     *
     * @param item アイテム
     * @return テキスト
     */
    public static Component getItemInfoasText(ItemStack item) {
        if (item.getItemMeta().hasDisplayName()) {
            return Component.text()
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false)
                    .content(item.getItemMeta().getDisplayName() + " x" + item.getAmount()).build();
        } else {
            return Component.translatable()
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false)
                    .append(Component.text().content(" x" + item.getAmount()).build())
                    .key(item.getType().translationKey()).build();
        }
    }

    /**
     * ある範囲のx,z空間内の中心点を見つける
     *
     * @param location1 範囲の始点
     * @param location2 範囲の終点
     * @return 範囲の中心点
     */
    @ParametersAreNonnullByDefault
    public static @Nonnull
    Location getCenterLocation(Location location1, Location location2) {
        return new Location(location1.getWorld(),
                (location1.getX() + location2.getX()) / 2,
                (location1.getY() + location2.getY()) / 2,
                (location1.getZ() + location2.getZ()) / 2);
    }

    /**
     * プレイヤーがx,z空間上で範囲内にいるか判定する
     *
     * @param player    プレイヤー
     * @param location1 範囲の始点
     * @param location2 範囲の終点
     * @return 範囲内にいるか
     */
    @ParametersAreNonnullByDefault
    public static boolean isPlayerInArea(Player player, Location location1, Location location2) {
        double playerX = player.getLocation().getX();
        double playerZ = player.getLocation().getZ();

        boolean isXInRange = Math.min(location1.getX(), location2.getX()) <= playerX
                && Math.max(location1.getX(), location2.getX()) >= playerX;
        boolean isZInRange = Math.min(location1.getZ(), location2.getZ()) <= playerZ
                && Math.max(location1.getZ(), location2.getZ()) >= playerZ;

        return isXInRange && isZInRange;
    }

    public static String fixedLengthString(String string, int length) {
        return String.format("%1$" + length + "s", string);
    }

    public static Entity getNearestEntityInSight(Player player, int range) {
        ArrayList<Entity> entities = (ArrayList<Entity>) player.getNearbyEntities(range, range, range);
        ArrayList<Block> sightBlock = (ArrayList<Block>) player.getLineOfSight(null, range);
        ArrayList<Location> sight = new ArrayList<>();
        for (int i = 0; i < sightBlock.size(); i++)
            sight.add(sightBlock.get(i).getLocation());
        for (int i = 0; i < sight.size(); i++) {
            for (int k = 0; k < entities.size(); k++) {
                if (Math.abs(entities.get(k).getLocation().getX() - sight.get(i).getX()) < 1.3) {
                    if (Math.abs(entities.get(k).getLocation().getY() - sight.get(i).getY()) < 1.5) {
                        if (Math.abs(entities.get(k).getLocation().getZ() - sight.get(i).getZ()) < 1.3) {
                            return entities.get(k);
                        }
                    }
                }
            }
        }
        return null; //Return null/nothing if no entity was found
    }
}
