import braayy.pets.model.Pet;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;

import java.util.UUID;

public class PetBitwiseTest {

    public static void main(String[] args) {
        Pet pet = new Pet("Piggo", EntityType.PIG, UUID.randomUUID(), (byte) 0);

        pet.setHorseStyle(Horse.Style.WHITE_DOTS);

        pet.setColor(Horse.Color.WHITE.ordinal());

        pet.setAdult(true);

        pet.setSaddle(false);

        pet.setArmor(Pet.HorseArmor.GOLD);

        System.out.println("Style: " + pet.getHorseStyle());
        System.out.println("Color: " + pet.getColor());
        System.out.println("Adult: " + pet.isAdult());
        System.out.println("Saddle: " + pet.isSaddle());
        System.out.println("Armor: " + pet.getArmor());
    }

}