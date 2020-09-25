package braayy.pets.inventory;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class SimpleInventory implements InventoryHolder, Listener {

    private final Map<String, Object> propertyMap;

    @Getter private SimpleInventory parent;

    private final Inventory inventory;

    @Getter private final boolean cancel;
    private boolean dirty;

    final Map<Integer, ClickHandler> clickHandlerMap;

    public SimpleInventory(String title, int size, boolean cancel) {
        this(null, title, size, cancel);
    }

    public SimpleInventory(SimpleInventory parent, String title, int size, boolean cancel) {
        this.clickHandlerMap = new HashMap<>();
        this.propertyMap = new HashMap<>();

        this.parent = parent;

        this.inventory = Bukkit.createInventory(this, size, title);

        this.cancel = cancel;
        this.dirty = true;
    }

    public abstract void setup(Player player);

    public abstract void draw(Player player);

    public void setItem(int slot, ItemStack stack) {
        if (slot >= 0 && slot < this.inventory.getSize()) {
            this.inventory.setItem(slot, stack);
        }
    }

    public void handleClick(int slot, ClickHandler handler) {
        if (slot >= 0 && slot < this.inventory.getSize()) {
            this.clickHandlerMap.put(slot, handler);
        }
    }

    public void handleClick(int slot, EmptyClickHandler handler) {
        this.handleClick(slot, (ClickHandler) handler);
    }

    public void setDirty() {
        this.dirty = true;
    }

    public void setProperty(String key, Object value) {
        this.propertyMap.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(String key) {
        return (T) this.propertyMap.get(key);
    }

    public void removeProperty(String key) {
        this.propertyMap.remove(key);
    }

    public void redraw(Player player) {
        if (player != null) {
            this.inventory.clear();
            this.draw(player);
        }
    }

    public void open(Player player) {
        if (player != null) {
            if (this.dirty) {
                this.dirty = false;

                this.inventory.clear();
                this.draw(player);
            }

            player.openInventory(this.inventory);
        }
    }

    public void back(Player player) {
        if (player != null && this.parent != null) {
            this.parent.open(player);
        }
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

}