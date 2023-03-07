package quarri6343.overcrafted.core.data;

import lombok.Getter;
import lombok.Setter;
import quarri6343.overcrafted.api.object.IOCTeams;
import quarri6343.overcrafted.core.object.OCTeams;
import quarri6343.overcrafted.core.object.RangedFloat;
import quarri6343.overcrafted.core.object.RangedInt;
import quarri6343.overcrafted.impl.OCStages;

/**
 * ゲーム進行に関わるデータを全て保存するクラス
 */
public class OCVariableData {

    /**
     * ゲーム管理者が現在選択しているクラスの名前
     */
    @Getter @Setter
    private String adminSelectedTeam = "";

    /**
     * ゲーム管理者が現在選択しているステージ
     */
    @Getter @Setter
    private OCStages selectedStage = null;
    
    /**
     * 登録されている全てのチーム
     */
    @Getter
    private final IOCTeams teams = new OCTeams();

    /**
     * 人力で行うクラフトにかかる時間(tick)
     */
    @Getter
    private final RangedInt craftingTime = new RangedInt(60,1,Integer.MAX_VALUE);

    /**
     * 時間をかけるクラフトにかかる時間(tick)
     */
    @Getter
    private final RangedInt cookingTime = new RangedInt(200,1,Integer.MAX_VALUE);

    /**
     * 時間をかけるクラフトの成果物が燃えるまでの時間(tick)
     */
    @Getter
    private final RangedInt burnTime = new RangedInt(200,1,Integer.MAX_VALUE);

    /**
     * 連続して適切な注文を納品されることで追加されるスコアの倍率
     */
    @Getter
    private final RangedFloat tipMultiplier = new RangedFloat(0.1f,0.1f,10);
}