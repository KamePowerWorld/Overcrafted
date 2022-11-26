package quarri6343.overcrafted.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
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
}
