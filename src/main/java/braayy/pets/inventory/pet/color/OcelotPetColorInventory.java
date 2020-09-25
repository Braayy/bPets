package braayy.pets.inventory.pet.color;

import braayy.pets.inventory.SimpleInventory;
import braayy.pets.model.Pet;
import braayy.pets.util.NamedItemStack;
import braayy.pets.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;

public class OcelotPetColorInventory extends PetColorInventory {

    public OcelotPetColorInventory(SimpleInventory parent) {
        super(parent);
    }

    @Override
    public void draw(Player player) {
        super.draw(player);

        Pet pet = this.getParent().getProperty("Pet");

        for (int i = 0; i < Ocelot.Type.values().length; i++) {
            Ocelot.Type type = Ocelot.Type.values()[i];

            DyeColor woolColor = getDyeColor(i);

            String woolName = Util.dyeColorToChatColor(woolColor) + Util.getFancyName(type.name());

            this.setItem(i, new NamedItemStack(Material.WOOL, 1, woolColor.getWoolData(), woolName));

            this.handleClick(i, (clickType) -> {
                /*String typeName = ChatColor.stripColor(clickType.getItemMeta().getDisplayName()).replace(' ', '_').toUpperCase();

                Ocelot.Type clickColor = Ocelot.Type.valueOf(typeName);*/

                pet.setColor(type.ordinal());

                this.getParent().setDirty();
                this.back(player);
            });
        }
    }

    public static DyeColor getDyeColor(int color) {
        Ocelot.Type oType = Ocelot.Type.values()[color];

        switch (oType) {
            case WILD_OCELOT: return DyeColor.WHITE;
            case RED_CAT: return DyeColor.ORANGE;
            case BLACK_CAT: return DyeColor.BLACK;
            case SIAMESE_CAT: return DyeColor.GRAY;
        }

        return DyeColor.WHITE;
    }
}
