package quarri6343.overcrafted.api.item;

import it.unimi.dsi.fastutil.Pair;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.item.interfaces.ICombinedOCItem;
import quarri6343.overcrafted.api.item.interfaces.IOCItem;
import quarri6343.overcrafted.api.item.interfaces.IRightClickEventHandler;
import quarri6343.overcrafted.common.PlaceItemHandler;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.impl.item.OCItems;

public class InteractableOCItem extends OCItem implements IRightClickEventHandler {

    private static final Material blockCanPlaceItem = Material.DARK_OAK_PLANKS;

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }
    
    /**
     * 固有アイテムの型を作製する
     *
     * @param name            作りたい固有アイテムの名前(ユーザーが読むので必ず日本語にすること)
     * @param material        作りたい固有アイテムの元となるバニラアイテム
     * @param internalName    作りたい固有アイテムの内部的な名前<br>
     * @param customModelData
     */
    public InteractableOCItem(TextComponent name, Material material, String internalName, int customModelData) {
        super(name, material, internalName, customModelData);
    }

    @Override
    public void onRightClick(PlayerInteractEvent event) {
        if (event.isCancelled())
            return;

        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;

        if (event.getClickedBlock() == null || event.getClickedBlock().getType() != blockCanPlaceItem)
            return;

        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null) {
            return;
        }

        event.setCancelled(true);

        if (event.getItem().getType() == OCData.invalidItem.getType())
            return;

        if(PlaceItemHandler.getItem(event.getClickedBlock()) != null){
            IOCItem ocItem1 = ItemManager.toOCItem(event.getItem());
            IOCItem ocItem2 = ItemManager.toOCItem(PlaceItemHandler.getItem(event.getClickedBlock()));

            for(OCItems ocItem : OCItems.values()){
                if(!(ocItem.get() instanceof ICombinedOCItem)){
                    continue;
                }

                Pair<OCItems, OCItems> ingredients = ((ICombinedOCItem)ocItem.get()).getIngredients();
                if((ingredients.left().get().equals(ocItem1) && ingredients.right().get().equals(ocItem2))
                        || (ingredients.left().get().equals(ocItem2) && ingredients.right().get().equals(ocItem1))){
                    PlaceItemHandler.pickUpItem(event.getClickedBlock());
                    event.getPlayer().setItemInHand(ocItem.get().getItemStack());
                    return;
                }
            }
        } else if(PlaceItemHandler.placeItem(event.getClickedBlock(), event.getItem())) {
            event.getPlayer().setItemInHand(null);
        }
    }
}
