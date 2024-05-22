package gg.voided.api.menu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the menu as asynchronous,
 * this is overridden by Menu#setAsync.
 *
 * @author J4C0B3Y
 * @version MenuAPI
 * @since 22/05/2024
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Async {
}
