package quarri6343.overcrafted.common;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.utils.ItemCreator;
import quarri6343.overcrafted.utils.OvercraftedUtils;

import java.util.Random;

public class DishHandler {

    private static final String nbtID = "MenuID";
    public static final String dishItemName = "皿";
    
    /**
     * ランダムなメニューを皿にエンコードする
     * @return
     */
    public static ItemStack encodeRandom(){
        DishMenu[] menus = DishMenu.values();
        return encode(menus[new Random().nextInt(menus.length)]);
    }
    
    /**
     * 皿で納品させたいアイテムリストを皿(paper)上にエンコードする
     * @param productStack 注文票に載せるアイテムリスト
     * @return 皿のアイテム
     */
    public static ItemStack encode(DishMenu productStack){
        return new ItemCreator(Material.PAPER).setName(Component.text(dishItemName))
                .setLore(OvercraftedUtils.getItemInfoasText(productStack.getProduct()))
                .setIntNBT(nbtID, productStack.ordinal()).create();
    }

    /**
     * 皿に書かれた注文票の内容を抽出する
     * @param dish
     * @return
     */
    public static DishMenu decode(ItemStack dish){
        Integer index = new ItemCreator(dish).getIntNBT(nbtID);
        if(index == null)
            return null;
        
        DishMenu[] menus = DishMenu.values();
        return menus[index];
    }
}
