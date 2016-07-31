package com.arrayprolc.gametech.treasurechests.main;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.arrayprolc.gametech.treasurechests.menu.Menu;

public class TreasureChestListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPlayedBefore()) {
            Player p = e.getPlayer();
            TreasureKey k = TreasureKey.getFromConfig("voter");
            int i = 1;
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
    }

    @EventHandler
    public void onInteract(EntityDamageByEntityEvent e) {
        if (e.getEntityType().equals(EntityType.ARMOR_STAND)) {
            if (e.getEntity() instanceof LivingEntity) {
                final ArmorStand as = (ArmorStand) e.getEntity();
                if (as.getCustomName() != null) {
                    final String s = as.getCustomName();
                    as.setCustomName(s);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TreasureChestCore.getInstance(), new Runnable() {
                        public void run() {
                            as.setCustomName(s);
                        }
                    }, 2);
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInteract2(PlayerInteractEvent e) {
        try {
            ItemStack i = e.getPlayer().getItemInHand();
            for (String s : TreasureChestCore.getInstance().getConfig().getString("loadedkeys").split(",")) {
                try {
                    TreasureKey k = TreasureKey.getFromConfig(s);

                    if (k.getItem().getItemMeta().getLore().equals(i.getItemMeta().getLore())) {
                        if (k.getItem().getItemMeta().getDisplayName().equals(i.getItemMeta().getDisplayName())) {
                            e.setCancelled(true);
                        }
                    }

                } catch (Exception ex) {

                }
            }
            try {
                for (TreasureChest c : TreasureChestCore.getInstance().chests) {
                    if (e.getClickedBlock().getLocation().equals(c.locFromString())) {
                        e.setCancelled(true);
                     //   long id = UtilUniqueKey.getIDFromStack(i);
                        if (c.isOpenableBy(e.getPlayer().getItemInHand())) {
//                            if (UtilUniqueKey.hasBeenUsed(i)) {
//                                e.getPlayer().setVelocity(new Vector(e.getPlayer().getLocation().getDirection().getX() * -3, 0.5, e.getPlayer().getLocation().getDirection().getZ() * -3));
//                                e.getPlayer().sendMessage("§4§lERROR: §7That key has already been used!");
//                                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.NOTE_BASS, 100, 0);
//                                for (ItemStack it : e.getPlayer().getInventory().getContents()) {
//                                    try {
//                                        if (UtilUniqueKey.getIDFromStack(i) == UtilUniqueKey.getIDFromStack(it)) {
//                                            e.getPlayer().getInventory().removeItem(it);
//                                        }
//                                    } catch (NullPointerException ex) {
//                                    }
//                                }
//                                e.setCancelled(true);
//                                i.setAmount(0);
//                                e.getPlayer().setItemInHand(i);
//                                e.getPlayer().updateInventory();
//                                return;
//                            }
                  //          UtilUniqueKey.useKey(i);
                   //         UtilUniqueKey.save();
                            if (!c.open(e.getPlayer())) {
                                return;
                            }
                                i.setAmount(i.getAmount() - 1);
                                e.getPlayer().setItemInHand(i);
                        } else {
//                            e.getPlayer().setVelocity(new Vector(e.getPlayer().getLocation().getDirection().getX() * -3, 0.5, e.getPlayer().getLocation().getDirection().getZ() * -3));
//                            e.getPlayer().sendMessage("§4§lERROR: §7You do not have the correct key for that!");
                            ResultList rs = c.getResults();  
                            Menu.showMenu(0, e.getPlayer(), rs);
                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.NOTE_BASS, 100, 0);
                        }
                        e.getPlayer().updateInventory();

                    }
                }
            } catch (Exception ex) {

            }
        } catch (Exception ex) {
        }
    }

}
