package quarri6343.unredstone.impl;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import quarri6343.unredstone.UnRedstone;
import quarri6343.unredstone.UnRedstoneData;
import quarri6343.unredstone.UnRedstoneLogic;
import quarri6343.unredstone.utils.ItemCreator;
import quarri6343.unredstone.utils.UnRedstoneUtils;

public class UIMenu {
    
    public static void openUI(Player player) {
        UnRedstoneData data = UnRedstone.getInstance().config.data;
        
        PaginatedGui gui = Gui.paginated()
                .title(Component.text("管理メニュー"))
                .rows(3)
                .pageSize(27)
                .disableAllInteractions()
                .create();
        
        GuiItem setStartButton = new GuiItem(new ItemCreator(Material.FURNACE_MINECART).setName(Component.text("開始地点を設定"))
                .setLore(getLocDisc(data.startLocation)).create(),
                event -> {
                    data.startLocation = event.getWhoClicked().getLocation();
                    event.getWhoClicked().sendMessage(Component.text("開始地点を" + UnRedstoneUtils.locationBlockPostoString(event.getWhoClicked().getLocation()) + "に設定しました"));
                    openUI((Player)event.getWhoClicked());
                });
        gui.setItem(1, setStartButton);
//        GuiItem setRelay1Button = new GuiItem(new ItemCreator(Material.BEACON).setName(Component.text("中継地点1を設定")).create(),
//                event -> {
//                    data.relayLocation1 = event.getWhoClicked().getLocation();
//                    event.getWhoClicked().sendMessage(Component.text("中継地点1を" + UnRedstoneUtils.locationBlockPostoString(event.getWhoClicked().getLocation()) + "に設定しました"));
//                });
//        gui.setItem(3, setRelay1Button);
//        GuiItem setRelay2Button = new GuiItem(new ItemCreator(Material.BEACON).setName(Component.text("中継地点2を設定")).create(),
//                event -> {
//                    data.relayLocation2 = event.getWhoClicked().getLocation();
//                    event.getWhoClicked().sendMessage(Component.text("中継地点2を" + UnRedstoneUtils.locationBlockPostoString(event.getWhoClicked().getLocation()) + "に設定しました"));
//                });
//        gui.setItem(5, setRelay2Button);
        GuiItem setEndButton = new GuiItem(new ItemCreator(Material.DETECTOR_RAIL).setName(Component.text("終了地点を設定"))
                .setLore(getLocDisc(data.endLocation)).create(),
                event -> {
                    data.endLocation = event.getWhoClicked().getLocation();
                    event.getWhoClicked().sendMessage(Component.text("終了地点を" + UnRedstoneUtils.locationBlockPostoString(event.getWhoClicked().getLocation()) + "に設定しました"));
                    openUI((Player)event.getWhoClicked());
                });
        gui.setItem(7, setEndButton);

        GuiItem startButton = new GuiItem(new ItemCreator(Material.GREEN_WOOL).setName(Component.text("ゲームを開始")).setLore(getCanStartGameDisc()).create(),
                event -> {
                    UnRedstone.getInstance().logic.startGame((Player)event.getWhoClicked());
                    openUI((Player)event.getWhoClicked());
                });
        gui.setItem(11, startButton);
        GuiItem endButton = new GuiItem(new ItemCreator(Material.RED_WOOL).setName(Component.text("ゲームを強制終了")).setLore(getCanTerminateGameDisc()).create(),
                event -> {
                    UnRedstone.getInstance().logic.endGame((Player)event.getWhoClicked(), UnRedstoneLogic.GameResult.FAIL);
                    openUI((Player)event.getWhoClicked());
                });
        gui.setItem(15, endButton);

        GuiItem closeButton = new GuiItem(new ItemCreator(Material.BARRIER).setName(Component.text("閉じる")).create(),
                event -> {
                    gui.close(event.getWhoClicked());
                });
        gui.setItem(22, closeButton);

        gui.open(player);
    }
    
    public static TextComponent getLocDisc(Location location){
        return Component.text(location != null? "現在：" + UnRedstoneUtils.locationBlockPostoString(location) : "未設定です")
                .color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false);
    }

    public static TextComponent getCanStartGameDisc(){
        return UnRedstone.getInstance().logic.gameStatus == UnRedstoneLogic.GameStatus.INACTIVE ?
                Component.text("開始可能" ).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                : Component.text("ゲームが進行中です!" ).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false);
    }

    public static TextComponent getCanTerminateGameDisc(){
        return UnRedstone.getInstance().logic.gameStatus == UnRedstoneLogic.GameStatus.ACTIVE ?
                Component.text("強制終了可能" ).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                : Component.text("進行中のゲームはありません" ).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false);
    }
}
