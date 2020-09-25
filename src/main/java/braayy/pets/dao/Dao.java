package braayy.pets.dao;


import braayy.pets.Pets;
import braayy.pets.service.DatabaseService;

public abstract class Dao<K, V> {

    protected final DatabaseService databaseService;

    public Dao(Pets plugin) {
        this.databaseService = plugin.getDatabaseService();
    }

    public abstract void createTable();

    public abstract void create(V value);

    public abstract void update(V value);

    public abstract void delete(V value);

    public abstract V load(K key);

}