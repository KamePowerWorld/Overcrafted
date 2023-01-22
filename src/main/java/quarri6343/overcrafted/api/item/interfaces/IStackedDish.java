package quarri6343.overcrafted.api.item.interfaces;

import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.api.item.StackedDish;

public interface IStackedDish {
    public int getStackedNumber();
    public StackedDish.StackedDishType getType();
    public ItemStack getItemStack();
}
