package com.arrayprolc.treasurechests.trails.effects;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

import com.arrayprolc.gametech.treasurechests.main.TreasureChestCore;
import com.arrayprolc.treasurechests.trails.extra.ExtraManager;
import com.arrayprolc.treasurechests.trails.util.ParticleManager;

public class EffectManager implements Listener {
    public static HashMap<Player, EffectType> effect3 = new HashMap();
    public static HashMap<Location, EffectType> effect4 = new HashMap();

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (hasEffect(p))
            removeEffect(p, false);
    }

    @EventHandler
    public void onPlayerLeave(PlayerKickEvent e) {
        Player p = e.getPlayer();

        if (hasEffect(p))
            removeEffect(p, false);
    }

    public static boolean hasEffect(Player p) {
        if (effect3.containsKey(p)) {
            return true;
        }
        return false;
    }

    public static void addEffect(Player p, EffectType type) {
        if (!effect3.containsKey(p)) {
            effect3.put(p, type);
        } else {
            removeEffect(p, false);
            effect3.put(p, type);
        }
    }

    public static EffectType getEffect(Player p) {
        return (EffectType) effect3.get(p);
    }

    public static void removeEffect(Player p, boolean message) {
        if (hasEffect(p)) {
            if (ParticleManager.hasCircleEffect(p)) {
                ParticleManager.removeCircleEffect(p);
            }
            effect3.remove(p);
            if (ExtraManager.hasExtraEffect(p)) {
                ExtraManager.removeExtraEffect(p);
            }

        } else if (message) {
            p.sendMessage("Particle Aready Removed");
        }
    }

    public static boolean hasEffect(Location p) {
        if (effect4.containsKey(p)) {
            return true;
        }
        return false;
    }

    public static void addEffectLocation(Location p, EffectType type) {
        if (!effect4.containsKey(p)) {
            effect4.put(p, type);
        } else {
            removeEffect(p, false);
            effect4.put(p, type);
        }
    }

    public static EffectType getEffect(Location p) {
        return (EffectType) effect4.get(p);
    }

    public static void removeEffect(Location p, boolean message) {
        if (hasEffect(p)) {
            effect4.remove(p);
        }
    }

    public static void registerEvents(TreasureChestCore plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new EffectManager(), plugin);
    }

    public static enum EffectType {
        FlameRing, GreenSpiral, Cloud, CloudSnow, CloudStorm, Disco, Tornado, Vortex, FlameLilly, HourGlass, Shield, Fountain, GreenRing, EnderWave, Footstep;
    }

    public static EffectType fromString(String s) {
        for (EffectType e : EffectType.values()) {
            if (e.toString().equalsIgnoreCase(s)) {
                return e;
            }
        }
        for (EffectType e : EffectType.values()) {
            if (e.toString().toLowerCase().startsWith(s.toLowerCase())) {
                return e;
            }
        }
        return null;
    }
}