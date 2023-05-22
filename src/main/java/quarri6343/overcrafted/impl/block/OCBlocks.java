package quarri6343.overcrafted.impl.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import quarri6343.overcrafted.api.block.IOCBlock;
import quarri6343.overcrafted.core.OCBlock;
import quarri6343.overcrafted.core.data.constant.OCParticleData;
import quarri6343.overcrafted.core.data.constant.OCSoundData;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * ブロックのレジストリ
 */
public enum OCBlocks {

    TRASHCAN(new BlockTrashCan()),
    SUPPLIER(new BlockSupplier()),
    TABLE(new BlockTable(Material.DARK_OAK_PLANKS)),
    TABLE_2(new BlockTable(Material.DARK_OAK_STAIRS)),
    TABLE_3(new BlockTable(Material.DARK_OAK_SLAB)),
    
    CRAFTING(new BlockManualProcessor(Material.CRAFTING_TABLE, "crafting_progression", OCSoundData.craftingSound, OCParticleData.craftParticle)),
    SMELTING(new BlockAutomaticProcessor(Material.FURNACE, "smelting_progression", OCSoundData.smeltingSound, OCParticleData.smeltingParticle)),
    WASHING(new BlockManualProcessor(Material.WATER_CAULDRON, "washing_progression", OCSoundData.washingSound, OCParticleData.washingParticle)),
    FORGING(new BlockManualProcessor(Material.ANVIL, "forging_progression", OCSoundData.forgingSound, OCParticleData.forgingParticle)),
    ENCHANTING(new BlockAutomaticProcessor(Material.ENCHANTING_TABLE, "enchanting_progression", OCSoundData.enchantingSound, OCParticleData.enchantingParticle)),
    COUNTER(new BlockCounter(Material.END_PORTAL_FRAME)),
    CONVEYOR(new BlockConveyor());

    private final OCBlock ocBlock;

    OCBlocks(OCBlock ocBlock) {
        this.ocBlock = ocBlock;
    }

    public OCBlock get() {
        return ocBlock;
    }

    /**
     * バニラのブロックからそれに対応したプラグイン独自の機能のクラスを求める
     * @param block バニラブロック
     * @return カスタム機能
     */
    @Nullable
    public static IOCBlock toOCBlock(Block block) {
        OCBlocks ocBlock = Arrays.stream(OCBlocks.values()).filter(ocBlocks -> ocBlocks.ocBlock.getMaterial() == block.getType()).findFirst().orElse(null);
        return ocBlock == null ? null : ocBlock.get();
    }
}
