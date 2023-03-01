package quarri6343.overcrafted.core.object;

import lombok.Data;
import quarri6343.overcrafted.api.IStageEvent;
import quarri6343.overcrafted.api.item.ISubmittableOCItem;
import quarri6343.overcrafted.api.item.ISupplier;

import java.util.List;

/**
 * ゲームのステージ
 */
@Data
public class OCStage {

    /**
     * ステージの名前
     */
    private final String name;

    /**
     * ステージの制限時間
     */
    private final int time;

    /**
     * 使い終わった皿を洗う必要があるかどうか
     */
    private final boolean enableDishGettingDirty;
    
    /**
     * ステージに登場する納品対象
     */
    private final List<ISubmittableOCItem> products;

    /**
     * ステージに登場する原材料
     */
    private final List<ISupplier> materials;

    /**
     * このステージのハイスコア
     */
    private int highScore = 0;

    /**
     * このステージで起こるイベント
     */
    private final IStageEvent event;
}
