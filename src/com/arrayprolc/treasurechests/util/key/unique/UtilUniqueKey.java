package com.arrayprolc.treasurechests.util.key.unique;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import com.arrayprolc.gametech.treasurechests.main.TreasureChestCore;
import com.arrayprolc.treasurechests.trails.util.NMSUtils;

public class UtilUniqueKey {

  /*  private static long currentLong = -1;
    private static ArrayList<Long> used = new ArrayList<Long>();

    public static long generateNextID() {
        if (TreasureChestCore.getInstance().getConfig().getBoolean("use-ids")) {
            currentLong++;
            if (hasBeenUsed(currentLong)) {
                return generateNextID();
            }
            save();
            return currentLong;
        }
        return -999;
    }

    public static void useKey(long id) {
        if (id == -999) {
            return;
        }
        if (!used.contains(id)) {
            used.add(id);
        }
        save();
    }

    public static boolean hasBeenUsed(long id) {
        if (id == -999) {
            return false;
        }
        return used.contains(id);
    }

    public static void useKey(ItemStack i) {
        useKey(getIDFromStack(i));
    }

    public static boolean hasBeenUsed(ItemStack i) {
        return hasBeenUsed(getIDFromStack(i));
    }
    public static long getIDFromStack(ItemStack i) {

        try {
            long l = NMSUtils.getID(i);
            return l;
        } catch (Exception ex) {
            return -999;
        }
    }

    public static ItemStack setIDToStack(ItemStack i, long amount) {
        return NMSUtils.setID(i, amount);
    }

    @SuppressWarnings("deprecation")
    public static void save() {
        Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(TreasureChestCore.getInstance(), new Runnable() {
            public void run() {
                File file = new File("./plugins/" + "GTTreasureCrates" + "/already-used.lcdb");
                file.getParentFile().mkdirs();
                String builder = "";
                boolean first = true;
                for (long l : used) {
                    if (first) {
                        builder = l + "";
                        first = false;
                    } else {
                        builder = builder + "," + l;
                    }
                }

                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                try {
                    PrintWriter out = new PrintWriter(file.getAbsolutePath());
                    out.println(builder);
                    out.println("CURRENT: " + currentLong);
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, 1);
    }

    @SuppressWarnings("deprecation")
    public static void load() {
        // TODO fix title!
        Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(TreasureChestCore.getInstance(), new Runnable() {
            public void run() {
                // used.clear();
                File file = new File("./plugins/" + "GTTreasureCrates" + "/already-used.lcdb");
                file.getParentFile().mkdirs();
                String fileName = file.getAbsolutePath();
                String line = "";

                try {
                    FileReader fileReader = new FileReader(fileName);

                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    while ((line = bufferedReader.readLine()) != null) {
                        if (line.startsWith("CURRENT: ")) {
                            line = line.replace("CURRENT: ", "");
                            currentLong = Long.parseLong(line);
                        } else {
                            for (String s : line.split(",")) {
                                try {
                                    used.add(Long.parseLong(s));
                                } catch (NumberFormatException e) {
                                    // Ignore it.
                                }
                            }
                        }
                    }

                    bufferedReader.close();
                } catch (FileNotFoundException ex) {
                    System.out.println("Unable to open file '" + fileName + "'");
                } catch (IOException ex) {
                    System.out.println("Error reading file '" + fileName + "'");
                }
            }
        }, 1);
    }*/
}
