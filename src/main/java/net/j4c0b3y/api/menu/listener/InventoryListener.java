package net.j4c0b3y.api.menu.listener;

import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Handles inventories being clicked or closed,
 * also closing the inventory when players quit.
 *
 * @author J4C0B3Y
 * @version MenuAPI
 * @since 12/05/2024
 */
@RequiredArgsConstructor
public class InventoryListener implements Listener {
    /**
     * The menu handler that registered the listener.
     */
    private final MenuHandler handler;

    /**
     * Sends a click event to a player's menu if they have one
     * open, cancelling the click, so they can't pick up the item.
     *
     * @param event The click event.
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        handler.ifOpen((Player) event.getWhoClicked(), menu -> {
            event.setCancelled(true);
            menu.click(event);
        });
    }

    /**
     * When an inventory is closed, the menu
     * gets closed and remove from the open menus map.
     *
     * @param event The close event.
     */
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }

        handler.ifOpen((Player) event.getPlayer(), menu ->
            menu.close(false)
        );
    }

    /**
     * Closes a players menu when they leave the game.
     *
     * @param event The quit event.
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        handler.ifOpen(event.getPlayer(), Menu::close);
    }
}
