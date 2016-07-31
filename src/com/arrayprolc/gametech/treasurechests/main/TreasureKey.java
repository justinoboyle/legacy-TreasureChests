package com.arrayprolc.gametech.treasurechests.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.arrayprolc.treasurechests.trails.item.ItemTools;
import com.arrayprolc.treasurechests.util.key.unique.UtilUniqueKey;

public class TreasureKey {

    String displayName;
    String name;
    String lore;
    String openableChests;
    boolean enchanted;

    public TreasureKey(String name, String displayName, String lore, String openableChests, boolean enchanted) {
        super();
        this.name = name;
        this.displayName = displayName;
        this.lore = lore;
        this.openableChests = openableChests;
        this.enchanted = enchanted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLore() {
        return lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public String getOpenableChests() {
        return openableChests;
    }

    public void setOpenableChests(String openableChests) {
        this.openableChests = openableChests;
    }

    public boolean isEnchanted() {
        return enchanted;
    }

    public void setEnchanted(boolean enchanted) {
        this.enchanted = enchanted;
    }

    public ItemStack getItem() {
        ItemStack i = ItemTools.setName(new ItemStack(Material.TRIPWIRE_HOOK), ChatColor.translateAlternateColorCodes('&', displayName.replace("_", " ")),
                new String[] { ChatColor.translateAlternateColorCodes('&', lore.replace("_", " ")), /*
                                                                                                     * ChatColor
                                                                                                     * .
                                                                                                     * translateAlternateColorCodes
                                                                                                     * (
                                                                                                     * '&'
                                                                                                     * ,
                                                                                                     * "&7&oID: "
                                                                                                     * +
                                                                                                     * UtilUniqueKey
                                                                                                     * .
                                                                                                     * generateNextID
                                                                                                     * (
                                                                                                     * )
                                                                                                     * )
                                                                                                     */});
        // i = UtilUniqueKey.setIDToStack(i, UtilUniqueKey.generateNextID());
        return i;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (enchanted ? 1231 : 1237);
        result = prime * result + ((lore == null) ? 0 : lore.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((openableChests == null) ? 0 : openableChests.hashCode());
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
        TreasureKey other = (TreasureKey) obj;
        if (enchanted != other.enchanted)
            return false;
        if (lore == null) {
            if (other.lore != null)
                return false;
        } else if (!lore.equals(other.lore))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (openableChests == null) {
            if (other.openableChests != null)
                return false;
        } else if (!openableChests.equals(other.openableChests))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TreasureKey [name=" + name + ", lore=" + lore + ", openableChests=" + openableChests + ", enchanted=" + enchanted + "]";
    }

    public void save() {

        TreasureChestCore.getInstance().getConfig().set("TreasureKeys." + name + ".displayName", displayName);

        TreasureChestCore.getInstance().getConfig().set("TreasureKeys." + name + ".lore", lore);

        TreasureChestCore.getInstance().getConfig().set("TreasureKeys." + name + ".openableChests", openableChests);

        TreasureChestCore.getInstance().getConfig().set("TreasureKeys." + name + ".enchanted", enchanted);

        String s = "";

        if (TreasureChestCore.getInstance().getConfig().getString("loadedkeys") != null) {
            s = TreasureChestCore.getInstance().getConfig().getString("loadedkeys");
        }
        if (s.equals("")) {
            s = name;
        } else {
            if (!s.contains(name)) {
                s = s + "," + name;
            }
        }
        TreasureChestCore.getInstance().getConfig().set("loadedkeys", s);
        TreasureChestCore.getInstance().saveConfig();
    }

    public static TreasureKey getFromConfig(String name) {

        String lore = TreasureChestCore.getInstance().getConfig().getString("TreasureKeys." + name + ".lore");

        String openableChests = TreasureChestCore.getInstance().getConfig().getString("TreasureKeys." + name + ".openableChests");

        String displayName = TreasureChestCore.getInstance().getConfig().getString("TreasureKeys." + name + ".displayName");

        boolean enchanted = TreasureChestCore.getInstance().getConfig().getBoolean("TreasureKeys." + name + ".enchanted");
        enchanted = true;

        return new TreasureKey(name, displayName, lore, openableChests, enchanted);

    }

}
