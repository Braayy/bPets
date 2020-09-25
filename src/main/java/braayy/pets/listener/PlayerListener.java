package braayy.pets.listener;

import braayy.pets.Pets;
import braayy.pets.inventory.SimpleInventory;
import braayy.pets.inventory.pet.SeePetInventory;
import braayy.pets.model.Pet;
import braayy.pets.service.PetService;
import braayy.pets.service.SelectNameService;
import braayy.pets.util.NMSUtil;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Sittable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

import java.util.UUID;

public class PlayerListener implements Listener {

    private final SelectNameService selectNameService;
    private final PetService petService;

    public PlayerListener(Pets plugin) {
        this.selectNameService = plugin.getSelectNameService();
        this.petService = plugin.getPetService();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        SimpleInventory inventory = this.selectNameService.getHoldInventory(event.getPlayer());
        if (inventory != null) {
            event.setCancelled(true);

            Pet pet = inventory.getProperty("Pet");

            if (pet == null) return;

            if (!event.getMessage().matches("[a-zA-Z0-9 _]+")) {
                inventory.setProperty("InvalidName", true);

                inventory.setDirty();

                inventory.open(event.getPlayer());

                return;
            }

            String name = event.getMessage();

            if (name.length() > 16) name = name.substring(0, 16);

            pet.setName(name);
            inventory.removeProperty("InvalidName");

            inventory.setDirty();

            inventory.open(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.petService.loadPets(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.petService.unloadPets(event.getPlayer());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
            event.getFrom().getBlockY() == event.getTo().getBlockY() &&
            event.getFrom().getBlockZ() == event.getTo().getBlockZ()) return;

        Pet pet = this.petService.getSelectedPet(event.getPlayer());

        if (pet != null) {
            if (pet.getEntity() instanceof Sittable && ((Sittable) pet.getEntity()).isSitting()) return;

            double distSq = event.getPlayer().getLocation().distanceSquared(pet.getEntity().getLocation());

            if (distSq >= 10 * 10) {
                pet.getEntity().teleport(event.getTo());
            } else if (distSq >= 2.5 * 2.5) {
                Object handle = NMSUtil.getHandle(pet.getEntity());
                Object navigation = NMSUtil.getNavigation(handle);

                NMSUtil.move(navigation, event.getTo(), 1.65f);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryOpenEvent event) {
        if (event.getInventory() != null && event.getInventory().getHolder() instanceof Horse) {
            Horse horse = (Horse) event.getInventory().getHolder();

            if (horse.hasMetadata("Pet")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof Horse) {
            Horse horse = (Horse) event.getClickedInventory().getHolder();

            if (horse.hasMetadata("Pet")) {
                event.setCancelled(true);
            }
        }
    }

}