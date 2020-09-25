package braayy.pets.inventory.pet.color;

import braayy.pets.inventory.SimpleInventory;
import braayy.pets.model.Pet;
import braayy.pets.util.NamedItemStack;
import braayy.pets.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;

public class RabbitPetColorInventory extends PetColorInventory {

    public RabbitPetColorInventory(SimpleInventory parent) {
        super(parent);
    }

    @Override
    public void draw(Player player) {
        super.draw(player);

        Pet pet = this.getParent().getProperty("Pet");

        for (int i = 0; i < Rabbit.Type.values().length - 1; i++) {
            Rabbit.Type type = Rabbit.Type.values()[i];

            DyeColor woolColor = getDyeColor(i);

            String woolName = Util.dyeColorToChatColor(woolColor) + Util.getFancyName(type.name());

            this.setItem(i, new NamedItemStack(Material.WOOL, 1, woolColor.getWoolData(), woolName));

            this.handleClick(i, (clickType) -> {
                /*String typeName = ChatColor.stripColor(clickType.getItemMeta().getDisplayName()).replace(' ', '_').toUpperCase();

                Rabbit.Type clickColor = Rabbit.Type.valueOf(typeName);*/

                pet.setColor(type.ordinal());

                this.getParent().setDirty();
                this.back(player);
            });
        }
    }

    public static DyeColor getDyeColor(int color) {
        Rabbit.Type rType = Rabbit.Type.values()[color];

        switch (rType) {
            case BROWN: return DyeColor.BROWN;
            case BLACK: return DyeColor.BLACK;
            case BLACK_AND_WHITE:
            case SALT_AND_PEPPER:
                return DyeColor.GRAY;
            case GOLD: return DyeColor.ORANGE;
        }

        return DyeColor.WHITE;
    }
}
