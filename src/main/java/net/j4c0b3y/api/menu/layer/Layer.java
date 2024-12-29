package net.j4c0b3y.api.menu.layer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.pagination.PaginationSlot;
import net.j4c0b3y.api.menu.utils.Position;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Represents a layer of buttons in a menu with
 * many methods to manipulate the layer's content.
 *
 * @author J4C0B3Y
 * @version MenuAPI
 * @since 12/05/2024
 */
@Getter
@RequiredArgsConstructor
public abstract class Layer {
    /**
     * The menu the layer belongs to.
     */
    private final Menu menu;

    /**
     * The buttons in the layer, this is a tree map in the
     * foreground so the buttons are sorted for pagination.
     */
    private final Map<Integer, Button> buttons;

    /**
     * The name of the layer which
     * is used in warning messages.
     *
     * @return The name of the layer.
     */
    public abstract String getName();

    /**
     * Sets a button using an index,
     * validates if the placement is legal.
     *
     * @param index The index.
     * @param button The button to set.
     */
    public void set(int index, Button button) {
        if (isInvalid(button, index)) {
            return;
        }

        buttons.put(index, button);
    }

    /**
     * Sets a button using x and y coordinates.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param button The button to set.
     */
    public void set(int x, int y, Button button) {
        set(menu.getIndex(x, y), button);
    }

    /**
     * Sets a button using a position.
     *
     * @param position The position.
     * @param button The button to set.
     */
    public void set(Position position, Button button) {
        set(position.getX(), position.getY(), button);
    }

    /**
     * Adds a button to the next available slot,
     * checking the other layer if present.
     *
     * @param button The button to add.
     * @param other The other layer to check.
     */
    protected void add(Button button, Layer other) {
        for (int i = 0; i < menu.getTotalSlots(); i++) {
            if (buttons.get(i) != null) {
                continue;
            }

            if (other != null && other.getButtons().get(i) != null) {
                continue;
            }

            set(i, button);
            return;
        }
    }

    /**
     * Adds a button to the next available slot.
     *
     * @param button The button to add.
     * @param other If the other layer should be checked.
     */
    public void add(Button button, boolean other) {
        add(button, null);
    }

    /**
     * Adds a button to the next available slot,
     * checking slots in the other layer as well.
     *
     * @param button The button to add.
     */
    public void add(Button button) {
        add(button, true);
    }

    /**
     * Fills the whole layer with a button.
     *
     * @param button The button to fill.
     */
    public void fill(Button button) {
        for (int i = 0; i < menu.getTotalSlots(); i++) {
            set(i, button);
        }
    }

    /**
     * Fills a button between two x and y coordinates.
     *
     * @param ax The first x coordinate.
     * @param ay The first y coordinate.
     * @param bx The second x coordinate.
     * @param by The second y coordinate.
     * @param button The button to fill.
     */
    public void fill(int ax, int ay, int bx, int by, Button button) {
        int maxX = Math.max(ax, bx);
        int maxY = Math.max(ay, by);

        for (int x = Math.min(ax, bx); x <= maxX; x++) {
            for (int y = Math.min(ay, by); y <= maxY; y++) {
                set(x, y, button);
            }
        }
    }

    /**
     * Fills a button between two positions.
     *
     * @param a The first position.
     * @param b The second position.
     * @param button The button to fill.
     */
    public void fill(Position a, Position b, Button button) {
        fill(a.getX(), a.getY(), b.getX(), b.getY(), button);
    }

    /**
     * Fills a button between two slot indexes.
     *
     * @param a The first slot index.
     * @param b The second slot index.
     * @param button The button to fill.
     */
    public void fill(int a, int b, Button button) {
        fill(menu.getPosition(a), menu.getPosition(b), button);
    }

    /**
     * Fills a row with a button.
     *
     * @param row The row index.
     * @param button The button to fill.
     */
    public void row(int row, Button button) {
        for (int i = 0; i < Menu.COLUMNS; i++) {
            set(i, row, button);
        }
    }

