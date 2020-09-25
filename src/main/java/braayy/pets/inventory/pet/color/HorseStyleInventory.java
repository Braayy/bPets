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

public class HorseStyleInventory extends SimpleInventory {

    public HorseStyleInventory(SimpleInventory parent) {
        super(parent, "Horse Style", 9 * 3, true);
    }

    @Override
    public void setup(Player player) {
        // Back
        this.handleClick(this.getInventory().getSize() - 1, () -> this.back(player));
    }

    @Override
    public void draw(Player player) {
        Pet pet = this.getParent().getProperty("Pet");

        for (int i = 0; i < Horse.Style.values().length; i++) {
            Horse.Style style = Horse.Style.values()[i];

            DyeColor woolColor = getDyeColor(style);

            String woolName = Util.dyeColorToChatColor(woolColor) + Util.getFancyName(style.name());

            this.setItem(i, new NamedItemStack(Material.WOOL, 1, woolColor.getWoolData(), woolName));

            this.handleClick(i, (clickedWool) -> {
                /*String typeName = ChatColor.stripColor(clickedWool.getItemMeta().getDisplayName()).replace(' ', '_').toUpperCase();

                Horse.Style clickStyle = Horse.Style.valueOf(typeName);*/

                pet.setHorseStyle(style);

                this.getParent().setDirty();
                this.back(player);
            });
        }

        // Back
        this.setItem(this.getInventory().getSize() - 1, new NamedItemStack(Material.ARROW, ChatColor.RED + "Back"));
    }

    public static DyeColor getDyeColor(Horse.Style style) {
        switch (style) {
            case WHITEFIELD:
            case WHITE_DOTS:
            case WHITE: return DyeColor.WHITE;
            case BLACK_DOTS: return DyeColor.BLACK;

            default: return DyeColor.BROWN;
        }
    }

}
