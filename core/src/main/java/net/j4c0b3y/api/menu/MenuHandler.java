package net.j4c0b3y.api.menu;


import lombok.Getter;
import lombok.Setter;
import net.j4c0b3y.api.menu.listener.InventoryListener;
import net.j4c0b3y.api.menu.task.AutoUpdateTask;
import net.j4c0b3y.api.menu.utils.TriFunction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Stores open menus, menu options, starts tasks
 * and registers listeners for menus to function.
 *
 * @author J4C0B3Y
 * @version MenuAPI
 * @since 12/05/2024
 */
@Getter
public class MenuHandler {
    /**
     * The menu handler instance.
     */
    @Getter private static MenuHandler instance;

    /**
     * The plugin to register listeners and start tasks with.
     */
    private final JavaPlugin plugin;

    /**
     * Stores menus that players have open.
     */
    private final Map<Player, Menu> openMenus = new ConcurrentHashMap<>();

    /**
     * Handles auto updating menus.
     */
    private final AutoUpdateTask autoUpdateTask;

    /**
     * If the cursor position should
     * reset when switching between menus.
     */
    @Setter private boolean resetCursor = false;

    /**
     * If the menu should close when the back
     * method is called and there is no previous menu.
     */
    @Setter private boolean closeOnBack = false;

    /**
     * The method to use when creating internal menu inventories.
     */
    @Setter private TriFunction<Player, Integer, String, Inventory> inventoryCreator =
        Bukkit::createInventory;

    /**
     * Creates a new menu handler, starting the autoupdating
     * task and registering inventory and connection listeners.
     *
     * @param plugin The plugin to register listeners and start tasks with.
     */
    public MenuHandler(JavaPlugin plugin) {
        instance = this;

        this.plugin = plugin;
        this.autoUpdateTask = new AutoUpdateTask(this);

        Bukkit.getScheduler().runTaskTimer(plugin, autoUpdateTask, 0, 1);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(this), plugin);
    }

    /**
     * Executes a callback if the
     * specified player has an open menu.
     *
     * @param player The player to check.
     * @param callback The callback to execute.
     */
    public void ifOpen(Player player, Consumer<Menu> callback) {
        Menu menu = openMenus.get(player);
        if (menu != null) callback.accept(menu);
    }

    /**
     * Displays a warning message that comes
     * from the plugin associated with the handler.
     *
     * @param message The warning message.
     */
    public void warning(String message) {
        plugin.getLogger().warning("[MenuAPI] " + message);
    }

    /**
     * Runs a task on the server thread.
     *
     * @param task The task to run.
     */
    protected void schedule(Runnable task) {
        if (Bukkit.isPrimaryThread()) {
            task.run();
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    /**
     * Runs a task using the bukkit scheduler.
     *
     * @param task The task to run.
     * @param async If the task should be run asynchronously.
     */
    protected void schedule(Runnable task, boolean async) {
        if (async) {
            CompletableFuture.runAsync(task);
        } else {
            schedule(task);
        }
    }

    /**
     * Runs a task asynchronously.
     *
     * @param task The task to run.
     * @param async If the task should be run asynchronously.
     */
    protected void run(Runnable task, boolean async) {
        if (async) {
            CompletableFuture.runAsync(task);
        } else {
            task.run();
        }
    }
}
