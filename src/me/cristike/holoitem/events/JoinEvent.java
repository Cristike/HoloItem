package me.cristike.holoitem.events;

import me.cristike.holoitem.HoloManager;
import me.cristike.holoitem.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinEvent implements Listener {
    private Plugin plugin = Main.getPlugin(Main.class);
    private HoloManager hm = new HoloManager();

    @EventHandler
    public void onJoin (PlayerJoinEvent e) {
        Player p = e.getPlayer();
        final boolean[] initial = {true};
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = p;
                Location loc = null;
                if (!p.isOnline()) {
                    cancel();
                    return;
                }
                if (initial[0]) {
                    initial[0] = false;
                    loc = player.getLocation();
                    if (p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getType() == Material.AIR) return;
                    if (plugin.getConfig().getStringList("Blacklist").contains(p.getInventory().getItemInMainHand().getType().toString().toUpperCase())) return;
                    ItemStack nitem = player.getInventory().getItemInMainHand();
                    int amount = nitem.getAmount();
                    nitem.setAmount(1);
                    hm.create(player, player.getInventory().getItemInMainHand());
                    hm.Move(player, Main.players.get(player.getUniqueId()));
                    player.getInventory().getItemInMainHand().setAmount(amount);
                    Main.notMoving.add(player.getUniqueId());
                }
                if (player.getLocation() == loc) {
                    if (Main.players.containsKey(player.getUniqueId())) return;
                    if (p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getType() == Material.AIR) return;
                    if (plugin.getConfig().getStringList("Blacklist").contains(p.getInventory().getItemInMainHand().getType().toString().toUpperCase())) return;
                    ItemStack nitem = player.getInventory().getItemInMainHand();
                    int amount = nitem.getAmount();
                    nitem.setAmount(1);
                    hm.create(player, player.getInventory().getItemInMainHand());
                    hm.Move(player, Main.players.get(player.getUniqueId()));
                    player.getInventory().getItemInMainHand().setAmount(amount);
                    Main.notMoving.add(player.getUniqueId());
                }
                loc = p.getLocation();
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
