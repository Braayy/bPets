package braayy.pets.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NamedItemStack extends ItemStack {

    public NamedItemStack(Material type, String name) {
        this(type, 1, (short) 0, name);
    }

    public NamedItemStack(Material type, int amount, short damage, String name) {
        super(type, amount, damage);

        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName(name);
        this.setItemMeta(meta);
    }

}