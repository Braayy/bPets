package braayy.pets.inventory.pet.color;

import braayy.pets.inventory.SimpleInventory;
import braayy.pets.model.Pet;
import braayy.pets.util.NamedItemStack;
import braayy.pets.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GenericPetColorInventory extends PetColorInventory {

    public GenericPetColorInventory(SimpleInventory parent) {
        super(parent);
    }

    @Override
    public void draw(Player player) {
        super.draw(player);

        Pet pet = this.getParent().getProperty("Pet");

        for (int i = 0; i < DyeColor.values().length; i++) {
            DyeColor color = DyeColor.values()[i];

            ChatColor cColor = Util.dyeColorToChatColor(color);

            String woolName = cColor + Util.getFancyName(color.name());

            this.setItem(i, new NamedItemStack(Material.WOOL, 1, color.getWoolData(), woolName));

            this.handleClick(i, (clickedWool) -> {
                /*String typeName = ChatColor.stripColor(clickedWool.getItemMeta().getDisplayName()).replace(' ', '_').toUpperCase();

                DyeColor clickColor = DyeColor.valueOf(typeName);*/

                pet.setColor(color.getWoolData());

                this.getParent().setDirty();
                this.back(player);
            });
        }
    }

    public static DyeColor getDyeColor(int color) {
        return DyeColor.getByWoolData((byte) color);
    }

}
