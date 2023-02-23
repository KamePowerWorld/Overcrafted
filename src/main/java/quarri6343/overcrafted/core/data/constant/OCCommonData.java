package quarri6343.overcrafted.core.data.constant;

public class OCCommonData {

    /**
     * ゲーム開始前に自動的にプレイヤーをチームに割り当てる周期(tick)
     */
    public static final int assignTeamLength = 20;

    /**
     * ゲームが始まるまでのカウントダウンの長さ(tick)
     */
    public static final int gameBeginCountdownLength = 100;

    /**
     * ゲームのリザルトシーンの長さ(tick)
     */
    public static final int gameResultSceneLength = 100;

    /**
     * ゲームがプレイヤーのインベントリを確認する周期(tick)
     */
    public static final int checkInventoryInterval = 20;

    /**
     * ゲームが皿置場における最大の皿の数
     */
    public static final int maxDishesNumber = 5;

    /**
     * ゲームで同時に表示される注文の数
     */
    public static final int ordersOnStart = 5;

    /**
     * プレイヤーが投げたアイテムを他のプレイヤーが拾えるまでの時間(tick)
     */
    public static final int thrownItemsPickupDelay = 20;

    /**
     * プレイヤーの近くのブロックがプレイヤーを検知してイベントを起こす範囲
     */
    public static final int blockEventTriggerRange = 2;

    /**
     * 人力で行うクラフトにかかる時間(tick)
     */
    public static final int craftingTime = 40;

    /**
     * 時間をかけるクラフトにかかる時間(tick)
     */
    public static final int cookingTime = 100;

    /**
     * 時間をかけるクラフトの成果物が燃えるまでの時間(tick)
     */
    public static final int burnTime = 100;

    /**
     * ゴミ箱に皿を捨てた時それが再び皿置き場に戻るまでの時間
     */
    public static final int dishReturnLag = 100;

    /**
     * 連続して適切な注文を納品されることで追加されるスコアの倍率
     */
    public static final float tipMultiplierValue = 0.1f;
}
