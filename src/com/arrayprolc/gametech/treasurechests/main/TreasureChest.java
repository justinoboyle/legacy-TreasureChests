package com.arrayprolc.gametech.treasurechests.main;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.arrayprolc.treasurechests.trails.effects.EffectManager;
import com.arrayprolc.treasurechests.trails.effects.EffectManager.EffectType;
import com.arrayprolc.treasurechests.trails.util.ItemTools;
import com.arrayprolc.treasurechests.trails.util.UtilFirework;
import com.arrayprolc.treasurechests.trails.util.UtilPacket;

public class TreasureChest implements ConfigurationSerializable, Listener {

    String location;
    String particle;
    String results;
    String name;
    String displayName;
    boolean shouldShootFirework;
    ArmorStand aStand;
    boolean open = false;

    public TreasureChest(Location location, String particle, String results, String name, boolean shouldShootFirework, String displayName) {
        super();
        this.location = location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ();
        this.particle = particle;
        this.results = results;
        this.name = name;
        this.displayName = displayName;
        this.shouldShootFirework = shouldShootFirework;

        createWitherSkull();

        EffectManager.addEffectLocation(getLocation().clone().add(0.5, 0, 0.5), getParticle());

        open = false;

    }

    public boolean isOpenableBy(ItemStack i) {
        for (String s : TreasureChestCore.getInstance().getConfig().getString("loadedkeys").split(",")) {
            try {
                TreasureKey k = TreasureKey.getFromConfig(s);

                if (k.getItem().getItemMeta().getLore().get(0).equals(i.getItemMeta().getLore().get(0))) {
                    if (k.getItem().getItemMeta().getDisplayName().equals(i.getItemMeta().getDisplayName())) {
                        if (k.openableChests.contains(this.getName())) {
                            return true;
                        }
                    }
                }

            } catch (Exception ex) {

            }
        }
        return false;
    }

    public TreasureChest(String location, String particle, String results, String name, boolean shouldShootFirework, String displayName) {
        super();
        this.location = location;
        this.particle = particle;
        this.results = results;
        this.name = name;
        this.displayName = displayName;
        this.shouldShootFirework = shouldShootFirework;
        createWitherSkull();

        EffectManager.addEffectLocation(getLocation().clone().add(0.5, 0, 0.5), getParticle());
    }

    public void chestStayOpen() {
        for (Player p : getLocation().getWorld().getPlayers()) {
            UtilPacket.sendPacketPlayOutOpenChest(p, getLocation(), 30);
            p.playSound(getLocation(), Sound.CHEST_OPEN, 1, 1);
        }
    }

    public void createWitherSkull() {
        if (aStand != null) {
            aStand.remove();
        }
        Location loc = locFromString();
        aStand = loc.getWorld().spawn(loc.clone().add(0.5, 0, 0.5), ArmorStand.class);
        aStand.setVisible(false);
        aStand.setGravity(false);
        aStand.setCanPickupItems(false);
        aStand.setCustomName(ChatColor.translateAlternateColorCodes('&', displayName.replace("_", " ")));
        aStand.setCustomNameVisible(true);
        aStand.setSmall(true);
    }

    private double getRandom() {
        if (new Random().nextBoolean() == false) {
            return 1D;
        }
        return -1.5D;
    }

    public TreasureChest getChest() {
        return this;
    }

