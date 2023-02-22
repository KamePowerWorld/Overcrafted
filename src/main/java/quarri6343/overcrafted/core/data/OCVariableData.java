package quarri6343.overcrafted.core.data;

import lombok.Getter;
import lombok.Setter;
import quarri6343.overcrafted.api.IOCTeams;
import quarri6343.overcrafted.core.object.OCTeams;

/**
 * ゲーム進行に関わるデータを全て保存するクラス
 */
public class OCVariableData {

    /**
     * ゲーム管理者が現在選択しているクラスの名前
     */
    @Getter @Setter
    private String adminSelectedTeam = "";

    @Getter
    private final IOCTeams teams = new OCTeams();
}