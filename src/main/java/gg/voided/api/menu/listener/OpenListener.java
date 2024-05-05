package gg.voided.api.menu.listener;

import gg.voided.api.menu.Menu;
import gg.voided.api.menu.MenuHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

@RequiredArgsConstructor
public class OpenListener implements Listener {
    private final MenuHandler handler;

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;

        Menu menu = handler.getOpenedMenus().get((Player) event.getPlayer());
        if (menu == null) return;

        if (event.getInventory().equals(menu.getInventory())) return;
        menu.close();
    }
}
