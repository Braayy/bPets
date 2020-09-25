package braayy.pets.inventory;

import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface EmptyClickHandler extends ClickHandler {

    void handle();

    @Override
    default void handle(ItemStack stack) {
        this.handle();
    }
}