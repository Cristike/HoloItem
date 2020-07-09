package me.cristike.hotw.events;

import me.cristike.hotw.HoloManager;
import me.cristike.hotw.Main;
import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class JoinEvent implements Listener {
    private Plugin plugin = Main.getPlugin(Main.class);
    private HoloManager hm = new HoloManager();

    @EventHandler
    public void onJoin (PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Main.previous.put(player.getUniqueId(), player.getLocation().getX() + player.getLocation().getY() + player.getLocation().getZ() + player.getLocation().getPitch() + player.getLocation().getYaw());
        Main.time.put(player.getUniqueId(), 0);
        if (Main.lastParticle.containsKey(player.getUniqueId())) {
            Particle finalParticle = Main.lastParticle.get(player.getUniqueId());
            Main.hasparticle.add(player.getUniqueId());
            if (Main.LastColor.containsKey(player.getUniqueId())) {
                Color finalColor = Main.LastColor.get(player.getUniqueId());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Player p = player;
                        if (!Main.hasparticle.contains(p.getUniqueId())) {
                            cancel();
                            return;
                        }
                        if (!DisguiseAPI.isDisguised(p)) {
                            Main.hasparticle.remove(p.getUniqueId());
                            cancel();
                            return;
                        }
                        Entity e = DisguiseAPI.getDisguise(p).getEntity();
                        double pitch = ((12 + 90) * Math.PI) / 180;
                        double yaw = ((e.getLocation().getYaw() + 90) * Math.PI) / 180;
                        Vector vector = e.getLocation().getDirection();
                        vector.setX(Math.sin(pitch) * Math.cos(yaw));
                        vector.setY(Math.cos(pitch));
                        vector.setZ(Math.sin(pitch) * Math.sin(yaw));
                        Location loc = e.getLocation().add(vector.multiply(-1));
                        loc.setY(e.getLocation().getY()+0.25);
                        player.spawnParticle(finalParticle, loc.getX(), loc.getY(), loc.getZ(), 0, 0.001, 1, 0, 1, new Particle.DustOptions(finalColor, 1));
                    }
                }.runTaskTimer(plugin, 0, 20);
            }
            else {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Player p = player;
                        if (!Main.hasparticle.contains(p.getUniqueId())) {
                            cancel();
                            return;
                        }
                        if (!DisguiseAPI.isDisguised(p)) {
                            Main.hasparticle.remove(p.getUniqueId());
                            cancel();
                            return;
                        }
                        else {
                            Entity e = DisguiseAPI.getDisguise(p).getEntity();
                            double pitch = ((12 + 90) * Math.PI) / 180;
                            double yaw = ((e.getLocation().getYaw() + 90) * Math.PI) / 180;
                            Vector vector = e.getLocation().getDirection();
                            vector.setX(Math.sin(pitch) * Math.cos(yaw));
                            vector.setY(Math.cos(pitch));
                            vector.setZ(Math.sin(pitch) * Math.sin(yaw));
                            Location loc = e.getLocation().add(vector.multiply(-1));
                            loc.setY(e.getLocation().getY()+0.25);
                            loc.getWorld().spawnParticle(finalParticle, loc, 0, 0, 0, 0, 1);
                        }
                    }
                }.runTaskTimer(plugin, 0, 20);
            }
        }
    }
}
