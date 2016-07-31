package com.arrayprolc.treasurechests.trails.util;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UtilString {

    public static void sendToSender(CommandSender sender, String s) {
        if (sender instanceof Player) {
            sender.sendMessage(s);
            return;
        }
        sender.sendMessage(ChatColor.stripColor(s));

    }

    public static String enumCaps(String s) {
        return WordUtils.capitalize(s.toLowerCase().replace("_", " "));
    }
}
