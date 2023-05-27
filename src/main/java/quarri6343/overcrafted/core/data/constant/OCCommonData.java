package quarri6343.overcrafted.core.data.constant;

import java.util.UUID;

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
     * プレイヤーの近くのブロックがプレイヤーを検知してイベントを起こす範囲
     */
    public static final int blockEventTriggerRange = 2;

    /**
     * ゴミ箱に皿を捨てた時それが再び皿置き場に戻るまでの時間
     */
    public static final int dishReturnLag = 100;

    /**
     * システムによって机などに設置されたアイテムに付けられるタグ
     */
    public static final UUID placedItemTag = UUID.fromString("CB3F55D3-645C-4F38-A144-9C13A33DB5CF");
}
