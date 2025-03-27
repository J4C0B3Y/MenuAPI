package net.j4c0b3y.api.menu.template;

import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;

/**
 * Used to apply a reusable design preset to a menus layers.
 *
 * @author J4C0B3Y
 * @version MenuAPI
 * @since 12/05/2024
 */
public interface Template {
    /**
     * Applies a design to a menu.
     *
     * @param background The menu's background layer.
     * @param foreground The menu's foreground layer.
     */
    void apply(BackgroundLayer background, ForegroundLayer foreground);
}
