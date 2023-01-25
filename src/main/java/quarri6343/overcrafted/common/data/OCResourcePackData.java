package quarri6343.overcrafted.common.data;

import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;

public class OCResourcePackData {

    /**
     * リソースパックのurl
     */
    public static final String packURL = "https://drive.google.com/uc?export=download&id=1_ryjm_tb5HVxHwi28Rp3TPk7QSMOSepr";

    /**
     * ダウンロードしてきたリソースパックを補完するパス
     */
    public static final String packPath = "plugins/overcrafted_resourcepack.zip";

    /**
     * リソースパックのハッシュ
     */
    public static String packHash;

    /**
     * menuFontのリソースパック上での名前
     */
    public static final Key menuFontName = Key.key("menu");

    /**
     * ProgressBarFontのリソースパック上での名前
     */
    public static final Key progressBarFontName = Key.key("progressbar");

    /**
     * リソースパックのmenuFontのカスタムアイコン名とunicodeの対応表
     */
    public enum MenuFont {
        SPACE(" "),
        TORCH("\uE000"),
        FURNACE("\uE001"),
        IRON("\uE002"),
        MINECART("\uE003"),
        BONUS("\uE004");

        @Getter
        private final String _char;

        MenuFont(String _char) {
            this._char = _char;
        }
    }

    /**
     * リソースパックのprogressbarFontのカスタムアイコン名とunicodeの対応表
     */
    public enum ProgressBarFont {
        SPACE(" ",-1),
        _0("\uE000",0),
        _1("\uE001",1),
        _2("\uE002",2),
        _3("\uE003",3),
        _4("\uE004",4),
        _5("\uE005",5),
        _6("\uE006",6),
        _7("\uE007",7),
        _8("\uE008",8),
        _9("\uE009",9),
        _10("\uE010",10),
        _overheat1("\uE00A",-1),
        _overheat2("\uE00B",-1),
        _overheat3("\uE00C",-1),
        _overheat4("\uE00D",-1);
        
        
        @Getter
        private final String _char;
        @Getter
        private final int filledPercentage;

        ProgressBarFont(String _char, int filledPercentage) {
            this._char = _char;
            this.filledPercentage = filledPercentage;
        }
    }
}
