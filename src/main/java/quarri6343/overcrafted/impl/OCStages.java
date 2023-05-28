package quarri6343.overcrafted.impl;

import quarri6343.overcrafted.api.item.ISubmittableOCItem;
import quarri6343.overcrafted.api.item.ISupplier;
import quarri6343.overcrafted.core.object.OCStage;
import quarri6343.overcrafted.core.stageevent.PlayerPosSwapEvent;
import quarri6343.overcrafted.core.stageevent.WindEvent;
import quarri6343.overcrafted.core.stageevent.ZombieSpawnEvent;
import quarri6343.overcrafted.impl.item.OCItems;

import java.util.Arrays;
import java.util.List;

/**
 * ステージレジストリ
 */
public enum OCStages {
    
    /**
     * ・ステージ1
     * 石->かまど
     * 作業台の使い方
     * 机の使い方
     * ギミックなし
     */
    TUTORIAL1(new OCStage("ステージ1", 600, false,
            List.of((ISubmittableOCItem) OCItems.DISH_FURNACE.get()),
            List.of((ISupplier) OCItems.COBBLESTONE.get()), null)),

    /**
     * ・ステージ2
     * 鉄鉱石->鉄インゴット 、かまど
     * かまどの使い方
     */
    TUTORIAL2(new OCStage("ステージ2", 600, false,
            Arrays.asList((ISubmittableOCItem) OCItems.DISH_IRON_INGOT.get(), (ISubmittableOCItem) OCItems.DISH_FURNACE.get()),
            Arrays.asList((ISupplier) OCItems.IRON_ORE.get(), (ISupplier) OCItems.COBBLESTONE.get()), null)),

    /**
     * ・ステージ3
     * 皿洗いのやり方
     */
    STAGE1(new OCStage("ステージ3", 300, true,
            Arrays.asList((ISubmittableOCItem) OCItems.DISH_IRON_INGOT.get(), (ISubmittableOCItem) OCItems.DISH_FURNACE.get()),
            Arrays.asList((ISupplier) OCItems.IRON_ORE.get(), (ISupplier) OCItems.COBBLESTONE.get()), null)),

    /**
     * ・ステージ4
     * (原木->木材->棒 + 原木->木炭)->松明、鉄インゴット、かまど
     */
    STAGE2(new OCStage("ステージ4", 400, true,
            Arrays.asList((ISubmittableOCItem) OCItems.DISH_TORCH.get(), (ISubmittableOCItem) OCItems.DISH_IRON_INGOT.get(),(ISubmittableOCItem) OCItems.DISH_FURNACE.get()),
            Arrays.asList((ISupplier) OCItems.IRON_ORE.get(), (ISupplier) OCItems.COBBLESTONE.get(), (ISupplier) OCItems.WOOD.get()), null)),

    /**
     * ・ステージ5
     * ギミック：プレイヤーの位置がランダムに入れ替わる
     */
    STAGE3(new OCStage("ステージ5", 480, true,
            Arrays.asList((ISubmittableOCItem) OCItems.DISH_TORCH.get(), (ISubmittableOCItem) OCItems.DISH_IRON_INGOT.get(),(ISubmittableOCItem) OCItems.DISH_FURNACE.get()),
            Arrays.asList((ISupplier) OCItems.IRON_ORE.get(), (ISupplier) OCItems.COBBLESTONE.get(), (ISupplier) OCItems.WOOD.get()), new PlayerPosSwapEvent())),

    /**
     * ・ステージ6
     * (鉄鉱石->鉄インゴット + 原木->木材->棒)->鉄と棒—(金床)->鉄の剣、松明、鉄インゴット, かまど
     * 金床(手動加工装置)初登場
     */
    STAGE4(new OCStage("ステージ6", 480, true,
            Arrays.asList((ISubmittableOCItem) OCItems.DISH_IRON_SWORD.get(), (ISubmittableOCItem) OCItems.DISH_TORCH.get(), (ISubmittableOCItem) OCItems.DISH_IRON_INGOT.get(), (ISubmittableOCItem) OCItems.DISH_FURNACE.get()),
            Arrays.asList((ISupplier) OCItems.IRON_ORE.get(), (ISupplier) OCItems.COBBLESTONE.get(), (ISupplier) OCItems.WOOD.get()), null)),

    /**
     * ・ファイナルステージ
     * 今までの全てのメニュー
     * ギミック：ステージが夜になり、難易度がEASYになり、ランダムにゾンビが襲ってくる。
     * ゾンビは素手または鉄の剣で倒せる
     */
    FINAL_STAGE(new OCStage("ファイナルステージ", 720, true,
            Arrays.asList((ISubmittableOCItem) OCItems.DISH_IRON_SWORD.get(), (ISubmittableOCItem) OCItems.DISH_TORCH.get(), (ISubmittableOCItem) OCItems.DISH_IRON_INGOT.get(), (ISubmittableOCItem) OCItems.DISH_FURNACE.get()),
            Arrays.asList((ISupplier) OCItems.IRON_ORE.get(), (ISupplier) OCItems.COBBLESTONE.get(), (ISupplier) OCItems.WOOD.get()), new ZombieSpawnEvent()));
    
    private final OCStage stage;

    OCStages(OCStage ocStage) {
        this.stage = ocStage;
    }

    public OCStage get() {
        return stage;
    }
}
