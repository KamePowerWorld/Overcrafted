package quarri6343.overcrafted.impl;

import quarri6343.overcrafted.api.item.ISubmittableOCItem;
import quarri6343.overcrafted.api.item.ISupplier;
import quarri6343.overcrafted.core.object.OCStage;
import quarri6343.overcrafted.core.stageevent.PlayerPosSwapEvent;
import quarri6343.overcrafted.core.stageevent.ZombieSpawnEvent;
import quarri6343.overcrafted.impl.item.OCItems;

import java.util.Arrays;
import java.util.List;

/**
 * ステージレジストリ
 */
public enum OCStages {
    
    /**
     * ・チュートリアル1
     * 石->かまど
     * 作業台の使い方
     * 机の使い方
     * ギミックなし
     */
    TUTORIAL1(new OCStage("チュートリアル1", 600, false,
            List.of((ISubmittableOCItem) OCItems.DISH_FURNACE.get()),
            List.of((ISupplier) OCItems.COBBLESTONE.get()), null)),

    /**
     * ・チュートリアル2
     * 鉄鉱石->鉄インゴット 、かまど
     * かまどの使い方
     * 皿洗いのやり方
     * ギミック：鉄のドア(壁を越えてのアイテムの投げ方をここで学ぶ)
     */
    TUTORIAL2(new OCStage("チュートリアル2", 600, true,
            Arrays.asList((ISubmittableOCItem) OCItems.DISH_IRON_INGOT.get(), (ISubmittableOCItem) OCItems.DISH_FURNACE.get()),
            Arrays.asList((ISupplier) OCItems.IRON_ORE.get(), (ISupplier) OCItems.COBBLESTONE.get()), null)),

    /**
     * ・ステージ1
     * (原木->木材->棒 + 原木->木炭)->松明、鉄インゴット、かまど
     * ギミック：スライムブロックでキッチンの穴をジャンプで飛び越えないといけない
     */
    STAGE1(new OCStage("ステージ1", 210, true,
            Arrays.asList((ISubmittableOCItem) OCItems.DISH_TORCH.get(), (ISubmittableOCItem) OCItems.DISH_IRON_INGOT.get(), (ISubmittableOCItem) OCItems.DISH_FURNACE.get()),
            Arrays.asList((ISupplier) OCItems.IRON_ORE.get(), (ISupplier) OCItems.COBBLESTONE.get(), (ISupplier) OCItems.WOOD.get()), null)),

    /**
     * ・ステージ2
     * (鉄鉱石->鉄インゴット + 原木->木材->棒)->鉄と棒—(金床)->鉄の剣、松明、鉄インゴット
     * 金床(手動加工装置)初登場
     * ギミック：ステージが夜になり、難易度がEASYになり、ランダムにゾンビが襲ってくる。
     * ゾンビは素手または鉄の剣で倒せる
     */
    STAGE2(new OCStage("ステージ2", 240, true,
            Arrays.asList((ISubmittableOCItem) OCItems.DISH_IRON_SWORD.get(), (ISubmittableOCItem) OCItems.DISH_TORCH.get(), (ISubmittableOCItem) OCItems.DISH_IRON_INGOT.get()),
            Arrays.asList((ISupplier) OCItems.IRON_ORE.get(), (ISupplier) OCItems.COBBLESTONE.get(), (ISupplier) OCItems.WOOD.get()), new ZombieSpawnEvent())),

    /**
     * ・ステージ3
     * (金鉱石->金 + 葉っぱ->りんご) ->金とりんご—(エンチャント台)->金りんご、りんご、鉄インゴット
     * エンチャント台(時間加工型装置)初登場
     * ギミック：プレイヤーの位置がランダムに入れ替わる
     */
    STAGE3(new OCStage("ステージ3", 240, true,
            Arrays.asList((ISubmittableOCItem) OCItems.DISH_GOLDEN_APPLE.get(), (ISubmittableOCItem) OCItems.DISH_APPLE.get(), (ISubmittableOCItem) OCItems.DISH_IRON_INGOT.get()),
            Arrays.asList((ISupplier) OCItems.LEAVES.get(), (ISupplier) OCItems.GOLD_ORE.get(), (ISupplier) OCItems.IRON_ORE.get()), new PlayerPosSwapEvent())),

    /**
     * ・ステージ4
     * (ソウルサンド->ソウルサンドの台 + ウィザースケルトンの頭)->ウィザー-(エンチャント台)>ネザースター、松明、鉄の剣
     * ギミック：強風。プレイヤーの位置が特定方向に押し返されるので、レバーで起動する壁で対抗しなくてはいけない
     */
    STAGE4(new OCStage("ステージ4", 240, true,
            Arrays.asList((ISubmittableOCItem) OCItems.DISH_NETHER_STAR.get(), (ISubmittableOCItem) OCItems.DISH_TORCH.get(), (ISubmittableOCItem) OCItems.DISH_IRON_SWORD.get()),
            Arrays.asList((ISupplier) OCItems.SOUL_SAND.get(), (ISupplier) OCItems.WITHER_SKULL.get(), (ISupplier) OCItems.WOOD.get(), (ISupplier) OCItems.IRON_ORE.get()), null)),

    /**
     * ・ファイナルステージ
     * 今までの全てのメニュー
     * ギミック：夜、ランダムテレポート、強風がローテで発生
     */
    FINAL_STAGE(new OCStage("ファイナルステージ", 360, true,
           Arrays.asList((ISubmittableOCItem) OCItems.DISH_NETHER_STAR.get(), (ISubmittableOCItem) OCItems.DISH_GOLDEN_APPLE.get(),(ISubmittableOCItem) OCItems.DISH_APPLE.get(),
                   (ISubmittableOCItem) OCItems.DISH_IRON_SWORD.get(), (ISubmittableOCItem) OCItems.DISH_TORCH.get(),(ISubmittableOCItem) OCItems.DISH_IRON_INGOT.get(),(ISubmittableOCItem) OCItems.DISH_FURNACE.get()),
            Arrays.asList((ISupplier) OCItems.SOUL_SAND.get(), (ISupplier) OCItems.WITHER_SKULL.get(), (ISupplier) OCItems.WOOD.get(),(ISupplier) OCItems.LEAVES.get(), (ISupplier) OCItems.GOLD_ORE.get(),
                    (ISupplier) OCItems.WOOD.get(), (ISupplier) OCItems.IRON_ORE.get()), new ZombieSpawnEvent()));
    
    private final OCStage stage;

    OCStages(OCStage ocStage) {
        this.stage = ocStage;
    }

    public OCStage get() {
        return stage;
    }
}
