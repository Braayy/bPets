package braayy.pets.dao.mysql;

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

public class MySQLPetDao extends PetDao {

    public MySQLPetDao(Pets plugin) {
        super(plugin);
    }

    @Override
    public void createTable() {
        try (Connection connection = this.databaseService.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS bpets_pets(id BINARY(16) NOT NULL, name VARCHAR(16) NOT NULL, type SMALLINT NOT NULL, owner BINARY(16) NOT NULL, style SMALLINT not null, PRIMARY KEY(id, owner))"
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
                     "INSERT INTO bpets_pets VALUES(UNHEX(?), ?, ?, UNHEX(?), ?)"
             )) {

            stmt.setString(1, pet.getId().toString().replace("-", ""));
            stmt.setString(2, pet.getName());
            stmt.setShort(3, (short) pet.getType().ordinal());
            stmt.setString(4, pet.getOwner().toString().replace("-", ""));
            stmt.setShort(5, pet.getStyle());

            stmt.executeUpdate();
        } catch (Exception exception) {
            this.databaseService.getLogger().log(Level.SEVERE, "Something went while creating " + pet.getOwner() + "'s pet", exception);
        }
    }

    @Override
    public void update(Pet pet) {
        try (Connection connection = this.databaseService.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE bpets_pets SET name = ?, type = ?, style = ? WHERE owner = UNHEX(?) AND id = UNHEX(?)"
             )) {

            stmt.setString(1, pet.getName());
            stmt.setShort(2, (short) pet.getType().ordinal());
            stmt.setShort(3, pet.getStyle());

            stmt.setString(4, pet.getOwner().toString().replace("-", ""));
            stmt.setString(5, pet.getId().toString().replace("-", ""));

            stmt.executeUpdate();
        } catch (Exception exception) {
            this.databaseService.getLogger().log(Level.SEVERE, "Something went while updating " + pet.getOwner() + "'s pet", exception);
        }
    }

    @Override
    public void delete(Pet pet) {
        try (Connection connection = this.databaseService.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "DELETE FROM bpets_pets WHERE owner = UNHEX(?) AND name = UNHEX(?)"
             )) {

            stmt.setString(1, pet.getOwner().toString().replace("-", ""));
            stmt.setString(2, pet.getId().toString().replace("-", ""));

            stmt.executeUpdate();
        } catch (Exception exception) {
            this.databaseService.getLogger().log(Level.SEVERE, "Something went while deleting " + pet.getOwner() + "'s pet", exception);
        }
    }

    @Override
    public Pet load(Pair<UUID, UUID> pair) { return null; }

    @Override
    public List<Pet> loadPets(UUID owner) {
        try (Connection connection = this.databaseService.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT HEX(id), name, type, style WHERE owner = UNHEX(?)"
             )) {

            stmt.setString(1, owner.toString().replace("-", ""));

            try (ResultSet set = stmt.executeQuery()) {
                List<Pet> pets = new ArrayList<>();

                while (set.next()) {
                    UUID id = Util.blobToUUID(set.getString(1));
                    String name = set.getString("name");
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

}
