package quarri6343.overcrafted.core.ui;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import quarri6343.overcrafted.Overcrafted;
import quarri6343.overcrafted.core.data.OCVariableData;
import quarri6343.overcrafted.utils.ItemCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理者が設定したいチームを選択する用のGUI
 */
public class UIAdminSelectTeam {

    public static void openUI(Player player) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text("設定用チームセレクタ").color(NamedTextColor.GRAY))
                .rows(3)
                .pageSize(27)
                .disableAllInteractions()
                .create();

        OCVariableData data = Overcrafted.getInstance().getData();
        if (data.getTeams().getTeamsLength() == 0) {
            GuiItem closeButton = new GuiItem(new ItemCreator(Material.BARRIER)
                    .setName(Component.text("まず/create team {チーム名} {チームの色}でチームを作ってください").color(NamedTextColor.WHITE)).create(),
                    event -> gui.close(event.getWhoClicked()));
            gui.setItem(22, closeButton);
        } else {
            for (int i = 0; i < data.getTeams().getTeamsLength(); i++) {
                List<Component> lores = new ArrayList<>();
                lores.add(Component.text("プレイヤー数: " + data.getTeams().getTeam(i).getPlayersSize()));
                lores.addAll(data.getTeams().getTeam(i).playerNamesToText());

                ItemStack teamSelectItem = new ItemCreator(Material.WHITE_WOOL)
                        .setName(Component.text("チーム" + data.getTeams().getTeam(i).getName() + "を選択"))
                        .setLores(lores).create();
                GuiItem teamSelectButton = new GuiItem(teamSelectItem,
                        event -> {
                            data.setAdminSelectedTeam(data.getTeams().getTeam(event.getSlot()).getName());
                            event.getWhoClicked().sendMessage("チーム" + data.getAdminSelectedTeam() + "を選択しました");
                            UIAdminMenu.openUI((Player) event.getWhoClicked());
                        });
                gui.setItem(i, teamSelectButton);
            }
        }

        gui.open(player);
    }
}
