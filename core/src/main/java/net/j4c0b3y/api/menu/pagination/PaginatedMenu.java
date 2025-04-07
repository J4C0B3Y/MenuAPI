package net.j4c0b3y.api.menu.pagination;

import lombok.Getter;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A menu with pagination functionality.
 *
 * @author J4C0B3Y
 * @version MenuAPI
 * @since 12/05/2024
 */
@Getter
public abstract class PaginatedMenu extends Menu {
    /**
     * The minimum / first page possible in a paginated menu.
     */
    public final static int MINIMUM_PAGE = 1;

    /**
     * A list of the buttons that should be paginated.
     */
    private final List<Button> paginationEntries = new ArrayList<>();

    /**
     * A map of a slot to an index in the pagination entries.
     */
    private final Map<Integer, Integer> indexCache = new HashMap<>();

    /**
     * The current page of the pagination menu.
     */
    private int page = MINIMUM_PAGE;

    /**
     * The total pages in the pagination menu.
     */
    private int totalPages = MINIMUM_PAGE;

    /**
     * Creates a new paginated menu, initializing the underlying bukkit inventory.
     *
     * @param title The title, auto translated.
     * @param size The menu size, in rows.
     * @param player The player to open the menu with.
     */
    public PaginatedMenu(String title, MenuSize size, Player player) {
        super(title, size, player);
    }

    /**
     * A list of the buttons that should be paginated.
     *
     * @return The buttons to paginate.
     */
    public abstract List<Button> getEntries();

    /**
     * Updates the pagination entries, recreates the index cache,
     * calculates the total and current page then updates the menu.
     */
    @Override
    protected void refresh() {
        // Update the pagination entries.
        paginationEntries.clear();
        paginationEntries.addAll(getEntries());

        // Clear the index cache.
        indexCache.clear();
        int index = 0;

        // For each pagination slot in the menu's foreground layer,
        // map the slot of the button in the layer to the index
        // relative to other pagination slots.
        for (Map.Entry<Integer, Button> entry : getForeground().getButtons().entrySet()) {
            if (entry.getValue() instanceof PaginationSlot) {
                indexCache.put(entry.getKey(), index++);
            }
        }

        // Calculate the total pages by dividing the amount of entries
        // by the amount of slots then rounding to the next highest whole number.
        // If the index cache or the pagination entries is empty, use the MINIMUM_PAGE.
        totalPages = indexCache.isEmpty() ? MINIMUM_PAGE : Math.max(
            (int) Math.ceil((double) paginationEntries.size() / getPaginationSlots()), MINIMUM_PAGE
        );

        // Clamps the page number from MINIMUM_PAGE to the total page count.
        page = Math.min(Math.max(page, MINIMUM_PAGE), totalPages);

        // Refreshes the menu to reflect the changes.
        super.refresh();
    }

    /**
     * Increments the page then updates the menu.
     */
    public void nextPage() {
        page++;
        update();
    }

    /**
     * Decrements the page then updates the menu.
     */
    public void previousPage() {
        page--;
        update();
    }

    /**
     * Sets the page then updates the menu.
     *
     * @param number The page number.
     */
    public void setPage(int number) {
        page = number;
        update();
    }

    /**
     * If there is a next page in the menu,
     * which is true if page < total pages.
     *
     * @return If there is a next page.
     */
    public boolean hasNextPage() {
        return page < totalPages;
    }

    /**
     * If there is a previous page in the
     * menu, which is true if page > 1.
     *
     * @return If there is previous page.
     */
    public boolean hasPreviousPage() {
        return page > 1;
    }

    /**
     * The total amount of slots for pagination in the menu,
     * this corresponds directly to the size of the index cache.
     *
     * @return The pagination slot count.
     */
    public int getPaginationSlots() {
        return indexCache.size();
    }

    /**
     * The index of the current page which
     * is the current page number - 1.
     *
     * @return The page index.
     */
    public int getPageIndex() {
        return page - 1;
    }
}
