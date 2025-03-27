package net.j4c0b3y.api.menu.layer.impl;

import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.layer.Layer;
import net.j4c0b3y.api.menu.pagination.PaginationSlot;

import java.util.HashMap;

/**
 * Represents a background layer in a menu with
 * many methods to manipulate the layer's content.
 *
 * @author J4C0B3Y
 * @version MenuAPI
 * @since 12/05/2024
 */
public class BackgroundLayer extends Layer {
    /**
     * Creates a new background layer.
     *
     * @param menu The menu the layer belongs to.
     */
    public BackgroundLayer(Menu menu) {
        super(menu, new HashMap<>());
    }

    /**
     * Returns the name of the background layer.
     *
     * @return The background layer name.
     */
    @Override
    public String getName() {
        return "background";
    }

    /**
     * Adds a button to the next available slot,
     * checking slots in the foreground layer as well.
     *
     * @param button The button to add.
     * @param other If the foreground layer should be checked.
     */
    @Override
    public void add(Button button, boolean other) {
        add(button, other ? getMenu().getForeground() : null);
    }

    /**
     * Checks if a button to be set is invalid,
     * if so sends a warning message.
     * Also prevents pagination slots from
     * being added to the background layer.
     *
     * @param button The button to check
     * @param index The index to check
     * @return If the button or index is invalid.
     */
    @Override
    protected boolean isInvalid(Button button, int index) {
        if (super.isInvalid(button, index)) return true;

        if (button instanceof PaginationSlot) {
            warning("pagination slot", button, index);
            return true;
        }

        return false;
    }
}
