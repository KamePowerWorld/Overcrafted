package quarri6343.overcrafted.common.data;

/**
 * 必要なデータを全て保存するクラス
 */
public class OCData {

    /**
     * ゲーム管理者が現在選択しているクラスの名前
     */
    public String adminSelectedTeam = "";

    /**
     * ゲームが始まるまでのカウントダウンの長さ
     */
    public static final int gameBeginCountdownLength = 100;

    /**
     * ゲームの長さ
     */
    public static final int gameLength = 180;
    
    /**
     * ゲームのリザルトシーンの長さ
     */
    public static final int gameResultSceneLength = 100;

    /**
     * ゲームがプレイヤーのインベントリを確認する周期
     */
    public static final int checkInventoryInterval = 20;

    public final OCTeams teams = new OCTeams();
    
    public OrderBox orderBox = new OrderBox();
}