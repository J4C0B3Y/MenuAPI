package net.j4c0b3y.api.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The possible sizes for a bukkit inventory.
 *
 * @author J4C0B3Y
 * @version MenuAPI
 * @since 12/05/2024
 */
@Getter
@RequiredArgsConstructor
public enum MenuSize {
    /**
     * A menu with three rows.
     */
    THREE(3),

    /**
     * A menu with four rows.
     */
    FOUR(4),

    /**
     * A menu with five rows.
     */
    FIVE(5),

    /**
     * A menu with six rows.
     */
    SIX(6);

    /**
     * How many rows the menu has.
     */
    private final int rows;

    /**
     * Returns the menu size using the input number,
     * which can be either the amount of rows or slots.
     *
     * @param input The rows or slots.
     * @return The associated menu size.
     */
    public static MenuSize of(int input) {
        for (MenuSize size : MenuSize.values()) {
            int rows = size.getRows();

            if (rows == input || rows == input * Menu.COLUMNS) {
                return size;
            }
        }

        throw new IllegalArgumentException("There is no menu size with " + input + " rows or slots!");
    }
}
