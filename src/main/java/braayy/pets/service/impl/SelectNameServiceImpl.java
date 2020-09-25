package braayy.pets.service.impl;

import braayy.pets.Pets;
import braayy.pets.inventory.SimpleInventory;
import braayy.pets.service.SelectNameService;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SelectNameServiceImpl extends SelectNameService {

    private final Map<UUID, SimpleInventory> holdInventoryMap;

    public SelectNameServiceImpl(Pets plugin) {
        super(plugin);

        this.holdInventoryMap = new HashMap<>();
    }

    @Override
    public void holdInventory(Player player, SimpleInventory inventory) {
        this.holdInventoryMap.put(player.getUniqueId(), inventory);
    }

    @Override
    public SimpleInventory getHoldInventory(Player player) {
        return this.holdInventoryMap.remove(player.getUniqueId());
    }
}
