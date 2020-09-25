package braayy.pets.util;

import braayy.pets.inventory.pet.*;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public class Util {

    public static String getFancyName(String str) {
        String spaced = str.replace('_', ' ').toLowerCase();

        return Character.toUpperCase(spaced.charAt(0)) + spaced.substring(1);
    }

    public static ChatColor dyeColorToChatColor(DyeColor color) {
        switch (color) {
            case ORANGE:
            case BROWN:
                return ChatColor.GOLD;
            case MAGENTA:
            case PINK:
                return ChatColor.LIGHT_PURPLE;
            case LIGHT_BLUE: return ChatColor.BLUE;
            case YELLOW: return ChatColor.YELLOW;
            case LIME: return ChatColor.GREEN;
            case GRAY:
            case SILVER:
                return ChatColor.GRAY;
            case CYAN: return ChatColor.DARK_AQUA;
            case PURPLE: return ChatColor.DARK_PURPLE;
            case BLUE: return ChatColor.DARK_BLUE;
            case GREEN: return ChatColor.DARK_GREEN;
            case RED: return ChatColor.RED;

            default: return ChatColor.WHITE;
        }
    }

    public static boolean hasColor(EntityType type) {
        if (type == null) return false;

        switch (type) {
            case WOLF:
            case SHEEP:
            case OCELOT:
            case RABBIT:
            case PARROT:
            case HORSE:
                return true;
            default:
                return false;
        }
    }

    public static UUID blobToUUID(String blob) {
        return UUID.fromString(blob.replaceAll("(.{8})(.{4})(.{4})(.{4})(.{12})", "$1-$2-$3-$4-$5"));
    }

}