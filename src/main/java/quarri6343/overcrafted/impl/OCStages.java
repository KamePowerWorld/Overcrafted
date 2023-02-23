package quarri6343.overcrafted.impl;

import quarri6343.overcrafted.api.item.ISubmittableOCItem;
import quarri6343.overcrafted.api.item.ISupplier;
import quarri6343.overcrafted.core.object.OCStage;
import quarri6343.overcrafted.impl.item.OCItems;

import java.util.Arrays;
import java.util.List;

/**
 * ステージレジストリ
 */
public enum OCStages {
    TUTORIAL1(new OCStage("チュートリアル1", 600, false,
            List.of((ISubmittableOCItem) OCItems.DISH_FURNACE.get()),
            List.of((ISupplier) OCItems.COBBLESTONE.get()))),

    TUTORIAL2(new OCStage("チュートリアル2", 600, true,
            Arrays.asList((ISubmittableOCItem) OCItems.DISH_IRON_INGOT.get(), (ISubmittableOCItem) OCItems.DISH_FURNACE.get()),
            Arrays.asList((ISupplier) OCItems.IRON_ORE.get(), (ISupplier) OCItems.COBBLESTONE.get()))),

    STAGE1(new OCStage("ステージ1", 210, true,
            Arrays.asList((ISubmittableOCItem) OCItems.DISH_IRON_INGOT.get(), (ISubmittableOCItem) OCItems.DISH_FURNACE.get(), (ISubmittableOCItem) OCItems.DISH_TORCH.get(), (ISubmittableOCItem) OCItems.DISH_MINECART.get()),
            Arrays.asList((ISupplier) OCItems.IRON_ORE.get(), (ISupplier) OCItems.COBBLESTONE.get(), (ISupplier) OCItems.WOOD.get())));

    private final OCStage stage;

    OCStages(OCStage ocStage) {
        this.stage = ocStage;
    }

    public OCStage get() {
        return stage;
    }
}
