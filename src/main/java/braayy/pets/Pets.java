package braayy.pets;

import braayy.pets.command.PetCommand;
import braayy.pets.dao.PetDao;
import braayy.pets.dao.mysql.MySQLPetDao;
import braayy.pets.dao.sqlite.SQLitePetDao;
import braayy.pets.inventory.SimpleInventoryListener;
import braayy.pets.listener.EntityListener;
import braayy.pets.listener.PlayerListener;
import braayy.pets.service.DatabaseService;
import braayy.pets.service.MessageService;
import braayy.pets.service.PetService;
import braayy.pets.service.SelectNameService;
import braayy.pets.service.impl.DatabaseServiceImpl;
import braayy.pets.service.impl.MessageServiceImpl;
import braayy.pets.service.impl.PetServiceImpl;
import braayy.pets.service.impl.SelectNameServiceImpl;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Pets extends JavaPlugin {

    private static Pets instance;

    private DatabaseService databaseService;
    private MessageService messageService;
    private SelectNameService selectNameService;
    private PetService petService;

    private PetDao petDao;

    @Override
    public void onEnable() {
        instance = this;

        this.databaseService = new DatabaseServiceImpl(this);
        this.messageService = new MessageServiceImpl(this);
        this.selectNameService = new SelectNameServiceImpl(this);

        try {
            this.databaseService.enable();
            this.messageService.enable();
        } catch (Exception exception) {
            this.getLogger().log(Level.SEVERE, "Something went wrong while enabling services", exception);
        }

        this.initDaos();

        this.petService = new PetServiceImpl(this);

        SimpleInventoryListener.register(this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getServer().getPluginManager().registerEvents(new EntityListener(), this);

        this.getCommand("pet").setExecutor(new PetCommand());
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        this.getServer().getScheduler().cancelTasks(this);

        try {
            this.databaseService.disable();
            this.messageService.disable();
        } catch (Exception exception) {
            this.getLogger().log(Level.SEVERE, "Something went wrong while disabling services", exception);
        }
    }

    private void initDaos() {
        String type = this.getConfig().getString("Storage.Type");

        if (type.equalsIgnoreCase("mysql")) {
            this.petDao = new MySQLPetDao(this);
        } else {
            this.petDao = new SQLitePetDao(this);
        }

        this.petDao.createTable();
    }

    public void async(Runnable task) {
        this.getServer().getScheduler().runTaskAsynchronously(this, task);
    }

    public DatabaseService getDatabaseService() {
        return databaseService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public SelectNameService getSelectNameService() {
        return selectNameService;
    }

    public PetService getPetService() {
        return petService;
    }

    public PetDao getPetDao() {
        return petDao;
    }

    public static Pets getInstance() {
        return instance;
    }
}