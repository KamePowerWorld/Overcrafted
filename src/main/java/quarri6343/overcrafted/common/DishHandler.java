package quarri6343.overcrafted.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.utils.ItemCreator;
import quarri6343.overcrafted.utils.OvercraftedUtils;

import java.util.Random;

public class DishHandler {

    private static final String nbtID = "menuID";
    private static final String completeNBTID = "isCompleted";
    public static final String dishItemName = "皿";
    
    /**
     * ランダムな注文を皿にエンコードする
     * @return
     */
    public static ItemStack encodeRandomOrder(){
        DishMenu[] menus = DishMenu.values();
        return encodeOrder(menus[new Random().nextInt(menus.length)]);
    }
    
    /**
     * 皿で納品させたい注文を皿(paper)上にエンコードする
     * @param productStack 注文票に載せるアイテムリスト
     * @return 皿のアイテム
     */
    public static ItemStack encodeOrder(DishMenu productStack){
        return new ItemCreator(Material.PAPER).setName(Component.text(dishItemName))
                .setLore(OvercraftedUtils.getItemInfoasText(productStack.getProduct()))
                .setIntNBT(nbtID, productStack.ordinal()).create();
    }

    /**
     * 皿に注文とそれが満たされた状態であることを記録する
     * @param productStack 注文票に載せるアイテムリスト
     * @return 皿のアイテム
     */
    public static ItemStack encodeOrderAsCompleted(DishMenu productStack){
        return new ItemCreator(Material.PAPER).setName(Component.text(dishItemName))
                .setLore(Component.text("注文された品が載っている").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
                .setIntNBT(nbtID, productStack.ordinal())
                .setBooleanNBT(completeNBTID, true).create();
    }

    /**
     * 皿に書かれた注文票の内容を抽出する
     * @param dish 皿のアイテム
     * @return 注文の種類
     */
    public static DishMenu decodeOrder(ItemStack dish){
        Integer index = new ItemCreator(dish).getIntNBT(nbtID);
        if(index == null)
            return null;
        
        DishMenu[] menus = DishMenu.values();
        return menus[index];
    }

    /**
     * 皿の注文が満たされたかどうか
     * @param dish 皿のアイテム
     * @return 注文が満たされたかどうか
     */
    public static boolean isOrderCompleted(ItemStack dish){
        Boolean isCompleted = new ItemCreator(dish).getBooleanNBT(completeNBTID);
        if(isCompleted == null)
            return false;
        
        return isCompleted;
    }
}
