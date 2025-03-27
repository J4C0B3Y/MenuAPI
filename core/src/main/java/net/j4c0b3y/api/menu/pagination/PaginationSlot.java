package net.j4c0b3y.api.menu.pagination;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import org.bukkit.inventory.ItemStack;

/**
 * Acts as a placeholder for a pagination entry
 * which uses the entry icon and click handler.
 *
 * @author J4C0B3Y
 * @version MenuAPI
 * @since 12/05/2024
 */
@RequiredArgsConstructor
public class PaginationSlot extends Button {
    /**
     * The pagination menu the slot belongs to.
     */
    private final PaginatedMenu menu;

    /**
     * The slot index for displaying the icon.
     */
    @Setter private int iconSlot;

    /**
     * Uses the pagination entry's icon.
     *
     * @return The entry's icon.
     */
    @Override
    public ItemStack getIcon() {
        Button button = getButton(iconSlot);
        return button != null ? button.getIcon() : null;
    }

    /**
     * Uses the pagination entry's click handler.
     *
     * @param click The button click.
     */
    @Override
    public void onClick(ButtonClick click) {
        Button button = getButton(click.getSlot());
        if (button != null) button.onClick(click);
    }

    /**
     * Gets the pagination entry that belongs to a slot.
     *
     * @param slot The slot.
     * @return The pagination entry.
     */
    private Button getButton(int slot) {
        // Uses the index cache to map the slot to the index in the entries array,
        // pageIndex * paginationSlots is the offset to shift by depending on the page.
        int index = menu.getPageIndex() * menu.getPaginationSlots() + menu.getIndexCache().get(slot);

        // If the index lies outside the list of entries there is
        // no entry for that slot and no icon should be displayed.
        if (index >= menu.getPaginationEntries().size()) {
            return null;
        }

        return menu.getPaginationEntries().get(index);
    }
}
