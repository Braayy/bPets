package braayy.pets.service;

import braayy.pets.Pets;
import braayy.pets.inventory.SimpleInventory;
import org.bukkit.entity.Player;

public abstract class SelectNameService extends Service {

    public SelectNameService(Pets plugin) {
        super(plugin);
    }

    @Override
    public void enable() {}

    @Override
    public void disable() {}

    public abstract void holdInventory(Player player, SimpleInventory inventory);

    public abstract SimpleInventory getHoldInventory(Player player);

}