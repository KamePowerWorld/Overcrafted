package quarri6343.overcrafted.common.order;

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
    private static Map<IOCTeam, List<ISubmittable>> ordersMap = new HashMap<>();

    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    private static OCLogic getLogic() {
        return Overcrafted.getInstance().getLogic();
    }

    public static void generateRandomOrders() {
        BossBarHandler.hideEverything();

        for (int i = 0; i < getData().teams.getTeamsLength(); i++) {
            List<ISubmittable> menus = new ArrayList<>();
            for (OCItems item : OCItems.values()){
                if(item.get() instanceof ISubmittable)
                    menus.add((ISubmittable) item.get());
            }

            List<ISubmittable> orders = new ArrayList<>();
            for (int j = 0; j < OCData.ordersOnStart; j++) {
                orders.add(menus.get(new Random().nextInt(menus.size())));
            }
            ordersMap.put(getData().teams.getTeam(i), orders);
            BossBarHandler.displayDishMenu(getData().teams.getTeam(i), orders);
        }
        ScoreBoardHandler.initialize();
    }

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

    public static boolean canSatisfyOrder(IOCTeam team, ISubmittable submittable) {
        List<ISubmittable> orders = ordersMap.get(team);
        if (orders.stream().filter(dishMenu -> dishMenu.equals(submittable)).findFirst().orElse(null) != null) {
            return true;
        }

        return false;
    }

    public static boolean trySatisfyOrder(IOCTeam team, ISubmittable submittable) {
        if (!canSatisfyOrder(team, submittable))
            return false;

        List<ISubmittable> orders = ordersMap.get(team);
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).equals(submittable)) {
                ScoreBoardHandler.addScore(team, orders.get(i).getScore());
                orders.remove(i);
                generateRandomOrder(team);
                return true;
            }
        }

        return false;
    }

    public static void clearOrders() {
        ordersMap.clear();
        ScoreBoardHandler.destroy();
        BossBarHandler.hideEverything();
    }
}
