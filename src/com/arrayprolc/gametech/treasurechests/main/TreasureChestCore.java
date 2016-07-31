package com.arrayprolc.gametech.treasurechests.main;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.arrayprolc.gametech.treasurechests.menu.Menu;
import com.arrayprolc.treasurechests.trails.effects.EffectManager;
import com.arrayprolc.treasurechests.trails.extra.ExtraManager;
import com.arrayprolc.treasurechests.trails.update.Updater;
import com.arrayprolc.treasurechests.trails.util.CircleParticle;
import com.arrayprolc.treasurechests.trails.util.ParticleManager;

public class TreasureChestCore extends JavaPlugin {

    private static TreasureChestCore instance;
    ArrayList<TreasureChest> chests = new ArrayList<TreasureChest>();

    @Override
    public void onEnable() {

        instance = this;

       // UtilUniqueKey.load();

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Updater(this), 1L, 1L);

        ParticleManager.registerEvents();
        ExtraManager.registerEvents(this);
        EffectManager.registerEvents(this);
        getServer().getPluginManager().registerEvents(new CircleParticle(), this);

        getConfig();
        
        getServer().getPluginManager().registerEvents(new Menu(), this);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                resetupChests();
                for (World w : Bukkit.getWorlds()) {
                    for (Entity e : w.getEntities()) {
                        for (TreasureChest c : chests) {
                            if (e instanceof ArmorStand) {
                                ArmorStand a = (ArmorStand) e;
                                try {
                                    String name = a.getCustomName();
                                    for (Entity e2 : a.getNearbyEntities(5, 5, 5)) {
                                        if (e2 instanceof ArmorStand) {
                                            ArmorStand a2 = (ArmorStand) e2;
                                            try {
                                                String name2 = a2.getCustomName();
                                                if (name2.equals(name)) {
                                                    a2.remove();
                                                }
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }

                                } catch (Exception ex) {
                                }
                            }
                        }
                    }
                }
            }
        }, 5);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                resetupChests();
            }
        }, 10);
        try {
            if (TreasureChestCore.getInstance().getConfig().getBoolean("use-ids") != false && TreasureChestCore.getInstance().getConfig().getBoolean("use-ids") != true) {
                TreasureChestCore.getInstance().getConfig().set("use-ids", true);
                TreasureChestCore.getInstance().saveConfig();
            }
        } catch (NullPointerException ex) {
            TreasureChestCore.getInstance().getConfig().set("use-ids", true);
            TreasureChestCore.getInstance().saveConfig();
        }
        Bukkit.getServer().getPluginManager().registerEvents(new TreasureChestListener(), this);
    }

    public void onDisable() {
        removeChests();
    }

    public void resetupChests() {

        removeChests();

        String s = "";

        try {
            if (TreasureChestCore.getInstance().getConfig().getString("loadedchests") != null) {
                s = TreasureChestCore.getInstance().getConfig().getString("loadedchests");
            }
        } catch (Exception ex) {

        }

        for (String s2 : s.split(",")) {

            String location = TreasureChestCore.getInstance().getConfig().getString("TreasureChests." + s2 + ".location");

            String particle = TreasureChestCore.getInstance().getConfig().getString("TreasureChests." + s2 + ".particle");

            String results = TreasureChestCore.getInstance().getConfig().getString("TreasureChests." + s2 + ".results");

            String name = TreasureChestCore.getInstance().getConfig().getString("TreasureChests." + s2 + ".name");

            boolean shouldShootFirework = TreasureChestCore.getInstance().getConfig().getBoolean("TreasureChests." + s2 + ".shouldShootFirework");

            String displayName = TreasureChestCore.getInstance().getConfig().getString("TreasureChests." + s2 + ".displayName");

            TreasureChest c = new TreasureChest(location, particle, results, name, shouldShootFirework, displayName);

            chests.add(c);
        }
    }

    public void removeChests() {
        for (TreasureChest c : chests) {
            c.aStand.remove();
        }
        chests.clear();
    }

    public static TreasureChestCore getInstance() {
        return instance;
    }

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("reloadchests")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                Bukkit.broadcastMessage(p.getItemInHand().getData().getData() + "");
                if (!p.isOp()) {
                    return false;
                }
            }
            this.reloadConfig();
            resetupChests();

        }

        if (label.equalsIgnoreCase("createresult")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage("This command must be ran by a player.");
                return true;
            }

            if (args.length != 5) {
                sender.sendMessage("/createresult isCommand command weight displayName isLegendary");
                sender.sendMessage("Display item is your item in hand. Use '_' for spaces inside arguments.");
                return true;
            }

            Player p = (Player) sender;

            if (!p.isOp()) {
                return false;
            }

            Result r = new Result(Boolean.parseBoolean(args[0]), p.getItemInHand(), args[1].replace("_", " "), Integer.parseInt(args[2]), ChatColor.translateAlternateColorCodes('&',
                    args[3].replace("_", " ")), Boolean.parseBoolean(args[4]));

            r.save();
            saveConfig();

            sender.sendMessage("Done setting up " + args[3]);
        }

        if (label.equalsIgnoreCase("createchest")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage("This command must be ran by a player.");
                return true;
            }

            if (args.length != 5) {
                sender.sendMessage("/createchest (eye/x,y,z) resultlist particle name displayName");
                sender.sendMessage("x,y,z = Location");
                return true;
            }

            Player p = (Player) sender;

            if (!p.isOp()) {
                return false;
            }
            HashSet<Material> mats = new HashSet<Material>();
            for(Material m : Material.values()) mats.add(m);
            Location loc = p.getTargetBlock(mats, 8).getLocation();

            if (!(args[0].equalsIgnoreCase("eye"))) {
                loc = new Location(p.getWorld(), Integer.parseInt(args[0].split(",")[0]), Integer.parseInt(args[0].split(",")[1]), Integer.parseInt(args[0].split(",")[2]));
            }

            TreasureChest c = new TreasureChest(loc, args[2], args[1], args[3], true, args[4]);
            chests.add(c);
            c.save();
            saveConfig();

            sender.sendMessage("Done setting up " + args[3]);
        }

        if (label.equalsIgnoreCase("createresultlist")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage("This command must be ran by a player.");
                return true;
            }

            if (args.length != 3) {
                sender.sendMessage("/createresultlist name create/add result,result");
                sender.sendMessage("Results can be created with /createresult or in the config.yml.");
                return true;
            }

            Player p = (Player) sender;

            if (!p.isOp()) {
                return false;
            }

            String mode = "ADD";
            if (args[1].equalsIgnoreCase("create")) {
                mode = "CREATE";
            }

            String name = args[0];
            String list = args[2];
            String s = "";

            if (mode.equals("ADD")) {
                s = getConfig().getString("ResultLists." + name);
                s = s + ",";
            }

            list = s + list;

            getConfig().set("ResultLists." + name, list);
            saveConfig();

            sender.sendMessage("Done setting up " + args[0]);
        }

        if (label.equalsIgnoreCase("createvoterkey")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage("This command must be ran by a player.");
                return true;
            }

            if (args.length != 4) {
                sender.sendMessage("/createvoterkey name displayname lore openablechests");
                sender.sendMessage("Use _ for spaces.");
                return true;
            }

            Player p = (Player) sender;

            if (!p.isOp()) {
                return false;
            }

            String name = args[0];
            String displayname = args[1];
            String lore = args[2];
            String openablechests = args[3];

            TreasureKey k = new TreasureKey(name, displayname, lore, openablechests, true);
            k.save();

            sender.sendMessage("Finished setting up " + args[0]);

        }

        if (label.equalsIgnoreCase("givereward")) {

            if (args.length != 2) {
                sender.sendMessage("/givereward player reward");
                return true;
            }

            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.isOp()) {
                    return false;
                }
            }

            Player p = Bukkit.getPlayer(args[0]);
            Result r = new Result(args[1]);

            r.giveReward(p);

        }

        if (label.equalsIgnoreCase("givekey")) {

            if (args.length != 3) {
                sender.sendMessage("/givekey name player amount");
                return true;
            }

            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.isOp()) {
                    return false;
                }
            }

            Player p = Bukkit.getPlayer(args[1]);
            TreasureKey k = TreasureKey.getFromConfig(args[0]);
            int i = Integer.parseInt(args[2]);
            int sendMessageTimes = 0;
            for (int x = 0; x < i; x++) {
                ItemStack ii = k.getItem().clone();
                if (p.getInventory().firstEmpty() == -1) {
                    p.getWorld().dropItem(p.getLocation(), ii);
                    sendMessageTimes++;
                } else {
                    p.getInventory().addItem(ii);
                }
            }
            if (sendMessageTimes > 0)
                p.sendMessage("§6§lCRATES: §7Your inventory could not fit the items, so they were placed on the ground. §c[x" + sendMessageTimes + "]");

        }

        if (label.equalsIgnoreCase("requirevalidation")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage("This command must be ran by a player.");
                return true;
            }

            if (args.length != 1) {
                sender.sendMessage("/requirevalidation (true/false)");
                return true;
            }

            if ((sender instanceof Player)) {
                if (!((Player) sender).isOp()) {
                    return false;
                }
            }

            boolean b = false;
            if (args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("yes")) {
                b = true;
            }

            getConfig().set("use-ids", b);
            saveConfig();

            sender.sendMessage("Requiring validation set to: " + b);

        }

        return false;
    }
}