    /**
     * Fills a column with a button.
     *
     * @param column The column index.
     * @param button The button to fill.
     */
    public void column(int column, Button button) {
        for (int i = 0; i < menu.getRows(); i++) {
            set(column, i, button);
        }
    }

    /**
     * Fills the border with a button.
     *
     * @param button The button to fill.
     */
    public void border(Button button) {
        row(0, button);

        if (menu.getRows() < 2) {
            return;
        }

        row(menu.getRows() - 1, button);

        if (menu.getRows() < 3) {
            return;
        }

        column(0, button);
        column(Menu.COLUMNS - 1, button);
    }

    /**
     * Fills the center using an inset with a button.
     *
     * @param button The button to fill.
     * @param inset The inset.
     */
    public void center(Button button, int inset) {
        if (menu.getRows() < inset * 2 + 1) {
            return;
        }

        if (inset < 1) {
            fill(button);
            return;
        }

        for (int x = inset; x < Menu.COLUMNS - inset; x++) {
            for (int y = inset; y < menu.getRows() - inset; y++) {
                set(x, y, button);
            }
        }
    }

    /**
     * Fills the center with a button.
     *
     * @param button The button to fill.
     */
    public void center(Button button) {
        center(button, 1);
    }

    /**
     * Removes a button using an index.
     *
     * @param index The index to remove.
     */
    public void remove(int index) {
        buttons.remove(index);
    }

    /**
     * Removes a button using x and y coordinates.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void remove(int x, int y) {
        remove(menu.getIndex(x, y));
    }

    /**
     * Removes a button using a position
     *
     * @param position The position.
     */
    public void remove(Position position) {
        remove(position.getX(), position.getY());
    }

    /**
     * Clears all buttons from the layer.
     */
    public void clear() {
        buttons.clear();
    }

    /**
     * Finds all buttons of a certain type.
     *
     * @param type The button class.
     * @return The matching buttons.
     */
    @SuppressWarnings("unchecked")
    public <T extends Button> List<T> getButtons(Class<T> type) {
        List<T> buttons = new ArrayList<>();

        for (Button button : this.buttons.values()) {
            if (type.isInstance(button)) {
                buttons.add((T) button);
            }
        }

        return buttons;
    }

    /**
     * Checks if a button to be set is invalid,
     * if so sends a warning message.
     *
     * @param button The button to check
     * @param index The index to check
     * @return If the button or index is invalid.
     */
    protected boolean isInvalid(Button button, int index) {
        if (button == null) {
            warning("null", null, index);
            return true;
        }

        if (index < 0 || index >= menu.getTotalSlots()) {
            warning("out of bounds", button, index);
            return true;
        }

        return false;
    }

    /**
     * Sends a warning for an incorrect button or
     * button placement with detailed information.
     *
     * @param reason The warning reason.
     * @param button The warning button.
     * @param slot The warning slot.
     */
    protected void warning(String reason, Button button, int slot) {
        menu.getHandler().warning(
            "Menu '" + menu.getClass().getSimpleName() +
            "' tried to add " + reason + " button '" +
            (button != null ? button.getClass().getSimpleName() : "unknown") +
            "' to layer '" + getName() + "' at index '" + slot +
            "' out of '" + menu.getTotalSlots() + "'."
        );
    }

    /**
     * Gets the buttons in the layer that don't already have a
     * position in the icons array and have a non-null icon.
     *
     * @param icons The existing icons.
     * @return The resulting buttons.
     */
    public Map<Integer, Button> getButtons(ItemStack[] icons) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.buttons.forEach((slot, button) -> {
            // Return if the icons array already has an icon in the slot.
            if (icons != null && icons[slot] != null) {
                return;
            }

            // If the button is a pagination slot, set the icon
            // index so the slot can calculate which entry to show.
            if (button instanceof PaginationSlot) {
                ((PaginationSlot) button).setIconSlot(slot);
            }

            ItemStack icon = button.getIcon();

            // If the button has an icon, add it to the icon
            // array and to the slot-button map being returned.
            if (icon != null) {
                if (icons != null) icons[slot] = icon;
                buttons.put(slot, button);
            }
        });

        return buttons;
    }
}
