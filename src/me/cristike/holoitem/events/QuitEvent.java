package me.cristike.holoitem.events;

import me.cristike.holoitem.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {

    @EventHandler
    public void onQuit (PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Main.previous.remove(p.getUniqueId());
        if (Main.notMoving.contains(p.getUniqueId())) {
            Main.notMoving.remove(p.getUniqueId());
        }
    }
}
