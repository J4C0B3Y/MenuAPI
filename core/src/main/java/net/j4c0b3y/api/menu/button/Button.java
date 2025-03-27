package net.j4c0b3y.api.menu.button;

import org.bukkit.inventory.ItemStack;

/**
 * A menu button, used to display an icon
 * and a click handler for the inventory item.
 *
 * @author J4C0B3Y
 * @version MenuAPI
 * @since 12/05/2024
 */
public abstract class Button {
    /**
     * Gets the button's display item stack, this can
     * be null if the button should not be displayed.
     *
     * @return The buttons icon.
     */
    public abstract ItemStack getIcon();

    /**
     * Called when the button is clicked,
     * this can be ignored by the onClick
     * handler in the button's menu.
     *
     * @param click The button click.
     */
    public void onClick(ButtonClick click) { }
}
