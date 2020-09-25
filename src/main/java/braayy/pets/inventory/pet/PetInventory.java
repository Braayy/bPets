package braayy.pets.inventory.pet;

import braayy.pets.Pets;
import braayy.pets.inventory.SimpleInventory;
import braayy.pets.model.Pet;
import braayy.pets.util.NamedItemStack;
import braayy.pets.util.TexturedSkullType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PetInventory extends SimpleInventory {

    public PetInventory() {
        super("Pets", 9 * 6, true);
    }

    @Override
    public void setup(Player player) {
        this.handleClick(9 * 5 + 4, (item) -> {
            SimpleInventory newPetInventory = new NewPetInventory(this);
            newPetInventory.setup(player);

            newPetInventory.open(player);
        });

        int x = 1, y = 1;

        while (true) {
            int slot = x + y * 9;

            this.handleClick(slot, (petHead) -> {
                if (petHead != null && petHead.getType() == Material.SKULL_ITEM) {
                    String petName = ChatColor.stripColor(petHead.getItemMeta().getDisplayName());

                    Pet pet = Pets.getInstance().getPetService().getPetByName(player, petName);

                    if (pet != null) {
                        SimpleInventory seePetInventory = new SeePetInventory(this);

                        seePetInventory.setProperty("Pet", pet);
                        seePetInventory.setProperty("ArmorIndex", pet.getArmor().ordinal());

                        seePetInventory.setup(player);

                        seePetInventory.open(player);
                    }
                }
            });

            x++;
            if (x == 7) {
                x = 0;
                y++;

                if (y == 4) break;
            }
        }
    }

    @Override
    public void draw(Player player) {
        this.setItem(9 * 5 + 4, new NamedItemStack(Material.BOOK, ChatColor.AQUA + "New Pet"));

        Pet selectedPet = Pets.getInstance().getPetService().getSelectedPet(player);

        List<Pet> pets = Pets.getInstance().getPetService().getPets(player);

        int x = 1, y = 1;

        for (Pet pet : pets) {
            ItemStack mobHead = TexturedSkullType
                    .getByEntityType(pet.getType())
                    .createHead(ChatColor.WHITE + pet.getName());

            if (selectedPet != null && selectedPet.getName().equals(pet.getName())) {
                //mobHead.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            this.setItem(x + y * 9, mobHead);

            x++;
            if (x == 7) {
                x = 0;
                y++;

                if (y == 4) break;
            }
        }
    }
}
