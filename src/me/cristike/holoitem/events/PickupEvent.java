package me.cristike.holoitem.events;

import me.cristike.holoitem.HoloManager;
import me.cristike.holoitem.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PickupEvent implements Listener {
    private Plugin plugin = Main.getPlugin(Main.class);
    private HoloManager hm = new HoloManager();

    @EventHandler
    public void onPickup (EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (Main.toggled.contains(p.getUniqueId())) return;
            if (Main.disabled) return;
            if (Main.notMoving.contains(p.getUniqueId())) {
                if (Main.players.containsKey(p.getUniqueId())) return;
                new BukkitRunnable() {
                    public void run() {
                        if (e.getItem().getItemStack().getType() != p.getInventory().getItemInMainHand().getType()) { cancel(); return; }
                        if (plugin.getConfig().getStringList("Blacklist").contains(p.getInventory().getItemInMainHand().getType().toString().toUpperCase())) { cancel(); return;}
                        ItemStack nitem = p.getInventory().getItemInMainHand();
                        int amount = nitem.getAmount();
                        nitem.setAmount(1);
                        hm.create(p, p.getInventory().getItemInMainHand());
                        hm.Move(p, Main.players.get(p.getUniqueId()));
                        p.getInventory().getItemInMainHand().setAmount(amount);
                        cancel();
                    }
                }.runTaskLater(plugin, 2);
            }
        }
    }
}
