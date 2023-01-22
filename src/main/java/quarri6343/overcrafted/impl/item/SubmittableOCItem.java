package quarri6343.overcrafted.impl.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.api.item.CombinedOCItem;
import quarri6343.overcrafted.common.data.OCData;
import quarri6343.overcrafted.common.data.OCTeam;
import quarri6343.overcrafted.common.order.OrderHandler;

public class SubmittableOCItem extends CombinedOCItem implements ISubmittable {
    
    /**
     * メニューを納品した時に得られるスコア
     */
    private final int score;

    /**
     * ボスバーに表示されるメニューを表すカスタムアイコンの文字コード
     */
    private final String iconUniCode;
    
    /**
     * 固有アイテムの型を作製する
     *
     * @param name            作りたい固有アイテムの名前(ユーザーが読むので必ず日本語にすること)
     * @param material        作りたい固有アイテムの元となるバニラアイテム
     * @param internalName    作りたい固有アイテムの内部的な名前<br>
     * @param customModelData
     */
    public SubmittableOCItem(TextComponent name, Material material, String internalName, int customModelData, OCItems ingredient1, OCItems ingredient2, int score, String iconUniCode) {
        super(name, material, internalName, customModelData, ingredient1, ingredient2);
        this.score = score;
        this.iconUniCode = iconUniCode;
    }
    
    private static OCData getData() {
        return Overcrafted.getInstance().getData();
    }

    @Override
    public void onRightClick(PlayerInteractEvent event) {
        super.onRightClick(event);

        OCTeam team = getData().teams.getTeambyPlayer(event.getPlayer());
        if (team == null) {
            event.getPlayer().sendMessage(Component.text("あなたはチームに所属していないため、皿を扱うことができません"));
            return;
        }
        
        trySubmitOrder(event, team);
    }
    
    /**
     * プレイヤーが手に持っている皿をカウンターに提出することを試みる
     */
    private void trySubmitOrder(PlayerInteractEvent event, OCTeam team) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.RED_BED) {
            if (OrderHandler.trySatisfyOrder(team, this)) {
                event.getPlayer().setItemInHand(null);
                team.dirtyDishPile.addDish();
            } else {
                event.getPlayer().sendMessage(Component.text("皿に載っているアイテムは誰も注文していないようだ..."));
            }
        } else {
            event.getPlayer().sendMessage(Component.text("赤いベッドを右クリックして納品しよう"));
        }
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public String toMenuUnicode() {
        return iconUniCode;
    }
}
