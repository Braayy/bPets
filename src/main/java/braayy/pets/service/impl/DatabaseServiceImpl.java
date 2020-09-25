package braayy.pets.service.impl;

import braayy.pets.Pets;
import braayy.pets.service.DatabaseService;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.sql.Connection;
import java.util.logging.Level;

public class DatabaseServiceImpl extends DatabaseService {

    private HikariDataSource dataSource;

    public DatabaseServiceImpl(Pets plugin) {
        super(plugin);
    }

    @Override
    public void enable() {
        String type = this.plugin.getConfig().getString("Storage.Type");

        this.dataSource = new HikariDataSource();

        if (type.equalsIgnoreCase("mysql")) {
            String host = this.plugin.getConfig().getString("Storage.Host");
            String port = this.plugin.getConfig().getString("Storage.Port");
            String user = this.plugin.getConfig().getString("Storage.User");
            String pass = this.plugin.getConfig().getString("Storage.Pass");
            String database = this.plugin.getConfig().getString("Storage.Database");

            this.dataSource.setJdbcUrl("jdbc:mysql://" + host + ':' + port + '/' + database + "?useSSL=false");
            this.dataSource.setUsername(user);
            this.dataSource.setPassword(pass);
        } else {
            String sqliteFilename = this.plugin.getConfig().getString("Storage.Filename");

            this.dataSource.setJdbcUrl(
                    "jdbc:sqlite://" + this.plugin.getDataFolder().getAbsolutePath() + File.separatorChar + sqliteFilename
            );
        }

        this.dataSource.setMaximumPoolSize(5);

        this.plugin.getLogger().info(type + " Database connected with success");
    }

    @Override
    public void disable() {
        if (this.dataSource != null) {
            this.dataSource.close();
        }

        this.plugin.getLogger().info("Successfully closed connections with the database");
    }

    @Override
    public Connection getConnection() {
        try {
            return this.dataSource != null ? this.dataSource.getConnection() : null;
        } catch (Exception exception) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not get a proper connection with database", exception);

            return null;
        }
    }

}