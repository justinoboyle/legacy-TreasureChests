package com.arrayprolc.treasurechests.trails.extra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import com.arrayprolc.gametech.treasurechests.main.TreasureChestCore;
import com.arrayprolc.treasurechests.trails.effects.EffectManager;
import com.arrayprolc.treasurechests.trails.extra.extraeffects.Cloud;
import com.arrayprolc.treasurechests.trails.extra.extraeffects.Disco;
import com.arrayprolc.treasurechests.trails.extra.extraeffects.EnderWave;
import com.arrayprolc.treasurechests.trails.extra.extraeffects.FlameLilly;
import com.arrayprolc.treasurechests.trails.extra.extraeffects.FlameRing;
import com.arrayprolc.treasurechests.trails.extra.extraeffects.Footstep;
import com.arrayprolc.treasurechests.trails.extra.extraeffects.Fountain;
import com.arrayprolc.treasurechests.trails.extra.extraeffects.GreenRing;
import com.arrayprolc.treasurechests.trails.extra.extraeffects.GreenSpiral;
import com.arrayprolc.treasurechests.trails.extra.extraeffects.HourGlass;
import com.arrayprolc.treasurechests.trails.extra.extraeffects.Shield;
import com.arrayprolc.treasurechests.trails.extra.extraeffects.Tornado;
import com.arrayprolc.treasurechests.trails.extra.extraeffects.Vortex;
import com.arrayprolc.treasurechests.trails.update.UpdateType;
import com.arrayprolc.treasurechests.trails.update.event.UpdateEvent;
import com.arrayprolc.treasurechests.trails.util.ParticleManager;
import com.arrayprolc.treasurechests.trails.util.UtilLocation;

public class ExtraManager implements Listener {
    public static List<String> Moving = new ArrayList();
    final HashMap<UUID, Location> lastBlockLocation = new HashMap();

    @EventHandler
    public void ParticleAura(UpdateEvent event) {
        if (event.getType() == UpdateType.FASTEST) {
            for (Player player : EffectManager.effect3.keySet()) {
                Location Current = player.getLocation();
                Location Last = (Location) this.lastBlockLocation.get(player.getUniqueId());
                if (this.lastBlockLocation.get(player.getUniqueId()) == null) {
                    this.lastBlockLocation.put(player.getUniqueId(), Current);
                    Last = (Location) this.lastBlockLocation.get(player.getUniqueId());
                }
                this.lastBlockLocation.put(player.getUniqueId(), player.getLocation());
                if ((Last.getX() != Current.getX()) || (Last.getY() != Current.getY()) || (Last.getZ() != Current.getZ())) {
                    if (!Moving.contains(player.getName())) {
                        Moving.add(player.getName());
                    }
                } else if (Moving.contains(player.getName()))
                    Moving.remove(player.getName());
            }
        }
    }

    public static boolean isMoving(Player p) {
        if (Moving.contains(p.getName())) {
            return false;
        }
        return true;
    }

    public static boolean hasExtraEffect(Player p) {
        if ((EffectManager.getEffect(p) == EffectManager.EffectType.Cloud) || (EffectManager.getEffect(p) == EffectManager.EffectType.CloudSnow)
                || (EffectManager.getEffect(p) == EffectManager.EffectType.CloudStorm) || (EffectManager.getEffect(p) == EffectManager.EffectType.Disco)
                || (EffectManager.getEffect(p) == EffectManager.EffectType.Vortex) || (EffectManager.getEffect(p) == EffectManager.EffectType.FlameLilly)
                || (EffectManager.getEffect(p) == EffectManager.EffectType.HourGlass) || (EffectManager.getEffect(p) == EffectManager.EffectType.Shield)
                || (EffectManager.getEffect(p) == EffectManager.EffectType.Fountain) || (EffectManager.getEffect(p) == EffectManager.EffectType.GreenRing)
                || (EffectManager.getEffect(p) == EffectManager.EffectType.FlameRing) || (EffectManager.getEffect(p) == EffectManager.EffectType.GreenSpiral)
                || (EffectManager.getEffect(p) == EffectManager.EffectType.Footstep) || (EffectManager.getEffect(p) == EffectManager.EffectType.EnderWave)
                || (EffectManager.getEffect(p) == EffectManager.EffectType.Tornado)) {
            return true;
        }

        return false;
    }

    public static void removeExtraEffect(Player p) {
        EffectManager.effect3.remove(p);
        UtilLocation.locationEvery2Second.remove(p);
    }

    public static void Activate(Player p, EffectManager.EffectType type) {
        if (!EffectManager.hasEffect(p)) {
            EffectManager.effect3.put(p, type);

            if (!hasExtraEffect(p)) {
                UtilLocation.locationEvery2Second.put(p, p.getLocation());
            } else {
                removeExtraEffect(p);
                EffectManager.effect3.put(p, type);
                UtilLocation.locationEverySecond.put(p, p.getLocation());
            }
        } else {
            if (hasExtraEffect(p)) {
                removeExtraEffect(p);
                EffectManager.effect3.put(p, type);
                UtilLocation.locationEverySecond.put(p, p.getLocation());
            }
            if (ParticleManager.hasCircleEffect(p))
                p.sendMessage("Too Many effects");
        }
    }

    public static void registerEvents(TreasureChestCore plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new Cloud(), plugin);
        pm.registerEvents(new Disco(), plugin);
        pm.registerEvents(new Vortex(), plugin);
        pm.registerEvents(new Tornado(), plugin);
        pm.registerEvents(new HourGlass(), plugin);
        pm.registerEvents(new Shield(), plugin);
        pm.registerEvents(new Fountain(), plugin);
        pm.registerEvents(new FlameLilly(), plugin);
        pm.registerEvents(new GreenRing(), plugin);
        pm.registerEvents(new FlameRing(), plugin);
        pm.registerEvents(new GreenSpiral(), plugin);
        pm.registerEvents(new ExtraManager(), plugin);
        pm.registerEvents(new Footstep(), plugin);
        pm.registerEvents(new EnderWave(), plugin);
    }
}