    public boolean open(Player p) {
        if (open) {
            return false;
        }
        open = true;
        chestStayOpen();
        UtilFirework.shootFirework2(getLocation().clone().add(0.5, 2, 0.5));
        ResultList rs = this.getResults();
        final Result r = rs.pickResult();
        r.giveReward(p);
        
        for (int it = 0; it < 10; it++) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TreasureChestCore.getInstance(), new Runnable() {
                public void run() {

                    final Item i = getLocation().getWorld().dropItemNaturally(getLocation().clone().add(0.5, 1, 0.5), ItemTools.setName(r.getDisplay().clone(), new Random().nextDouble() + ""));

                    // UtilEntity.noAI(i);
                    i.setVelocity(new Vector((getRandom() * new Random().nextDouble()) / 7, 0.5, (getRandom() * new Random().nextDouble()) / 7));
                    i.setPickupDelay(99999);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TreasureChestCore.getInstance(), new Runnable() {
                        public void run() {
                            i.remove();
                        }
                    }, 20 * 3);

                    for (Player p : getLocation().getWorld().getPlayers()) {
                        p.playSound(getLocation(), Sound.ITEM_PICKUP, 1, 0);
                    }
                }
            }, 1 + (it * 5));
        }
        // i.setVelocity(new Vector(0, 0.5, 0));
        // Give a player the result
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TreasureChestCore.getInstance(), new Runnable() {
            public void run() {
                // chestStayOpen();
                final byte data = getLocation().getBlock().getData();
                getLocation().getBlock().setType(Material.TRAPPED_CHEST);
                getLocation().getBlock().setData(data);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TreasureChestCore.getInstance(), new Runnable() {
                    public void run() {
                        getLocation().getBlock().setType(Material.CHEST);
                        getLocation().getBlock().setData(data);

                    }
                }, 1);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TreasureChestCore.getInstance(), new Runnable() {
                    public void run() {
                        open = false;
                    }
                }, 10);
            }
        }, 20 * 3);
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((aStand == null) ? 0 : aStand.hashCode());
        result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((particle == null) ? 0 : particle.hashCode());
        result = prime * result + ((results == null) ? 0 : results.hashCode());
        result = prime * result + (shouldShootFirework ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TreasureChest other = (TreasureChest) obj;
        if (aStand == null) {
            if (other.aStand != null)
                return false;
        } else if (!aStand.equals(other.aStand))
            return false;
        if (displayName == null) {
            if (other.displayName != null)
                return false;
        } else if (!displayName.equals(other.displayName))
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (particle == null) {
            if (other.particle != null)
                return false;
        } else if (!particle.equals(other.particle))
            return false;
        if (results == null) {
            if (other.results != null)
                return false;
        } else if (!results.equals(other.results))
            return false;
        if (shouldShootFirework != other.shouldShootFirework)
            return false;
        return true;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public String toString() {
        return "TreasureChest [location=" + location + ", particle=" + particle + ", results=" + results + ", name=" + name + ", displayName=" + displayName + ", shouldShootFirework="
                + shouldShootFirework + ", aStand=" + aStand + "]";
    }

    public Location getLocation() {
        return locFromString().clone();
    }

    public Location locFromString() {
        return new Location(Bukkit.getWorld(location.split(",")[0]), Double.parseDouble(location.split(",")[1]), Double.parseDouble(location.split(",")[2]), Double.parseDouble(location.split(",")[3]));
    }

    private String locToString(Location loc) {
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
    }

    public void setLocation(Location location) {
        this.location = locToString(location);
    }

    public EffectType getParticle() {
        return EffectManager.fromString(particle);
    }

    public void setParticle(EffectType particle) {
        this.particle = particle.toString();
    }

    public void setParticle(String particle) {
        this.particle = particle;
    }

    public ResultList getResults() {
        return ResultList.fromString(results);
    }

    public void setResults(ResultList results) {
        this.results = results.getName();
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShouldShootFirework() {
        return shouldShootFirework;
    }

    public void setShouldShootFirework(boolean shouldShootFirework) {
        this.shouldShootFirework = shouldShootFirework;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("location", location);
        maps.put("particle", particle);
        maps.put("results", results);
        maps.put("name", name);
        maps.put("shouldShootFirework", shouldShootFirework);
        maps.put("displayName", displayName);
        return maps;
    }

    public void save() {
        try {
            TreasureChestCore.getInstance().getConfig().set("TreasureChests." + this.getName() + ".location", location);

            TreasureChestCore.getInstance().getConfig().set("TreasureChests." + this.getName() + ".particle", particle);

            TreasureChestCore.getInstance().getConfig().set("TreasureChests." + this.getName() + ".results", results);

            TreasureChestCore.getInstance().getConfig().set("TreasureChests." + this.getName() + ".name", name);

            TreasureChestCore.getInstance().getConfig().set("TreasureChests." + this.getName() + ".shouldShootFirework", shouldShootFirework);

            TreasureChestCore.getInstance().getConfig().set("TreasureChests." + this.getName() + ".displayName", displayName);

            String s = "";
            try {
                if (TreasureChestCore.getInstance().getConfig().getString("loadedchests") != null) {
                    s = TreasureChestCore.getInstance().getConfig().getString("loadedchests");
                }
            } catch (Exception ex) {

            }
            if (s.equals("")) {
                s = name;
            } else {
                if (!s.contains(name)) {
                    s = s + "," + name;
                }
            }
            TreasureChestCore.getInstance().getConfig().set("loadedchests", s);
        } catch (Exception ex) {
            for (StackTraceElement e : ex.getStackTrace()) {
                Bukkit.broadcastMessage(e.toString());
            }
        }
        TreasureChestCore.getInstance().saveConfig();
    }

}
