package me.cristike.holoitem.events;

import me.cristike.holoitem.HoloManager;
import me.cristike.holoitem.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class HeldEvent implements Listener {
    private Plugin plugin = Main.getPlugin(Main.class);
    private HoloManager hm = new HoloManager();

    @EventHandler
    public void onHeld (PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        if (Main.toggled.contains(p.getUniqueId())) return;
        if (Main.disabled) return;
        if (Main.notMoving.contains(p.getUniqueId())) {
            int slot = e.getNewSlot();
            ItemStack item = p.getInventory().getItem(slot);
            if (Main.players.containsKey(p.getUniqueId())) return;
            if (item == null || item.getType() == Material.AIR) return;
            if (plugin.getConfig().getStringList("Blacklist").contains(item.getType().toString().toUpperCase())) return;
            int amount = item.getAmount();
            item.setAmount(1);
            hm.create(p, item);
            hm.Move(p, Main.players.get(p.getUniqueId()));
            p.getInventory().getItem(slot).setAmount(amount);
        }
    }
}
