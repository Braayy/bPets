package braayy.pets.inventory.pet.color;

import braayy.pets.inventory.SimpleInventory;
import braayy.pets.model.Pet;
import braayy.pets.util.NamedItemStack;
import braayy.pets.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;

import javax.sql.rowset.CachedRowSet;

public class ParrotPetColorInventory extends PetColorInventory {

    public ParrotPetColorInventory(SimpleInventory parent) {
        super(parent);
    }

    @Override
    public void draw(Player player) {
        super.draw(player);

        Pet pet = this.getParent().getProperty("Pet");

        for (int i = 0; i < Parrot.Variant.values().length; i++) {
            Parrot.Variant color = Parrot.Variant.values()[i];

            DyeColor woolColor = getDyeColor(i);

            String woolName = Util.dyeColorToChatColor(woolColor) + Util.getFancyName(color.name());

            this.setItem(i, new NamedItemStack(Material.WOOL, 1, woolColor.getWoolData(), woolName));

            this.handleClick(i, (clickedWool) -> {
                /*String typeName = ChatColor.stripColor(clickedWool.getItemMeta().getDisplayName()).replace(' ', '_').toUpperCase();

                Parrot.Variant clickColor = Parrot.Variant.valueOf(typeName);*/

                pet.setColor(color.ordinal());

                this.getParent().setDirty();
                this.back(player);
            });
        }
    }

    public static DyeColor getDyeColor(int color) {
        Parrot.Variant pType = Parrot.Variant.values()[color];

        switch (pType) {
            case RED: return DyeColor.RED;
            case BLUE: return DyeColor.BLUE;
            case GREEN: return DyeColor.GREEN;
            case CYAN: return DyeColor.CYAN;
            case GRAY: return DyeColor.GRAY;
        }

        return DyeColor.WHITE;
    }

}
