package com.arrayprolc.treasurechests.trails.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import com.arrayprolc.gametech.treasurechests.main.TreasureChestCore;
import com.arrayprolc.treasurechests.trails.effects.EffectManager;

public class ParticleManager implements Listener {

    public static boolean hasCircleEffect(Player p) {
        if (CircleParticle.effect2.containsKey(p)) {
            return true;
        }
        return false;
    }

    public static void removeCircleEffect(Player p) {
        EffectManager.effect3.remove(p);
        UtilLocation.locationEverySecond.remove(p);
        CircleParticle.effect2.remove(p);
    }

    public static void registerEvents() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new CircleParticle(), TreasureChestCore.getInstance());
    }

    public static enum ParticleType {
        Heart, Music, Flame, Water, Lava, Spark, Reddust, Enchantement, Witch, AngryVillager, Portal, Snow, Slime, Rainbow, SnowShovel, MagicCrit;
    }
}