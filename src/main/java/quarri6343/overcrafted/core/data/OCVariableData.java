package quarri6343.overcrafted.core.data;

import lombok.Getter;
import lombok.Setter;
import quarri6343.overcrafted.api.object.IOCTeams;
import quarri6343.overcrafted.core.object.OCTeams;
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
}