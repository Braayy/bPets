package braayy.pets.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.Method;

public class NMSUtil {

    private static Method GET_NAVIGATION_METHOD;
    private static Method A_METHOD;

    private static Method GET_HANDLE_METHOD;

    static {
        try {
            //noinspection ConstantConditions
            GET_NAVIGATION_METHOD = getNMSClass("EntityInsentient").getDeclaredMethod("getNavigation");
            GET_NAVIGATION_METHOD.setAccessible(true);

            //noinspection ConstantConditions
            A_METHOD = getNMSClass("NavigationAbstract").getDeclaredMethod("a", double.class, double.class, double.class, double.class);
            A_METHOD.setAccessible(true);

            //noinspection ConstantConditions
            GET_HANDLE_METHOD = getOBCClass("entity.CraftLivingEntity").getDeclaredMethod("getHandle");
            GET_HANDLE_METHOD.setAccessible(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // BukkitEntity#getHandle() -> EntityInsentient#getNavegation() -> NavigationAbstract#a(double, double, double, double)

    public static Class<?> getNMSClass(String name) {
        try {
            String version = Bukkit.getServer().getClass().getName().split("\\.")[3];

            return Class.forName("net.minecraft.server." + version + '.' + name);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public static Class<?> getOBCClass(String name) {
        try {
            String version = Bukkit.getServer().getClass().getName().split("\\.")[3];

            return Class.forName("org.bukkit.craftbukkit." + version + '.' + name);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public static Object getHandle(LivingEntity entity) {
        try {
            return GET_HANDLE_METHOD.invoke(entity);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public static Object getNavigation(Object handle) {
        try {
            return GET_NAVIGATION_METHOD.invoke(handle);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public static void move(Object navegation, Location location, float speed) {
        try {
            A_METHOD.invoke(navegation, location.getX(), location.getY(), location.getZ(), speed);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}