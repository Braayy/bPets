package braayy.pets.inventory.pet.color;

import braayy.pets.inventory.SimpleInventory;
import braayy.pets.model.Pet;
import braayy.pets.util.NamedItemStack;
import braayy.pets.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

public class HorsePetColorInventory extends PetColorInventory {

    public HorsePetColorInventory(SimpleInventory parent) {
        super(parent);
    }

    @Override
    public void draw(Player player) {
        super.draw(player);

        Pet pet = this.getParent().getProperty("Pet");

        for (int i = 0; i < Horse.Color.values().length; i++) {
            Horse.Color color = Horse.Color.values()[i];

            DyeColor woolColor = getDyeColor(i);

            String woolName = Util.dyeColorToChatColor(woolColor) + Util.getFancyName(color.name());

            this.setItem(i, new NamedItemStack(Material.WOOL, 1, woolColor.getWoolData(), woolName));

            this.handleClick(i, (clickedWool) -> {
                /*String typeName = ChatColor.stripColor(clickedWool.getItemMeta().getDisplayName()).replace(' ', '_').toUpperCase();

                Horse.Color clickColor = Horse.Color.valueOf(typeName);*/

                pet.setColor(color.ordinal());

                this.getParent().setDirty();
                this.back(player);
            });
        }
    }

    public static DyeColor getDyeColor(int color) {
        Horse.Color hColor = Horse.Color.values()[color];

        switch (hColor) {
            case WHITE: return DyeColor.WHITE;
            case BLACK: return DyeColor.BLACK;
            case GRAY: return DyeColor.GRAY;
            case CHESTNUT:
            case CREAMY: return DyeColor.ORANGE;

            default: return DyeColor.BROWN;
        }
    }

}
