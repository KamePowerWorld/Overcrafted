package quarri6343.overcrafted.impl.item;

import com.google.common.base.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.player.PlayerInteractEvent;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.item.InteractableOCItem;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;

public class WashableOCItem extends InteractableOCItem {
    
    /**
     * 固有アイテムの型を作製する
     *
     * @param name            作りたい固有アイテムの名前(ユーザーが読むので必ず日本語にすること)
     * @param material        作りたい固有アイテムの元となるバニラアイテム
     * @param internalName    作りたい固有アイテムの内部的な名前<br>
     * @param customModelData
     */
    public WashableOCItem(TextComponent name, Material material, String internalName, int customModelData) {
        super(name, material, internalName, customModelData);
    }

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }
    
    @Override
    public void onRightClick(PlayerInteractEvent event) {
        super.onRightClick(event);

        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null) {
            return;
        }
        
        tryWashDish(event, team);
    }


    /**
     * プレイヤーが手に持っている汚いアイテムを洗うことを試みる
     *
     * @param event
     */
    private void tryWashDish(PlayerInteractEvent event, OCTeam team) {
        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;

        if (event.getClickedBlock() == null || !Objects.equal(event.getClickedBlock().getType(), Material.WATER_CAULDRON)) {
            event.getPlayer().sendMessage(Component.text("水の入った大釜を右クリックして洗おう"));
            return;
        }

        Levelled cauldronData = (Levelled) event.getClickedBlock().getBlockData();
        if (cauldronData.getLevel() == cauldronData.getMinimumLevel()) {
            event.getClickedBlock().getWorld().setType(event.getClickedBlock().getLocation(), Material.CAULDRON);
        } else {
            cauldronData.setLevel(cauldronData.getLevel() - 1);
            event.getClickedBlock().setBlockData(cauldronData);
        }

        event.getPlayer().setItemInHand(null);
        team.cleanDishPile.addDish();

        event.getClickedBlock().getState().update();
    }
}
