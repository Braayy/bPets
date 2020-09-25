package braayy.pets.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

public class SimpleInventoryListener implements Listener {

    private SimpleInventoryListener() {}

    public static void register(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new SimpleInventoryListener(), plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null && event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof SimpleInventory) {
            SimpleInventory inventory = (SimpleInventory) event.getClickedInventory().getHolder();

            if (inventory.isCancel()) {
                event.setCancelled(true);
            }

            ClickHandler handler = inventory.clickHandlerMap.get(event.getSlot());

            if (handler != null) {
                handler.handle(event.getCurrentItem());
            }
        }
    }

}