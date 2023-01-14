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

    /**
     * インベントリを埋める無効アイテム
     */
    public static final ItemStack invalidItem = new ItemCreator(Material.RED_STAINED_GLASS_PANE).setName(Component.text("")).create();

    public final OCTeams teams = new OCTeams();
}