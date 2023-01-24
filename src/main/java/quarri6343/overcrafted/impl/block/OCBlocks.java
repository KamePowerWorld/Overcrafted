package quarri6343.overcrafted.impl.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import quarri6343.overcrafted.api.block.IOCBlock;
import quarri6343.overcrafted.api.block.OCBlock;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * ブロックのレジストリ
 */
public enum OCBlocks {

    TRASHCAN(new BlockTrashCan()),
    SUPPLIER(new BlockSupplier()),
    TABLE(new BlockTable(Material.DARK_OAK_PLANKS)),
    CRAFTING(new BlockManualProcessor(Material.CRAFTING_TABLE, "crafting_progression")),
    SMELTING(new BlockAutomaticProcessor(Material.FURNACE, "smelting_progression")),
    WASHING(new BlockManualProcessor(Material.WATER_CAULDRON, "washing_progression")),
    COUNTER(new BlockCounter(Material.RED_BED));

    private final OCBlock ocBlock;

    OCBlocks(OCBlock ocBlock) {
        this.ocBlock = ocBlock;
    }

    public OCBlock get() {
        return ocBlock;
    }

    @Nullable
    public static IOCBlock toOCBlock(Block block) {
        OCBlocks ocBlock = Arrays.stream(OCBlocks.values()).filter(ocBlocks -> ocBlocks.ocBlock.getMaterial() == block.getType()).findFirst().orElse(null);
        return ocBlock == null ? null : ocBlock.get();
    }
}
