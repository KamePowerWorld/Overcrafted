package quarri6343.overcrafted.impl.block;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.item.IRightClickEventHandler;
import quarri6343.overcrafted.api.item.ISupplier;
import quarri6343.overcrafted.core.OCBlock;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.api.IOCTeam;
import quarri6343.overcrafted.core.OCLogic;
import quarri6343.overcrafted.impl.item.OCItems;

import java.util.Collection;

/**
 * アイテムをプレイヤーに供給するブロック
 */
public class BlockSupplier extends OCBlock implements IRightClickEventHandler {

    public BlockSupplier() {
        super(Material.BARREL);
    }

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }
    
    @Override
    public void onRightClick(PlayerInteractEvent event) {
        supplyItemFrameItemToPlayer(event);
    }

    /**
     * このブロックの上の額縁に載っている料理可能なアイテムをプレイヤーに渡す
     */
    private void supplyItemFrameItemToPlayer(PlayerInteractEvent event){
        if (event.isCancelled())
            return;

        if (getLogic().gameStatus == OCLogic.GameStatus.INACTIVE)
            return;

        IOCTeam team = getData().getTeams().getTeamByPlayer(event.getPlayer());
        if (team == null)
            return;

        event.setCancelled(true);

        if (!event.getAction().isRightClick()) {
            return;
        }

        Collection<Entity> entities = event.getClickedBlock().getWorld().getNearbyEntities(event.getClickedBlock().getRelative(BlockFace.UP).getLocation().add(0.5, 0, 0.5), 0.1, 0.1, 0.1);
        Entity itemFrameEntity = entities.stream().filter(entity -> entity instanceof ItemFrame).findFirst().orElse(null);
        if(itemFrameEntity == null)
            return;
        
        ItemStack itemStackOnSupplier = ((ItemFrame)itemFrameEntity).getItem();
        for (OCItems ocItem : OCItems.values()) {
            if (!(ocItem.get() instanceof ISupplier)) {
                continue;
            }

            if(ocItem.get().getMaterial() == itemStackOnSupplier.getType()){
                ((ISupplier)ocItem.get()).onSupply(event.getPlayer());
                break;
            }
        }
    }
}
