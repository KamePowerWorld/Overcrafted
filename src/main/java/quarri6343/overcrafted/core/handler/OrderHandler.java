package quarri6343.overcrafted.core.handler;

import net.kyori.adventure.text.Component;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.api.object.IOCTeam;
import quarri6343.overcrafted.core.OCLogic;
import quarri6343.overcrafted.api.item.ISubmittableOCItem;
import quarri6343.overcrafted.core.data.constant.OCCommonData;
import quarri6343.overcrafted.core.handler.order.BossBarHandler;
import quarri6343.overcrafted.core.handler.order.ScoreBoardHandler;

import java.util.*;

/**
 * 注文のハンドラ
 */
public class OrderHandler {
    private static final Map<IOCTeam, List<ISubmittableOCItem>> ordersMap = new HashMap<>();
    
    private static final Map<IOCTeam, Float> tipsMultiplierMap = new HashMap<>();

    private static OCVariableData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }

    /**
     * 全てのチームに向けてランダムな注文のセットを生成する
     */
    public static void generateRandomOrders() {
        BossBarHandler.hideBossBarFromEveryTeam();

        for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
            List<ISubmittableOCItem> menus = getData().getSelectedStage().get().getProducts();

            List<ISubmittableOCItem> orders = new ArrayList<>();
            for (int j = 0; j < OCCommonData.ordersOnStart; j++) {
                orders.add(menus.get(new Random().nextInt(menus.size())));
            }
            ordersMap.put(getData().getTeams().getTeam(i), orders);
            tipsMultiplierMap.put(getData().getTeams().getTeam(i), 1f);
            BossBarHandler.registerOrUpdateBossBar(getData().getTeams().getTeam(i), orders);
        }
        ScoreBoardHandler.initialize();
    }

    /**
     * あるチーム用のランダムな注文セットを生成する
     * @param team チーム
     */
    public static void generateRandomOrder(IOCTeam team) {
        List<ISubmittableOCItem> menus = getData().getSelectedStage().get().getProducts();;

        List<ISubmittableOCItem> orders = ordersMap.get(team);
        orders.add(menus.get(new Random().nextInt(menus.size())));
        BossBarHandler.registerOrUpdateBossBar(team, orders);
    }

    /**
     * 提出可能なオブジェクトが注文リストに存在するか確認する
     * @param team 提出者のチーム
     * @param submittable オブジェクト
     * @return 存在するか
     */
    public static boolean canSatisfyOrder(IOCTeam team, ISubmittableOCItem submittable) {
        List<ISubmittableOCItem> orders = ordersMap.get(team);
        if (orders.stream().filter(dishMenu -> dishMenu.equals(submittable)).findFirst().orElse(null) != null) {
            return true;
        }

        return false;
    }

    /**
     * 提出可能なオブジェクトが提出されたら注文を1つ消化して補充する
     * 
     * @param team 提出者のチーム
     * @param submittable オブジェクト
     * @return 注文が消化できたか
     */
    public static boolean trySatisfyOrder(IOCTeam team, ISubmittableOCItem submittable) {
        if (!canSatisfyOrder(team, submittable))
            return false;

        List<ISubmittableOCItem> orders = ordersMap.get(team);
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).equals(submittable)) {
                if(i== 0){
                    float tipsMultiplier = tipsMultiplierMap.get(team);
                    tipsMultiplier += getData().getTipMultiplier().get();
                    tipsMultiplierMap.put(team, tipsMultiplier);
                    ScoreBoardHandler.addScore(team, (int)(orders.get(i).getScore() * tipsMultiplier));
                    for (int j = 0; j < team.getPlayersSize(); j++) {
                        team.getPlayer(j).sendActionBar(Component.text("チップによるスコア上昇:" + (float)Math.round(tipsMultiplier * 10) / 10 + "倍"));
                    }
                }
                else{
                    tipsMultiplierMap.put(team, 1f);
                    ScoreBoardHandler.addScore(team, orders.get(i).getScore());
                }
                orders.remove(i);
                generateRandomOrder(team);
                return true;
            }
        }

        return false;
    }

    /**
     * 全ての注文を削除する
     */
    public static void clearOrders() {
        ordersMap.clear();
        tipsMultiplierMap.clear();
        ScoreBoardHandler.destroy();
        BossBarHandler.hideBossBarFromEveryTeam();
    }
}
