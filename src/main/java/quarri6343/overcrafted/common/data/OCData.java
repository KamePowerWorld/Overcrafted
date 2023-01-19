package quarri6343.overcrafted.common.data;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.utils.ItemCreator;

/**
 * 必要なデータを全て保存するクラス
 */
public class OCData {

    /**
     * リソースパックのurl
     */
    public static final String resourcePackURL = "https://drive.google.com/uc?export=download&id=1_ryjm_tb5HVxHwi28Rp3TPk7QSMOSepr";
    
    public static final String resourcePackPath = "plugins/overcrafted_resourcepack.zip";

    /**
     * リソースパックのハッシュ
     */
    public static String resourcePackHash;
    
    /**
     * ゲーム管理者が現在選択しているクラスの名前
     */
    public String adminSelectedTeam = "";

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
     * ゲームが始まった時注文箱に入っている注文の数
     */
    public static final int dishesOnStart = 5;

    /**
     * プレイヤーが投げたアイテムを他のプレイヤーが拾えるまでの時間(tick)
     */
    public static final int thrownItemsPickupDelay = 20;
    
    /**
     * インベントリを埋める無効アイテム
     */
    public static final ItemStack invalidItem = new ItemCreator(Material.RED_STAINED_GLASS_PANE).setName(Component.text("")).create();

    public final OCTeams teams = new OCTeams();
}