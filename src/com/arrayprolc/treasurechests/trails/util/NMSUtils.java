package com.arrayprolc.treasurechests.trails.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NMSUtils {
    private static String version = getVersion();

    public static String getVersion() {
        if (version != null)
            return version;
        String name = Bukkit.getServer().getClass().getPackage().getName();
        String version = name.substring(name.lastIndexOf('.') + 1) + ".";
        return version;
    }

    public static Class<?> getNMSClass(String className) {
        String fullName = "net.minecraft.server." + getVersion() + className;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(fullName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static Class<?> getOBCClass(String className) {
        String fullName = "org.bukkit.craftbukkit." + getVersion() + className;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(fullName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static Object getHandle(Object obj) {
        try {
            return getMethod(obj.getClass(), "getHandle").invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getBlockHandle(Object obj) {      
        try {
            Class<?> c = getOBCClass("block.CraftBlock");
            Method m = c.getDeclaredMethod("getNMSBlock");
            m.setAccessible(true);
            return m.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Field getField(Class<?> clazz, String name) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        int modifiers = modifiersField.getInt(field);
        modifiers &= ~Modifier.FINAL;
        modifiersField.setInt(field, modifiers);
        return field;
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... args) {
        for (Method m : clazz.getMethods())
            if (m.getName().equals(name) && (args.length == 0 || ClassListEqual(args, m.getParameterTypes()))) {
                m.setAccessible(true);
                return m;
            }
        for (Method m : clazz.getDeclaredMethods())
            if (m.getName().equals(name) && (args.length == 0 || ClassListEqual(args, m.getParameterTypes()))) {
                m.setAccessible(true);
                return m;
            }
        return null;
    }

    public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
        boolean equal = true;
        if (l1.length != l2.length)
            return false;
        for (int i = 0; i < l1.length; i++)
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        return equal;
    }

    public static ItemStack setData(ItemStack item, String key, String data) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NullPointerException {
        net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = itemStack.getTag();
        tag.setString(key, data);
        itemStack.setTag(tag);
       return CraftItemStack.asBukkitCopy(itemStack);
    }

    public static String getData(ItemStack item, String key) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NullPointerException {
        net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = itemStack.getTag();
        return tag.getString(key);
    }

    public static ItemStack setID(ItemStack item, long data) {
        try {
           return setData(item, "voterKeyID", data + "");
        } catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException | NullPointerException e) {
            e.printStackTrace();
            return item;
        }
    }

    public static long getID(ItemStack item) {
        try {
            return Long.parseLong(getData(item, "voterKeyID"));
        } catch (Exception e) {
            // TODO Finish this
            e.printStackTrace();
            return -1;
        }
    }
}
