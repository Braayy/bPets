package braayy.pets.service;

import braayy.pets.Pets;
import braayy.pets.model.Pet;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public abstract class PetService extends Service {

    public PetService(Pets plugin) {
        super(plugin);
    }

    public abstract void createPet(Pet pet);

    public abstract void updatePet(Pet pet);

    public abstract void loadPets(Player player);

    public abstract void unloadPets(Player player);

    public abstract List<Pet> getPets(Player player);

    public abstract Pet getPetByName(Player player, String name);

    public abstract Pet getPetById(Player player, UUID id);

    public abstract void selectPet(Player player, Pet pet);

    public abstract Pet getSelectedPet(Player player);

    public abstract void deletePet(Pet pet);
}