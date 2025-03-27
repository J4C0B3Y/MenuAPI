# MenuAPI

Simple to use, high performance bukkit menu api.

## Features

- Inbuilt paginated menus
- Asynchronous menu option
- Foreground & background layers
- Reusable layer templates
- Auto update annotation
- Small and lightweight (~22kb)
- Inbuilt item builder utility
- Supports spigot 1.8+

## Support

If you need any assistance using or installing my MenuAPI,
feel free to contact me by either adding me on discord (@J4C0B3Y)
or by creating an issue and explaining your problem or question.

## Installation

Prebuilt jars can be found in [releases](https://github.com/J4C0B3Y/MenuAPI/releases).

> **NOTE:** <br/>
> It is recommended to relocate the library to prevent
> version mismatches with other plugins that use the api.

### Maven & Gradle

Replace `VERSION` with the latest release version on GitHub.

```kts
repositories {
    maven("https://repo.j4c0b3y.net/public/")
}

dependencies {
    implementation("net.j4c0b3y:MenuAPI-core:VERSION")
}
```

```xml
<repositories>
    <repository>
        <id>j4c0b3y-public</id>
        <url>https://repo.j4c0b3y.net/public/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>net.j4c0b3y</groupId>
        <artifactId>MenuAPI-core</artifactId>
        <version>VERSION</version>
    </dependency>
</dependencies>
```

### Building

1. Clone this repository and enter its directory.
2. Run the intellij build configuration by clicking the top right icon.
3. Alternatively you can run `gradle shadowJar`.

## Usage

A MenuHandler instance is required before opening any menus.

```java
public class ExamplePlugin extends JavaPlugin {
    private MenuHandler menuHandler;
    
    @Override
    public void onEnable() {
        this.menuHandler = new MenuHandler(this);
    }
}
```

Optionally, you can change default menu behaviour.

```java
// Reset the cursor to the center when switching between menus.
menuHandler.setResetCursor(true);

// Close the menu when Menu#back() is called and there is no previous menu.
menuHandler.setCloseOnBack(true);
```

### Menus

To make a menu, all you need to do is extend the `Menu` class.

```java
public class ExampleMenu extends Menu {
    
    public ExampleMenu(Player player) {
        // You must specify the title, size and 
        // player when calling the super constructor.
        super("Example Menu", MenuSize.THREE, player);
    }
    
    @Override
    public void setup(BackgroundLayer background, ForegroundLayer foreground) {
        // Here you can add buttons to the foreground and background layers.
        // This method is called the first time the menu is opened.
        // See layers & templates further down for more information.
    }
}
```

There are some optional methods you can use to listen to certain menu actions.

```java
@Override
public void onOpen() {
    // Called when the menu is opened.
}

@Override
public void onClose() {
    // Called when the menu is closed.
}
```

To make a menu asynchronous you can annotate the class with `@Async`.

> **WARNING:** <br/>
> Using bukkit api methods in async menus is not recommended, this is
> because the buttons and menu methods are not called on the server thread.

```java
@Async // Annotate the class with @Async
public class ExampleMenu extends Menu {
    // ...
}
```

To use the `Menu#back()` method, you must set the previous menu in the constructor.

If the previous menu is not set and the back method is called, by default
nothing will happen unless `menuHandler.setCloseOnBack(true)` is set.

```java
public ExampleMenu(Menu previous) {
    super("Example Menu", MenuSize.THREE, previous.getPlayer());
    setPreviousMenu(previous);
}
```

Here is how you open the menu for the player.

```java
new ExampleMenu(player).open()
```

To close the menu you can do one of two things, both achieve the same thing.

```java
menu.close();
// or
player.closeInventory();
```

There are two ways to update menus, the first way is to call `Menu#update()`.

Alternatively, you can make a menu auto update by annotating it with `@AutoUpdate`.

```java
// You can specify the update interval in ticks.
// Remember 20 ticks is equal to 1 second.
@AutoUpdate(20)
public class ExampleMenu { }
```

### Buttons

You must create an item / icon for the button to display in the menu.

```java
public class ExampleButton extends Button {
 
    @Override
    public ItemStack getIcon() {
        // If you have an ItemBuilder util, you should use that.
        ItemStack icon = new ItemStack(Material.WOOL);
        ItemMeta meta = icon.getItemMeta();
        
        meta.setDisplayName("Example Button");

        icon.setItemMeta(meta);
        return icon;
    }
    
    @Override
    public void onClick(ButtonClick click) {
        // This method is called when your button is clicked.
        if (!click.getType().equals(ClickType.LEFT)) return;
        
        // Do something here.
    }
}
```

### Layers

A menu has two layers to place buttons on, the background and the foreground.

- The background layer should obviously be used for the menu background, border design, etc.
- The foreground layer should be used for functional buttons, a back button, etc.

You can use a variety of methods for adding buttons to a layer,
there are javadocs on each method but here is an example.

```java
public class ExampleMenu extends Menu {
    // ...
    
    @Override
    public void setup(BackgroundLayer background, ForegroundLayer foreground) {
        background.border(new PlaceholderButton());
        
        foreground.set(1, 1, new TeamButton(Team.RED));
        foreground.set(3, 1, new TeamButton(Team.BLUE));
        foreground.set(5, 1, new TeamButton(Team.GREEN));
        foreground.set(7, 1, new TeamButton(Team.YELLOW));
    }
}
```

If a button returns a null icon, it will not be placed in the menu.

This makes layers powerful as the background button will be used
if a foreground button in the same slot returns null.

An example on why this is useful is making a back button that returns the item only if
there is a previous menu otherwise it will be ignored and use the button on the background. 

### Templates

Templates can be utilized to make reusable menu designs easily, here is an example.

```java
@RequiredArgsConstructor
public class BorderTemplate implements Template {
    private final Menu menu;
    
    @Override
    public void apply(BackgroundLayer background, ForegroundLayer foreground) {
        background.border(new PlaceholderButton());
        foreground.set(0, new BackButton(menu));
    }
}
```

You can then apply the template in the menu's setup method.

```java
public class ExampleMenu extends Menu {
    // ...

    @Override
    public void setup(BackgroundLayer background, ForegroundLayer foreground) {
        apply(new BorderTemplate(this));
        
        // You can then add menu specific buttons.
    }
}
```

### Paginated Menus

To make a paginated menu, extend the `PaginatedMenu` class.

```java
public class TagsMenu extends PaginatedMenu {
    // ...
    
    // It is recommended to put all this in a PaginationTemplate
    // so this doesn't have to be copied each time you make a menu.
    @Override
    public void setup(BackgroundLayer background, ForegroundLayer foreground) {
        background.border(new PlaceholderButton());
        foreground.set(0, new BackButton(this));
        
        // Add the inbuilt pagination slot button where 
        // you want your paginated buttons / entries to go.
        // NOTE: You cannot add pagination slots to the background layer.
        foreground.center(new PaginationSlot());

        // You can also add your own previous page and next page buttons.
        foreground.set(0, menu.getRows() - 1, new PreviousPageButton(menu));
        foreground.set(Menu.COLUMNS - 1, menu.getRows() - 1, new NextPageButton(menu));
    }
    
    // Here you create a list of the buttons that you want
    // to be paginated and put in the pagination slots added above.
    @Override
    public List<Button> getEntries() {
        List<Button> buttons = new ArrayList();
        
        for (Tag tag : plugin.getTagHandler().getTags()) {
            buttons.add(new TagButton(tag));
        }
        
        return buttons;
    }
}
```

Here are some pagination menu specific methods you can use.

```java
// Changes the page and updates the menu.
menu.page(int page);
menu.next();
menu.previous();

// Get information about the paginated menu.
menu.getPage();
menu.getTotalPages();
menu.hasNextPage();
menu.hasPreviousPage();

// More can be found in the pagination menu class.
```

### Item Builder Utility

The menu api contains a utility class for creating item stacks using a builder. 

Since the builder heavily utilizes XSeries, the library will contain many extra classes resulting in increased size,
the utility is contained in a separate package called `MenuAPI-extras`, which should be added in your build configuration.

Here is an example of its usage, refer to it's javadocs for more information:

```java
ItemStack item = new Item(XMaterial.PLAYER_HEAD) // Uses XSeries throughout
    .setName("&c&lTest") // Set the item's display name.
    .addLore("&7Test lore") // You can also use setLore.
    .setAmount(10) // Set the quantity of items in the item stack.
    .setUnbreakable() // You can optionally put a boolean here.
    .addEnchantment(XEnchantment.AQUA_AFFINITY, 3) // You can use unsafe levels.
    .addFlag(XItemFlag.HIDE_ENCHANTS) // Add item flags to the item.
    .setTexture("J4C0B3Y") // Can be uuid, username, base64, etc.
    .setData((short) 3) // Set the data / durability of the item.
    .build(); // Finalize the item, converting it into the item stack.
```

You can change how the name and lore is translated by setting the translator.

```java
// Specific to item instance:
ItemStack item = new Item()
    .setName("Some text.")
    .setFormatter(Color::translate)
    .build();

// Set for any future Item instances created.
Item.setDefaultFormatter(Color::translate);
```

### Want more?

Each and every class in my menu api has detailed javadocs explaining what
methods and variables are used for, and functionality of internal methods.

> Made with ❤ // J4C0B3Y 2024
