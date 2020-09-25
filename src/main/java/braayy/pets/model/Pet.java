package braayy.pets.model;

import braayy.pets.Pets;
import braayy.pets.inventory.pet.color.*;
import lombok.*;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ArmoredHorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Objects;
import java.util.UUID;

@Getter
public class Pet {

    public static final int COLOR_MASK = 0xF000;
    public static final short HORSE_STYLE_MASK = 0xF00;
    public static final short ADULT_MASK = 0x80;
    public static final short SADDLE_MASK = 0x40;
    public static final short ARMOR_MASK = 0x30;

    private final UUID id;
    @Setter private String name;
    @Setter private EntityType type;
    private final UUID owner;

    private short style;

    private LivingEntity entity;

    public Pet(String name, EntityType type, UUID owner, short style) {
        this(UUID.randomUUID(), name, type, owner, style);
    }

    public Pet(UUID id, String name, EntityType type, UUID owner, short style) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.owner = owner;
        this.style = style;
    }

    public void setColor(int color) {
        this.style = (short) ((this.style & ~COLOR_MASK) | (color << 12));
    }

    public void setHorseStyle(Horse.Style style) {
        this.style = (short) ((this.style & ~HORSE_STYLE_MASK) | (style.ordinal() << 8));
    }

    public void setAdult(boolean adult) {
        if (this.isAdult() != adult) {
            this.style = (short) (this.style ^ ADULT_MASK);
        }
    }

    public void setSaddle(boolean saddle) {
        if (this.isSaddle() != saddle) {
            this.style = (short) (this.style ^ SADDLE_MASK);
        }
    }

    public void setArmor(HorseArmor armor) {
        this.style = (short) ((this.style & ~ARMOR_MASK) | (armor.ordinal() << 4));
    }

    public int getColor() {
        return (this.style & COLOR_MASK) >> 12;
    }

    public Horse.Style getHorseStyle() {
        return Horse.Style.values()[(this.style & HORSE_STYLE_MASK) >> 8];
    }

    public boolean isAdult() {
        return (this.style & ADULT_MASK) > 0;
    }

    public boolean isSaddle() {
        return (this.style & SADDLE_MASK) > 0;
    }

    public HorseArmor getArmor() {
        return HorseArmor.values()[(this.style & ARMOR_MASK) >> 4];
    }

    public DyeColor getWoolColor() {
        if (this.type == null) return DyeColor.WHITE;

        switch (this.type) {
            case WOLF:
            case SHEEP: return GenericPetColorInventory.getDyeColor(this.getColor());
            case OCELOT: return OcelotPetColorInventory.getDyeColor(this.getColor());
            case PARROT: return ParrotPetColorInventory.getDyeColor(this.getColor());
            case HORSE: return HorsePetColorInventory.getDyeColor(this.getColor());
            case RABBIT: return RabbitPetColorInventory.getDyeColor(this.getColor());

            default: return DyeColor.WHITE;
        }
    }

    public void spawn(Player player, Location location) {
        this.entity = (LivingEntity) location.getWorld().spawnEntity(location.add(player.getLocation().getDirection().normalize().multiply(2).setY(0)), this.type);
        this.entity.setCustomName(this.name);
        this.entity.setCustomNameVisible(true);
        this.entity.setCanPickupItems(false);

        this.entity.setMetadata("Pet", new FixedMetadataValue(Pets.getInstance(), this.id));

        if (this.entity instanceof Ageable) {
            if (this.isAdult()) {
                ((Ageable) this.entity).setAdult();
            } else {
                ((Ageable) this.entity).setBaby();
            }
        }

        if (this.entity instanceof Tameable) {
            ((Tameable) this.entity).setTamed(true);
            ((Tameable) this.entity).setOwner(player);
        }

        if (this.entity instanceof AbstractHorse) {
            if (this.isSaddle()) {
                ((AbstractHorse) this.entity).getInventory().setSaddle(new ItemStack(Material.SADDLE));
            }

            if (((AbstractHorse) this.entity).getInventory() instanceof ArmoredHorseInventory) {
                ((ArmoredHorseInventory) ((AbstractHorse) this.entity).getInventory()).setArmor(new ItemStack(this.getArmor().material));
            }
        }

        switch (this.type) {
            case WOLF: {
                Wolf wolf = (Wolf) this.entity;

                wolf.setAngry(false);

                DyeColor color = DyeColor.getByWoolData((byte) this.getColor());
                wolf.setCollarColor(color);

                break;
            }
            case SHEEP: {
                Sheep sheep = (Sheep) this.entity;

                DyeColor color = DyeColor.getByWoolData((byte) this.getColor());
                sheep.setColor(color);

                break;
            }
            case OCELOT: {
                Ocelot ocelot = (Ocelot) this.entity;

                Ocelot.Type type = Ocelot.Type.values()[this.getColor()];
                ocelot.setCatType(type);

                break;
            }
            case RABBIT: {
                Rabbit rabbit = (Rabbit) this.entity;

                Rabbit.Type type = Rabbit.Type.values()[this.getColor()];
                rabbit.setRabbitType(type);

                break;
            }
            case PARROT: {
                Parrot parrot = (Parrot) this.entity;

                Parrot.Variant type = Parrot.Variant.values()[this.getColor()];
                parrot.setVariant(type);

                break;
            }
            case HORSE: {
                Horse horse = (Horse) this.entity;

                Horse.Color color = Horse.Color.values()[this.getColor()];
                Horse.Style style = this.getHorseStyle();

                horse.setColor(color);
                horse.setStyle(style);

                break;
            }

            case PIG: {
                Pig pig = (Pig) this.entity;

                if (this.isSaddle()) {
                    pig.setSaddle(true);
                }
            }
        }
    }

    public void remove() {
        if (this.entity != null) {
            this.entity.remove();

            this.entity = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return id.equals(pet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public enum HorseArmor {
        NONE(Material.BARRIER),
        IRON(Material.IRON_BARDING),
        GOLD(Material.GOLD_BARDING),
        DIAMOND(Material.DIAMOND_BARDING);

        public final Material material;

        HorseArmor(Material material) {
            this.material = material;
        }
    }
}