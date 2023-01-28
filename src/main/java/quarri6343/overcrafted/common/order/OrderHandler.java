package quarri6343.overcrafted.common.order;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.item.OCItem;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.data.interfaces.IOCTeam;
import quarri6343.overcrafted.common.logic.OCLogic;
import quarri6343.overcrafted.impl.item.ISubmittable;
import quarri6343.overcrafted.impl.item.OCItems;

import java.util.*;

/**
 * 注文のハンドラ
 */
public class OrderHandler {
    private static final Map<IOCTeam, List<ISubmittable>> ordersMap = new HashMap<>();
    
    private static final Map<IOCTeam, Float> tipsMultiplierMap = new HashMap<>();

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }

    /**
     * 全てのチームに向けてランダムな注文のセットを生成する
     */
    public static void generateRandomOrders() {
        BossBarHandler.hideEverything();

        for (int i = 0; i < getData().getTeams().getTeamsLength(); i++) {
            List<ISubmittable> menus = new ArrayList<>();
            for (OCItems item : OCItems.values()){
                if(item.get() instanceof ISubmittable)
                    menus.add((ISubmittable) item.get());
            }

            List<ISubmittable> orders = new ArrayList<>();
            for (int j = 0; j < OCData.ordersOnStart; j++) {
                orders.add(menus.get(new Random().nextInt(menus.size())));
            }
            ordersMap.put(getData().getTeams().getTeam(i), orders);
            tipsMultiplierMap.put(getData().getTeams().getTeam(i), 1f);
            BossBarHandler.displayDishMenu(getData().getTeams().getTeam(i), orders);
        }
        ScoreBoardHandler.initialize();
    }

    /**
     * あるチーム用のランダムな注文セットを生成する
     * @param team チーム
     */
    public static void generateRandomOrder(IOCTeam team) {
        List<ISubmittable> menus = new ArrayList<>();
        for (OCItems item : OCItems.values()){
            if(item.get() instanceof ISubmittable)
                menus.add((ISubmittable) item.get());
        }

        List<ISubmittable> orders = ordersMap.get(team);
        orders.add(menus.get(new Random().nextInt(menus.size())));
        BossBarHandler.displayDishMenu(team, orders);
    }

    /**
     * 提出可能なオブジェクトが注文リストに存在するか確認する
     * @param team 提出者のチーム
     * @param submittable オブジェクト
     * @return 存在するか
     */
    public static boolean canSatisfyOrder(IOCTeam team, ISubmittable submittable) {
        List<ISubmittable> orders = ordersMap.get(team);
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
    public static boolean trySatisfyOrder(IOCTeam team, ISubmittable submittable) {
        if (!canSatisfyOrder(team, submittable))
            return false;

        List<ISubmittable> orders = ordersMap.get(team);
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).equals(submittable)) {
                if(i== 0){
                    float tipsMultiplier = tipsMultiplierMap.get(team);
                    tipsMultiplier += 0.1;
                    tipsMultiplierMap.put(team, tipsMultiplier);
                    ScoreBoardHandler.addScore(team, (int)(orders.get(i).getScore() * tipsMultiplier));
                    for (int j = 0; j < team.getPlayersSize(); j++) {
                        team.getPlayer(j).sendMessage(Component.text("チップによるスコア上昇:" + Math.round(tipsMultiplier) + "倍"));
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
        BossBarHandler.hideEverything();
    }
}
