package quarri6343.overcrafted.common.data;

import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
     * リソースパックのmenuFontのカスタムアイコン名とunicodeの対応表
     */
    public enum MenuFont{
        SPACE(" "),
        TORCH("\uE000"),
        FURNACE("\uE001"),
        IRON("\uE002"),
        MINECART("\uE003"),
        BONUS("\uE004");
        
        private final String _char;
        
        MenuFont(String _char){
            this._char = _char;
        }
        
        public String get_char() {
            return _char;
        }
    }

    /**
     * リソースパックで設定されたカスタムアイテムとオリジナルアイテムとcustomModelDataとの対応表
     */
    public enum customModel{
        DISH(Material.PAPER, 1),
        DISH_DIRTY(Material.PAPER, 2),
        DISH_FURNACE(Material.PAPER, 3),
        DISH_IRON_INGOT(Material.PAPER, 4),
        DISH_MINECART(Material.PAPER, 5),
        DISH_TORCH(Material.PAPER, 6);
        

        private final Material material;
        private final int data;

        customModel(Material material, int data){
            this.material = material;
            this.data = data;
        }

        public Material getMaterial() {
            return material;
        }
        
        public int getData(){
            return data;
        }
    }
}
