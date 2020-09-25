package braayy.pets.service;

import braayy.pets.Pets;

import java.sql.Connection;
import java.util.logging.Logger;

public abstract class DatabaseService extends Service {

    public DatabaseService(Pets plugin) {
        super(plugin);
    }

    public abstract Connection getConnection();

    public Logger getLogger() {
        return this.plugin.getLogger();
    }

}