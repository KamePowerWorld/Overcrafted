package quarri6343.overcrafted.common.data;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.common.data.interfaces.IOCTeams;
import quarri6343.overcrafted.utils.ItemCreator;

/**
 * 必要なデータを全て保存するクラス
 */
public class OCData {

    /**
     * ゲーム管理者が現在選択しているクラスの名前
     */
    @Getter @Setter
    private String adminSelectedTeam = "";

    /**
     * ゲーム開始前に自動的にプレイヤーをチームに割り当てる周期(tick)
     */
    public static final int assignTeamLength = 20;

    /**
     * ゲームが始まるまでのカウントダウンの長さ(tick)
     */
    public static final int gameBeginCountdownLength = 100;

    /**
     * ゲームの長さ(秒)
     */
    public static final int gameLength = 300;

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
     * インベントリを埋める無効アイテム
     */
    public static final ItemStack invalidItem = new ItemCreator(Material.RED_STAINED_GLASS_PANE).setName(Component.text("")).create();

    @Getter
    private final IOCTeams teams = new OCTeams();
}