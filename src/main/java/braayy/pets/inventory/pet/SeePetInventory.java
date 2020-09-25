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

public class SeePetInventory extends SimpleInventory {

    public SeePetInventory(SimpleInventory parent) {
        super(parent, "See a Pet", 9 * 3, true);
    }

    @Override
    public void setup(Player player) {
        // Back
        this.handleClick(0, () -> this.back(player));

        // Select
        this.handleClick(8, () -> {
            Pet pet = this.getProperty("Pet");
            Pet selectedPet = Pets.getInstance().getPetService().getSelectedPet(player);

            if (selectedPet != null && selectedPet.getName().equals(pet.getName())) {
                selectedPet.remove();

                Pets.getInstance().getPetService().selectPet(player, null);

                player.closeInventory();

                return;
            }

            if (selectedPet != null) {
                selectedPet.remove();
            }

            pet.spawn(player, player.getLocation());

            Pets.getInstance().getPetService().selectPet(player, pet);

            player.closeInventory();
        });

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
                SimpleInventory horseStyleInventory = new HorseStyleInventory(this);

                horseStyleInventory.open(player);
            }
        });

        // Edit Handler
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

            Pets.getInstance().getPetService().updatePet(pet);

            messageService.sendMessage(player, "gui.pet.success-edit", "name", pet.getName());

            if (this.getParent() != null) {
                this.getParent().setDirty();
                this.back(player);
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

        // Delete Handler
        this.handleClick(this.getInventory().getSize() - 1, () -> {
            SimpleInventory confirmDeleteInventory = new ConfirmDeleteInventory(this);
            confirmDeleteInventory.setup(player);

            confirmDeleteInventory.open(player);
        });
    }

    @Override
    public void draw(Player player) {
        Pet pet = this.getProperty("Pet");

        this.setItem(0, new NamedItemStack(Material.ARROW, ChatColor.RED + "Back"));

        Pet selectedPet = Pets.getInstance().getPetService().getSelectedPet(player);

        if (selectedPet != null && selectedPet.getName().equals(pet.getName())) {
            this.setItem(8, new NamedItemStack(Material.FEATHER, ChatColor.RED + "Unselect"));
        } else {
            this.setItem(8, new NamedItemStack(Material.FEATHER, ChatColor.GREEN + "Select"));
        }

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

        this.setItem(this.getInventory().getSize() - 1, new NamedItemStack(Material.WOOL, 1, DyeColor.RED.getWoolData(), ChatColor.RED + "Delete"));

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

        this.setItem(this.getInventory().getSize() - 5, new NamedItemStack(Material.BOOK, ChatColor.AQUA + "Edit Pet"));
    }

    public static class ConfirmDeleteInventory extends SimpleInventory {

        public ConfirmDeleteInventory(SimpleInventory parent) {
            super(parent, "Are you sure?", 9 * 3, true);
        }

        @Override
        public void setup(Player player) {
            this.handleClick(12, () -> {
                Pet pet = this.getParent().getProperty("Pet");

                Pets.getInstance().getPetService().deletePet(pet);

                pet.remove();

                Pet selectedPet = Pets.getInstance().getPetService().getSelectedPet(player);

                if (selectedPet != null && selectedPet.getId().equals(pet.getId())) {
                    Pets.getInstance().getPetService().selectPet(player, null);
                }

                if (this.getParent().getParent() != null) {
                    this.getParent().getParent().setDirty();
                    this.getParent().back(player);
                }
            });

            this.handleClick(14, () -> this.back(player));
        }

        @Override
        public void draw(Player player) {
            Pet pet = this.getParent().getProperty("Pet");

            this.setItem(12, new NamedItemStack(Material.WOOL, 1, DyeColor.GREEN.getWoolData(), ChatColor.GREEN + "Delete " + pet.getName()));
            this.setItem(14, new NamedItemStack(Material.WOOL, 1, DyeColor.RED.getWoolData(), ChatColor.RED + "Cancel"));
        }
    }
}
