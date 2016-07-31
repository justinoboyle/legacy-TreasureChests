package com.arrayprolc.gametech.treasurechests.menu;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.arrayprolc.gametech.treasurechests.main.Result;
import com.arrayprolc.gametech.treasurechests.main.ResultList;
import com.arrayprolc.treasurechests.trails.util.UtilItem;

public class Menu implements Listener {

    private static HashMap<String, String> currentMenu = new HashMap<String, String>();

    public static void showMenu(int page, Player p, ResultList list) {
        if (currentMenu.containsKey(p.getUniqueId().toString()))
            currentMenu.remove(p.getUniqueId().toString());

        currentMenu.put(p.getUniqueId().toString(), list.getName());

        Inventory i = Bukkit.createInventory(null, 9 * 6, "Possible Rewards | " + (page + 1));
        int co = ((9 * 5) * page);
        int max = (9 * 5) - 1;
        for (Result r : list.getResults()) {
            if (co <= max) {
                try {
                    i.setItem(co, r.getDisplay());
                    if (r.getDisplay() != null)
                        co++;
                } catch (Exception ex) {
                }
            }
        }
        if (page == 0)
            i.setItem(48, UtilItem.setName(new ItemStack(Material.BARRIER), "§cClose.", "§7§oClose this window."));
        else
            i.setItem(48, UtilItem.setName(new ItemStack(Material.ARROW), "§a<§m--§a Back", "§7§oGo back a page."));
        i.setItem(50, UtilItem.setName(new ItemStack(Material.ARROW), " §aNext §m--§a>", "§7§oGo to the next page."));
        p.closeInventory();
        p.openInventory(i);

    }

    private static void debug(Object message) {
        // Bukkit.broadcastMessage("[Debug] " + message.toString());
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getInventory().getName().contains("Possible Rewards | "))
            return;
        e.setCancelled(true);
        try {
            ResultList currentList = ResultList.fromString(currentMenu.get(e.getWhoClicked().getUniqueId()));
            int page = Integer.parseInt(e.getInventory().getName().split("Possible Rewards | ")[1]);
            page = page - 1;
            switch (e.getSlot()) {
            case 48:
                if (page == 0)
                    e.getWhoClicked().closeInventory();
                else
                    showMenu(page - 1, (Player) e.getWhoClicked(), currentList);
                return;
            case 50:
                showMenu(page + 1, (Player) e.getWhoClicked(), currentList);
                return;
            default:
                return;
            }
        } catch (Exception ex) {
            e.getWhoClicked().sendMessage("§cUh oh. Something went wrong.");
        }

    }

    public static int[] getEmptySlots() {
        ArrayList<Integer> ints = new ArrayList<Integer>();
        for (int i = 0; i < 9; i++)
            ints.add(i);
        for (int i = 45; i <= 53; i++)
            ints.add(i);
        int i2 = 0;
        while (i2 <= 36) {
            i2 = i2 + 9;
            if (!ints.contains(i2)) {
                ints.add(i2);
            }
        }
        int i3 = 8;
        while (i3 <= 44) {
            i3 = i3 + 9;
            if (!ints.contains(i3)) {
                ints.add(i3);
            }
        }
        int[] i4 = new int[ints.size()];
        int i6 = 0;
        for (@SuppressWarnings("unused")
        int i5 : ints) {
            i4[i6] = ints.get(i6).intValue();
            i6++;
        }
        return i4;
    }

    public static int[] getAllowedSlots() {
        ArrayList<Integer> ints = new ArrayList<Integer>();
        for (int i = 0; i < 9 * 6; i++) {
            if (!isBlocked(i)) {
                ints.add(i);
            }
        }

        int[] i4 = new int[ints.size()];
        int i6 = 0;
        for (@SuppressWarnings("unused")
        int i5 : ints) {
            i4[i6] = ints.get(i6).intValue();
            i6++;
        }
        return i4;
    }

    public static boolean isBlocked(int i) {
        for (int i2 : getEmptySlots()) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

}
