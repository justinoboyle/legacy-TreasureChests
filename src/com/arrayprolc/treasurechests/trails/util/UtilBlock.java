package com.arrayprolc.treasurechests.trails.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class UtilBlock {

    @SuppressWarnings("deprecation")
    public static void sendGhostBlock(Player p, Location loc, Material type, byte data) {
        if (loc.getWorld().getName().equals(p.getWorld().getName())) {
            p.sendBlockChange(loc, type, data);
        }
    }

    public static void sendGhostBlock(Location loc, Material type, byte data) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendGhostBlock(p, loc, type, data);
        }
    }

}
