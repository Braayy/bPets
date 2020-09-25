package braayy.pets.inventory.pet.color;

import braayy.pets.inventory.SimpleInventory;
import braayy.pets.util.NamedItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public abstract class PetColorInventory extends SimpleInventory {

    public PetColorInventory(SimpleInventory parent) {
        super(parent, "Pet Color", 9 * 3, true);
    }

    @Override
    public void setup(Player player) {
        // Back
        this.handleClick(this.getInventory().getSize() - 1, () -> this.back(player));
    }

    @Override
    public void draw(Player player) {
        // Back
        this.setItem(this.getInventory().getSize() - 1, new NamedItemStack(Material.ARROW, ChatColor.RED + "Back"));
    }

    public static SimpleInventory getColorInventory(SimpleInventory parent, EntityType type) {
        if (type == null) return null;

        switch (type) {
            case WOLF:
            case SHEEP: return new GenericPetColorInventory(parent);
            case OCELOT: return new OcelotPetColorInventory(parent);
            case RABBIT: return new RabbitPetColorInventory(parent);
            case PARROT: return new ParrotPetColorInventory(parent);
            case HORSE: return new HorsePetColorInventory(parent);

            default: return null;
        }
    }
}
