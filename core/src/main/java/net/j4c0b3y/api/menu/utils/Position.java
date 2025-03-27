package net.j4c0b3y.api.menu.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a 2d position with an x and y coordinate.
 *
 * @author J4C0B3Y
 * @version MenuAPI
 * @since 12/05/2024
 */
@Getter
@RequiredArgsConstructor
public class Position {
    /**
     * The x coordinate.
     */
    private final int x;

    /**
     * The y coordinate.
     */
    private final int y;
}
