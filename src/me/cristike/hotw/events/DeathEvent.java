package me.cristike.hotw.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent implements Listener {

    @EventHandler
    public void DeathEvent (PlayerDeathEvent e) {
        Player p = (Player) e.getEntity();
        Location l = p.getLocation();
        l.setY(l.getY()+1);
        Bukkit.getServer().getWorld(l.getWorld().getName()).spawnParticle(Particle.CRIT, l, 20);
    }
}
