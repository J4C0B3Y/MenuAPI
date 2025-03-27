package net.j4c0b3y.api.menu.layer.impl;

import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.layer.Layer;

import java.util.TreeMap;

/**
 * Represents a foreground layer in a menu with
 * many methods to manipulate the layer's content.
 *
 * @author J4C0B3Y
 * @version MenuAPI
 * @since 12/05/2024
 */
public class ForegroundLayer extends Layer {

    /**
     * Creates a new foreground layer.
     *
     * @param menu The menu the layer belongs to.
     */
    public ForegroundLayer(Menu menu) {
        super(menu, new TreeMap<>());
    }

    /**
     * Returns the name of the foreground layer.
     *
     * @return The foreground layer name.
     */
    @Override
    public String getName() {
        return "foreground";
    }

    /**
     * Adds a button to the next available slot,
     * checking slots in the background layer as well.
     *
     * @param button The button to add.
     * @param other If the background layer should be checked.
     */
    @Override
    public void add(Button button, boolean other) {
        add(button, other ? getMenu().getBackground() : null);
    }
}
