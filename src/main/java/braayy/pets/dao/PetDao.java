package braayy.pets.dao;

import braayy.pets.Pets;
import braayy.pets.model.Pet;
import braayy.pets.util.Pair;

import java.util.List;
import java.util.UUID;

public abstract class PetDao extends Dao<Pair<UUID, UUID>, Pet> {

    public PetDao(Pets plugin) {
        super(plugin);
    }

    public abstract List<Pet> loadPets(UUID owner);
}