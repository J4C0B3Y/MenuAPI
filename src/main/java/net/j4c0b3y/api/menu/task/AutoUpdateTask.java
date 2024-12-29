package net.j4c0b3y.api.menu.task;

import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuHandler;
import net.j4c0b3y.api.menu.annotation.AutoUpdate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Used to automatically update menus annotated with @AutoUpdate
 *
 * @author J4C0B3Y
 * @version MenuAPI
 * @since 12/05/2024
 */
@RequiredArgsConstructor
public class AutoUpdateTask implements Runnable {
    /**
     * The menu handler.
     */
    private final MenuHandler handler;

    /**
     * The tick time since the task started.
     */
    @Getter private long ticks = 0;

    /**
     * Loops through all open menus that is annotated with
     * AutoUpdate and updates them if the ticks since the
     * last update matches the value of the annotation.
     */
    @Override
    public void run() {
        for (Menu menu : handler.getOpenMenus().values()) {
            AutoUpdate annotation = menu.getClass().getAnnotation(AutoUpdate.class);

            if (annotation != null && ticks - menu.getLastUpdate() >= annotation.value()) {
                menu.update();
            }
        }

        ticks++;
    }
}
