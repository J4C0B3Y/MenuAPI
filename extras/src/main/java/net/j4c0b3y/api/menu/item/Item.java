package net.j4c0b3y.api.menu.item;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XItemFlag;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.Profileable;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A builder utility to create items easily.
 *
 * @author J4C0B3Y
 * @version MenuAPI
 * @since 1/02/2025
 */
@Setter
@Accessors(chain = true)
public class Item implements Cloneable {
    /**
     * A reference to the modern setUnbreakable method, if applicable.
     */
    private static Method MODERN_SET_UNBREAKABLE_METHOD = getModernSetUnbreakableMethod();

    /**
     * The default formatter to use when an item class instance is created.
     */
    @Setter private static Function<String, String> defaultFormatter =
        content -> ChatColor.translateAlternateColorCodes('&', content);

    /**
     * The underlying item stack associated with the item instance.
     */
    private ItemStack item;

    /**
     * The underlying item meta associated with the item instance.
     */
    private ItemMeta meta;

    /**
     * The name of the item.
     */
    private String name;

    /**
     * The item's lore.
     */
    private List<String> lore = new ArrayList<>();

    /**
     * The item's formatter.
     */
    private Function<String, String> formatter = defaultFormatter;

    /**
     * Creates an item builder using an item stack.
     *
     * @param item The item stack.
     */
    public Item(ItemStack item) {
        this.item = item != null ? item.clone() : null;
        this.meta = item != null ? this.item.getItemMeta() : null;
    }

    /**
     * Creates an item builder using an XMaterial.
     *
     * @param material The XMaterial.
     */
    public Item(XMaterial material) {
        this(material != null ? material.parseItem() : null);
    }

    /**
     * A chain method to perform modifications on the item stack.
     * Ensures the item stack is not null, then accepts the consumer.
     *
     * @param consumer The consumer.
     * @return The item instance.
     */
    public Item modifyItem(Consumer<ItemStack> consumer) {
        if (this.item != null) {
            consumer.accept(this.item);
        }

        return this;
    }

    /**
     * A chain method to perform modifications on the item meta.
     * Ensures the item meta is not null, then accepts the consumer.
     *
     * @param consumer The consumer.
     * @return The item instance.
     */
    public Item modifyMeta(Consumer<ItemMeta> consumer) {
        return modifyItem(item -> {
            if (this.meta != null) {
                consumer.accept(this.meta);
            }
        });
    }

    /**
     * A chain method to perform modifications on the specified item meta.
     * Ensures the item meta is not null and is the correct type, then accepts the consumer.
     *
     * @param type The item meta type.
     * @param consumer The consumer.
     * @return The item instance.
     */
    @SuppressWarnings("unchecked")
    public <T extends ItemMeta> Item modifyMeta(Class<T> type, Consumer<T> consumer) {
        return modifyMeta(meta -> {
            if (type.isAssignableFrom(meta.getClass())) {
                consumer.accept((T) meta);
            }
        });
    }

    /**
     * Sets the amount of items in the item stack.
     *
     * @param amount The amount.
     * @return The item instance.
     */
    public Item setAmount(int amount) {
        return modifyItem(item -> item.setAmount(amount));
    }

    /**
     * Sets the data / durability of the item.
     *
     * @param data The data / durability.
     * @return The item instance.
     */
    public Item setData(short data) {
        return modifyItem(item -> item.setDurability(data));
    }

    /**
     * Sets the item's lore using the provided string list.
     *
     * @param lore The lore string list.
     * @return The item instance.
     */
    public Item setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    /**
     * Sets the item's lore using the provided lines.
     *
     * @param lore The lore lines.
     * @return The item instance.
     */
    public Item setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    /**
     * Adds the provided lines to the item's lore.
     *
     * @param lines The lore lines to add.
     * @return The item instance.
     */
    public Item addLore(String... lines) {
        this.lore.addAll(Arrays.asList(lines));
        return this;
    }

    /**
     * Unsafely adds an enchantment to the item using XEnchantment.
     *
     * @param enchantment The XEnchantment.
     * @param level The enchantment level.
     * @return The item instance.
     */
    public Item addEnchantment(XEnchantment enchantment, int level) {
        return modifyItem(item -> item.addUnsafeEnchantment(enchantment.get(), level));
    }

