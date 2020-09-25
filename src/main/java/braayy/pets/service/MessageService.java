package braayy.pets.service;

import braayy.pets.Pets;
import org.bukkit.command.CommandSender;

public abstract class MessageService extends Service {

    public MessageService(Pets plugin) {
        super(plugin);
    }

    public abstract String[] get(String key, Object... args);

    public abstract String getAsString(String key, Object... args);

    public abstract void sendMessage(CommandSender sender, String key, Object... args);
}
