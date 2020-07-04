package me.cristike.holoitem.events;

import me.cristike.holoitem.HoloManager;
import me.cristike.holoitem.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class MoveEvent implements Listener {
    private Plugin plugin = Main.getPlugin(Main.class);
    private HoloManager hm = new HoloManager();

    private void remove(Player p) {
        if (Main.notMoving.contains(p.getUniqueId())) {
            Main.notMoving.remove(p.getUniqueId());
        }
    }

    @EventHandler
    public void onMove (PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (Main.toggled.contains(p.getUniqueId())) return;
        if (Main.disabled) return;
        if (e.getFrom().getX() != e.getTo().getX()) {
            remove(p);
            return;
        }
        if (e.getFrom().getY() != e.getTo().getY()) {
            remove(p);
            return;
        }
        if (e.getFrom().getZ() != e.getTo().getZ()) {
            remove(p);
            return;
        }
        if (Main.notMoving.contains(p.getUniqueId())) return;
        Main.notMoving.add(p.getUniqueId());
        if (p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getType() == Material.AIR) return;
        if (plugin.getConfig().getStringList("Blacklist").contains(p.getInventory().getItemInMainHand().getType().toString().toUpperCase())) return;
        ItemStack nitem = p.getInventory().getItemInMainHand();
        int amount = nitem.getAmount();
        nitem.setAmount(1);
        hm.create(p, p.getInventory().getItemInMainHand());
        hm.Move(p, Main.players.get(p.getUniqueId()));
        p.getInventory().getItemInMainHand().setAmount(amount);
    }
}
