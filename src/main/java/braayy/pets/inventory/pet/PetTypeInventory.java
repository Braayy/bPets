package braayy.pets.inventory.pet;

import braayy.pets.inventory.SimpleInventory;
import braayy.pets.model.Pet;
import braayy.pets.util.NamedItemStack;
import braayy.pets.util.TexturedSkullType;
import braayy.pets.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PetTypeInventory extends SimpleInventory {

    public PetTypeInventory(SimpleInventory parent) {
        super(parent, "Pet Type", 9 * 3, true);
    }

    @Override
    public void setup(Player player) {
        // Back
        this.handleClick(this.getInventory().getSize() - 1, () -> this.back(player));
    }

    @Override
    public void draw(Player player) {
        int slot = 0;

        for (EntityType entityType : EntityType.values()) {
            if (entityType.getEntityClass() != null && Animals.class.isAssignableFrom(entityType.getEntityClass())) {
                ItemStack mobHead = TexturedSkullType
                        .getByEntityType(entityType)
                        .createHead(ChatColor.GOLD + Util.getFancyName(entityType.name()));

                this.setItem(slot, mobHead);

                this.handleClick(slot, (clickedHead) -> {
                    String typeName = ChatColor.stripColor(clickedHead.getItemMeta().getDisplayName()).replace(' ', '_').toUpperCase();
                    EntityType selectedType = EntityType.valueOf(typeName);

                    Pet pet = this.getParent().getProperty("Pet");

                    pet.setType(selectedType);

                    this.getParent().setDirty();

                    this.back(player);
                });

                slot++;
            }
        }

        // Back
        this.setItem(this.getInventory().getSize() - 1, new NamedItemStack(Material.ARROW, ChatColor.RED + "Back"));
    }
}
