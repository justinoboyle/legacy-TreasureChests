package com.arrayprolc.treasurechests.trails.extra.extraeffects;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.arrayprolc.treasurechests.trails.effects.EffectManager;
import com.arrayprolc.treasurechests.trails.particles18.ParticleLib18;
import com.arrayprolc.treasurechests.trails.particles18.ParticleLib18.ParticleType;
import com.arrayprolc.treasurechests.trails.update.UpdateType;
import com.arrayprolc.treasurechests.trails.update.event.UpdateEvent;

public class Footstep implements Listener {

    int i = 0;

    HashMap<UUID, Integer> notMoving = new HashMap<UUID, Integer>();

    @EventHandler
    public void LocationUpdater(UpdateEvent event) {
        if (event.getType() == UpdateType.TICK) {

            i++;
            if (i % 5 == 0) {
                i = 0;
                for (Player p : EffectManager.effect3.keySet()) {
                    if (EffectManager.getEffect(p) == EffectManager.EffectType.Footstep) {
                        if (p.isValid() && notMoving.containsKey(p.getUniqueId()) && notMoving.get(p.getUniqueId()) < 2) {
                            if (!(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR)) && (!p.getLocation().getBlock().getType().equals(Material.WATER))
                                    && (!p.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER))) {
                                new ParticleLib18(ParticleType.FOOTSTEP, 1, 3, 0).sendToLocation(p.getLocation().add(0.00D, 0.07D, 0.0D));
                            }
                        }
                    }
                    try {
                        int w = 0;
                        if (notMoving.containsKey(p.getUniqueId())) {
                            w = notMoving.get(p.getUniqueId());
                            w = notMoving.remove(p.getUniqueId());
                        }
                        w = w + 1;
                        notMoving.put(p.getUniqueId(), w);
                    } catch (Exception ex) {
                        notMoving.put(p.getUniqueId(), 1);
                    }
                }
            }
        }
    }

    @EventHandler
    public void move(PlayerMoveEvent e) {
        if ((Math.round(e.getFrom().getY()) != Math.round(e.getTo().getY())) || (Math.round(e.getFrom().getX()) != Math.round(e.getTo().getX()))
                || (Math.round(e.getFrom().getZ()) != Math.round(e.getTo().getZ()))) {
            if (notMoving.containsKey(e.getPlayer().getUniqueId()))
                notMoving.remove(e.getPlayer().getUniqueId());
            notMoving.put(e.getPlayer().getUniqueId(), 0);
        }

    }

}
