package net.j4c0b3y.api.menu.utils;

/**
 * @author J4C0B3Y
 * @version MenuAPI
 * @since 7/08/2025
 */
@FunctionalInterface
public interface TriFunction<F, S, T, R> {
    R apply(F first, S second, T third);
}
