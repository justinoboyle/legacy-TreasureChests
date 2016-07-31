package com.arrayprolc.gametech.treasurechests.main;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if(e.getPlayer().hasPlayedBefore()){
            return;
        }
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