    /**
     * Adds an item flag to the item using XItemFlag.
     *
     * @param flag The XItemFlag.
     * @return The item instance.
     */
    public Item addFlag(XItemFlag flag) {
        return modifyMeta(meta -> meta.addItemFlags(flag.get()));
    }

    /**
     * Sets the item as unbreakable, preventing it from losing durability.
     *
     * @param unbreakable If the item is unbreakable.
     * @return The item instance.
     */
    public Item setUnbreakable(boolean unbreakable) {
        return modifyMeta(meta -> {
            if (MODERN_SET_UNBREAKABLE_METHOD == null) {
                meta.spigot().setUnbreakable(unbreakable);
                return;
            }

            try {
                MODERN_SET_UNBREAKABLE_METHOD.invoke(meta, unbreakable);
            } catch (ReflectiveOperationException exception) {
                throw new RuntimeException("Failed to set item as unbreakable!", exception);
            }
        });
    }

    /**
     * Sets the item as unbreakable, preventing it from losing durability.
     *
     * @return The item instance.
     */
    public Item setUnbreakable() {
        return setUnbreakable(true);
    }

    /**
     * Sets the skull texture of the item using XSkull.
     * The texture can be a name, uuid, base64, etc.
     *
     * @param texture The texture string.
     * @return The item instance.
     */
    public Item setTexture(String texture) {
        return modifyMeta(meta -> {
            if (!(meta instanceof SkullMeta)) return;

            this.meta = XSkull.of(meta)
                .profile(Profileable.detect(texture))
                .apply();
        });
    }

    /**
     * Sets the skull texture of the item using XSkull.
     *
     * @param uniqueId The player's uuid.
     * @return The item instance.
     */
    public Item setTexture(UUID uniqueId) {
        return setTexture(uniqueId.toString());
    }

    /**
     * Sets the skull texture of the item using XSkull.
     *
     * @param player The player.
     * @return The item instance.
     */
    public Item setTexture(Player player) {
        return setTexture(player.getUniqueId());
    }

    /**
     * Formats the content using the item formatter.
     *
     * @param content The content to format.
     * @return The formatted content.
     */
    private String format(String content) {
        return this.formatter.apply(content);
    }

    /**
     * Formats the content lines using the item formatter.
     *
     * @param lines The content lines to format.
     * @return The formatted content lines.
     */
    private List<String> format(List<String> lines) {
        List<String> formatted = new ArrayList<>(lines);
        formatted.replaceAll(this::format);
        return formatted;
    }

    /**
     * Finalizes the builder, converting all saved data into a final item stack.
     *
     * @return The final item stack.
     */
    public ItemStack build() {
        modifyMeta(meta -> {
            if (this.name == null) return;
            meta.setDisplayName(format(this.name));
        });

        modifyMeta(meta -> {
            if (this.lore == null) return;
            meta.setLore(format(this.lore));
        });

        modifyItem(item -> item.setItemMeta(this.meta));

        return this.item;
    }

    /**
     * Creates a deep clone of the item builder.
     *
     * @return The cloned item builder.
     */
    @Override
    public Item clone() {
        try {
            // Create a structured clone, includes name and formatter.
            Item clone = (Item) super.clone();

            if (this.item != null) {
                // Clone the ItemStack.
                clone.item = this.item.clone();

                if (this.meta != null) {
                    // Clone the ItemMeta.
                    clone.meta = clone.item.getItemMeta().clone();
                }
            }

            if (this.lore != null) {
                // Deep copy the lore.
                clone.lore = new ArrayList<>(this.lore);
            }

            return clone;
        } catch (CloneNotSupportedException exception) {
            throw new Error(exception);
        }
    }

    /**
     * Tries to get the modern setUnbreakable method from the ItemMeta class.
     *
     * @return The modern unbreakable method.
     */
    @SuppressWarnings("JavaReflectionMemberAccess")
    private static Method getModernSetUnbreakableMethod() {
        try {
            return ItemMeta.class.getMethod("setUnbreakable", boolean.class);
        } catch (ReflectiveOperationException exception) {
            return null;
        }
    }
}
