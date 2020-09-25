package braayy.pets.inventory.pet;

import braayy.pets.Pets;
import braayy.pets.inventory.SimpleInventory;
import braayy.pets.inventory.pet.color.*;
import braayy.pets.model.Pet;
import braayy.pets.service.MessageService;
import braayy.pets.util.NamedItemStack;
import braayy.pets.util.TexturedSkullType;
import braayy.pets.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NewPetInventory extends SimpleInventory {

    public NewPetInventory(PetInventory parent) {
        super(parent, "New Pet", 9 * 3, true);
    }

    @Override
    public void setup(Player player) {
        this.setProperty("Pet", new Pet(null, null, player.getUniqueId(), (short) 0));
        this.setProperty("ArmorIndex", 0);

        // Back
        this.handleClick(0, () -> this.back(player));

        // Pet Name Handler
        this.handleClick(4, (item) -> {
            Pets.getInstance().getSelectNameService().holdInventory(player, this);
            Pets.getInstance().getMessageService().sendMessage(player, "gui.pet.select-name");

            player.closeInventory();
        });

        // Pet Color Handler
        this.handleClick(9 + 2, () -> {
            Pet pet = this.getProperty("Pet");

            if (Util.hasColor(pet.getType())) {
                SimpleInventory petColorInventory = PetColorInventory.getColorInventory(this, pet.getType());
                petColorInventory.setup(player);

                petColorInventory.open(player);
            }
        });

        // Pet Type Handler
        this.handleClick(9 + 4, () -> {
            SimpleInventory petTypeInventory = new PetTypeInventory(this);
            petTypeInventory.setup(player);

            petTypeInventory.open(player);
        });

        // Horse Style Handler
        this.handleClick(9 + 6, () -> {
            Pet pet = this.getProperty("Pet");

            if (pet.getType() == EntityType.HORSE) {
                SimpleInventory petColorInventory = new HorseStyleInventory(this);

                petColorInventory.open(player);
            }
        });

        // Adult Handler
        this.handleClick(18, () -> {
            Pet pet = this.getProperty("Pet");

            if (Ageable.class.isAssignableFrom(pet.getType().getEntityClass())) {
                pet.setAdult(!pet.isAdult());

                this.redraw(player);
            }
        });

        // Saddle Handler
        this.handleClick(19, () -> {
            Pet pet = this.getProperty("Pet");

            if (AbstractHorse.class.isAssignableFrom(pet.getType().getEntityClass()) || pet.getType() == EntityType.PIG) {
                pet.setSaddle(!pet.isSaddle());

                this.redraw(player);
            }
        });

        // Change Armor Handler
        this.handleClick(20, () -> {
            Pet pet = this.getProperty("Pet");

            if (AbstractHorse.class.isAssignableFrom(pet.getType().getEntityClass())) {
                int armorIndex = this.getProperty("ArmorIndex");

                armorIndex++;

                pet.setArmor(Pet.HorseArmor.values()[armorIndex % (3 + 1)]);

                this.setProperty("ArmorIndex", armorIndex);

                this.redraw(player);
            }
        });

        // Create Handler
        this.handleClick(this.getInventory().getSize() - 5, () -> {
            MessageService messageService = Pets.getInstance().getMessageService();

            if (this.getProperty("InvalidName") != null) {
                messageService.sendMessage(player, "gui.pet.invalid-name");

                return;
            }

            Pet pet = this.getProperty("Pet");

            if (pet.getName() == null) {
                messageService.sendMessage(player, "gui.pet.forgot-name");

                return;
            }

            if (pet.getType() == null) {
                messageService.sendMessage(player, "gui.pet.forgot-type");

                return;
            }

            Pets.getInstance().getPetService().createPet(pet);
            messageService.sendMessage(player, "gui.pet.success", "name", pet.getName());

            this.getParent().setDirty();
            this.back(player);
        });
    }

    @Override
    public void draw(Player player) {
        Pet pet = this.getProperty("Pet");

        this.setItem(0, new NamedItemStack(Material.ARROW, ChatColor.RED + "Back"));

        // Pet Name
        if (pet.getName() == null) {
            if (this.getProperty("InvalidName") == null) {
                this.setItem(4, new NamedItemStack(Material.PAPER, ChatColor.WHITE + "No Name"));
            } else {
                this.setItem(4, new NamedItemStack(Material.PAPER, ChatColor.RED + "Invalid Name"));
            }
        } else {
            this.setItem(4, new NamedItemStack(Material.PAPER, ChatColor.WHITE + pet.getName()));
        }

        // Pet Color
        if (Util.hasColor(pet.getType())) {
            this.setItem(9 + 2, new NamedItemStack(Material.WOOL, 1, pet.getWoolColor().getWoolData(), ChatColor.GOLD + "Pet Color"));
        }

        // Pet Type
        if (pet.getType() == null) {
            this.setItem(9 + 4, new NamedItemStack(Material.BARRIER, ChatColor.GOLD + "Pet Type"));
        } else {
            ItemStack mobHead = TexturedSkullType
                    .getByEntityType(pet.getType())
                    .createHead(ChatColor.GOLD + Util.getFancyName(pet.getType().name()));

            this.setItem(9 + 4, mobHead);
        }

        if (pet.getType() == EntityType.HORSE) {
            DyeColor color = HorseStyleInventory.getDyeColor(pet.getHorseStyle());

            this.setItem(9 + 6, new NamedItemStack(Material.WOOL, 1, color.getWoolData(), ChatColor.GOLD + "Horse Style"));
        }

        if (pet.getType() != null && Ageable.class.isAssignableFrom(pet.getType().getEntityClass())) {
            if (pet.isAdult()) {
                this.setItem(18, new NamedItemStack(Material.WOOL, 1, DyeColor.BLUE.getWoolData(), ChatColor.BLUE + "Adult"));
            } else {
                this.setItem(18, new NamedItemStack(Material.WOOL, 1, DyeColor.LIGHT_BLUE.getWoolData(), ChatColor.AQUA + "Baby"));
            }
        }

        if (pet.getType() != null && AbstractHorse.class.isAssignableFrom(pet.getType().getEntityClass()) || pet.getType() == EntityType.PIG) {
            if (pet.isSaddle()) {
                this.setItem(19, new NamedItemStack(Material.SADDLE, ChatColor.GREEN + "Saddle"));
            } else {
                this.setItem(19, new NamedItemStack(Material.SADDLE, ChatColor.RED + "Saddle"));
            }
        }

        if (pet.getType() != null && AbstractHorse.class.isAssignableFrom(pet.getType().getEntityClass())) {
            this.setItem(20, new NamedItemStack(pet.getArmor().material, ChatColor.GRAY + "Horse Armor"));
        }

        this.setItem(this.getInventory().getSize() - 5, new NamedItemStack(Material.BOOK, ChatColor.AQUA + "Create Pet"));
    }

}
