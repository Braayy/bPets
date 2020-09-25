package braayy.pets.command;

import braayy.pets.inventory.SimpleInventory;
import braayy.pets.inventory.pet.PetInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        SimpleInventory petInventory = new PetInventory();
        petInventory.setup(player);

        petInventory.open(player);

        return true;
    }

}
