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

public class JoinEvent implements Listener {
    private Plugin plugin = Main.getPlugin(Main.class);
    private HoloManager hm = new HoloManager();

    @EventHandler
    public void onJoin (PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Main.previous.put(p.getUniqueId(), p.getLocation());
    }
}
