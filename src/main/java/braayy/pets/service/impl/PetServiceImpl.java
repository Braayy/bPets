package braayy.pets.service.impl;

import braayy.pets.Pets;
import braayy.pets.dao.PetDao;
import braayy.pets.model.Pet;
import braayy.pets.service.PetService;
import org.bukkit.entity.Player;

import java.util.*;

public class PetServiceImpl extends PetService {

    private final Map<UUID, List<Pet>> petsMap;
    private final Map<UUID, Pet> selectedPetMap;

    private final PetDao petDao;

    public PetServiceImpl(Pets plugin) {
        super(plugin);

        this.petsMap = new HashMap<>();
        this.selectedPetMap = new HashMap<>();

        this.petDao = plugin.getPetDao();
    }

    @Override
    public void enable() {}

    @Override
    public void disable() {}

    @Override
    public void createPet(Pet pet) {
        List<Pet> pets = this.petsMap.getOrDefault(pet.getOwner(), new ArrayList<>());

        pets.add(pet);

        this.petsMap.put(pet.getOwner(), pets);

        this.plugin.async(() -> this.petDao.create(pet));
    }

    @Override
    public void updatePet(Pet pet) {
        this.petDao.update(pet);
    }

    @Override
    public void loadPets(Player player) {
        List<Pet> pets = this.petDao.loadPets(player.getUniqueId());

        if (pets != null) {
            this.petsMap.put(player.getUniqueId(), pets);
        }
    }

    @Override
    public void unloadPets(Player player) {
        List<Pet> pets = this.petsMap.remove(player.getUniqueId());
        if (pets != null) {
            pets.clear();
        }
    }

    @Override
    public List<Pet> getPets(Player player) {
        return this.petsMap.get(player.getUniqueId());
    }

    @Override
    public Pet getPetByName(Player player, String name) {
        List<Pet> pets = this.petsMap.get(player.getUniqueId());

        if (pets != null) {
            for (Pet pet : pets) {
                if (pet.getName().equalsIgnoreCase(name)) return pet;
            }
        }

        return null;
    }

    @Override
    public Pet getPetById(Player player, UUID id) {
        List<Pet> pets = this.petsMap.get(player.getUniqueId());

        if (pets != null) {
            for (Pet pet : pets) {
                if (pet.getId().equals(id)) return pet;
            }
        }

        return null;
    }

    @Override
    public void selectPet(Player player, Pet pet) {
        if (pet == null) {
            this.selectedPetMap.remove(player.getUniqueId());
        } else {
            this.selectedPetMap.put(player.getUniqueId(), pet);
        }
    }

    @Override
    public Pet getSelectedPet(Player player) {
        return this.selectedPetMap.get(player.getUniqueId());
    }

    @Override
    public void deletePet(Pet pet) {
        List<Pet> pets = this.petsMap.get(pet.getOwner());

        if (pets != null) {
            pets.remove(pet);
        }

        this.petDao.delete(pet);
    }

}
