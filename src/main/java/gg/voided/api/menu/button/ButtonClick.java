package gg.voided.api.menu.button;

import gg.voided.api.menu.Menu;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Stores information about a player clicking a menu.
 *
 * @author J4C0B3Y
 * @version MenuAPI
 * @since 12/05/2024
 */
@Getter
public class ButtonClick {
    /**
     * The underlying click event.
     */
    @Getter(AccessLevel.NONE) private final InventoryClickEvent event;

    /**
     * The menu that was clicked.
     */
    private final Menu menu;

    /**
     * The button that was clicked, this can be
     * null if the click was outside the inventory.
     */
    private final Button button;

    /**
     * The player that clicked.
     */
    private final Player player;

    /**
     * The type of the click.
     */
    private final ClickType type;

    /**
     * The click slot, this is -1 if the
     *  click was outside the inventory.
     */
    private final int slot;

    /**
     * If the click should be ignored,
     * and not send to the button's handler.
     */
    @Setter private boolean ignored;

    /**
     * Creates a new button click, extracting
     * values from the underlying click event.
     *
     * @param event The click event.
     * @param button The clicked button, can be null.
     * @param menu The clicked menu.
     */
    public ButtonClick(InventoryClickEvent event, Button button, Menu menu) {
        this.event = event;
        this.menu = menu;
        this.button = button;
        this.player = menu.getPlayer();
        this.type = event.getClick();
        this.slot = event.getSlot();
    }
}
