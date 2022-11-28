package quarri6343.overcrafted.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.SimplePluginManager;

import javax.annotation.Nullable;
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
}
