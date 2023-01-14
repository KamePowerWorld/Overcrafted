package quarri6343.overcrafted.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.SimplePluginManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;

public class OvercraftedUtils {

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
     * @param item アイテム
     * @return テキスト
     */
    public static Component getItemInfoasText(ItemStack item) {
        if (item.getItemMeta().hasDisplayName()){
            return Component.text()
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false)
                    .content(item.getItemMeta().getDisplayName() + " x" + item.getAmount()).build();
        }
        else {
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
}
