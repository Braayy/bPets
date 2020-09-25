package braayy.pets.dao.sqlite;

import braayy.pets.Pets;
import braayy.pets.dao.PetDao;
import braayy.pets.model.Pet;
import braayy.pets.util.Pair;
import braayy.pets.util.Util;
import org.bukkit.entity.EntityType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class SQLitePetDao extends PetDao {

    public SQLitePetDao(Pets plugin) {
        super(plugin);
    }

    @Override
    public void createTable() {
        try (Connection connection = this.databaseService.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS bpets_pets(id BINARY(16) NOT NULL, pet_name VARCHAR(16) NOT NULL, type SMALLINT NOT NULL, owner BINARY(16) NOT NULL, style SMALLINT not null, PRIMARY KEY(id, owner))"
             )) {

            stmt.executeUpdate();
        } catch (Exception exception) {
            this.databaseService.getLogger().log(Level.SEVERE, "Something went while creating table bpets_pets", exception);
        }
    }

    @Override
    public void create(Pet pet) {
        try (Connection connection = this.databaseService.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     replaceUUID("INSERT INTO bpets_pets VALUES(X'%s', ?, ?, X'%s', ?)", pet.getId(), pet.getOwner())
             )) {

            stmt.setString(1, pet.getName());
            stmt.setShort(2, (short) pet.getType().ordinal());
            stmt.setShort(3, pet.getStyle());

            stmt.executeUpdate();
        } catch (Exception exception) {
            this.databaseService.getLogger().log(Level.SEVERE, "Something went while creating " + pet.getOwner() + "'s pet", exception);
        }
    }

    @Override
    public void update(Pet pet) {
        try (Connection connection = this.databaseService.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     replaceUUID("UPDATE bpets_pets SET pet_name = ?, type = ?, style = ? WHERE id = X'%S' AND owner = X'%s'", pet.getId(), pet.getOwner())
             )) {

            stmt.setString(1, pet.getName());
            stmt.setShort(2, (short) pet.getType().ordinal());
            stmt.setShort(3, pet.getStyle());

            stmt.executeUpdate();
        } catch (Exception exception) {
            this.databaseService.getLogger().log(Level.SEVERE, "Something went while updating " + pet.getOwner() + "'s pet", exception);
        }
    }

    @Override
    public void delete(Pet pet) {
        try (Connection connection = this.databaseService.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     replaceUUID("DELETE FROM bpets_pets WHERE id = X'%s' AND owner = X'%s'", pet.getId(), pet.getOwner())
             )) {

            stmt.executeUpdate();
        } catch (Exception exception) {
            this.databaseService.getLogger().log(Level.SEVERE, "Something went while deleting " + pet.getOwner() + "'s pet", exception);
        }
    }

    @Override
    public Pet load(Pair<UUID, UUID> key) { return null; }

    @Override
    public List<Pet> loadPets(UUID owner) {
        try (Connection connection = this.databaseService.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     replaceUUID("SELECT HEX(id), pet_name, type, style FROM bpets_pets WHERE owner = X'%s'", null, owner)
             )) {

            try (ResultSet set = stmt.executeQuery()) {
                List<Pet> pets = new ArrayList<>();

                while (set.next()) {
                    UUID id = Util.blobToUUID(set.getString(1));
                    String name = set.getString("pet_name");
                    EntityType type = EntityType.values()[set.getShort("type")];
                    short style = set.getShort("style");

                    pets.add(new Pet(id, name, type, owner, style));
                }

                return pets;
            }
        } catch (Exception exception) {
            this.databaseService.getLogger().log(Level.SEVERE, "Something went while loading " + owner + "'s pet", exception);
        }

        return null;
    }

    private static String replaceUUID(String sql, UUID petId, UUID ownerId) {
        if (petId == null) {
            return String.format(sql, ownerId.toString().replace("-", ""));
        }

        return String.format(sql, petId.toString().replace("-", ""), ownerId.toString().replace("-", ""));
    }

}
