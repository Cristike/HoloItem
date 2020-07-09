package me.cristike.hotw.events;

import me.cristike.hotw.HoloManager;
import me.cristike.hotw.Main;
import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

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
        if (Main.time.get(p.getUniqueId()) > 0) {
            Main.time.remove(p.getUniqueId());
            Main.time.put(p.getUniqueId(), 0);
        }
        if (Main.isAfk.contains(p.getUniqueId())) {
            Main.isAfk.remove(p.getUniqueId());
            if (Main.lastParticle.containsKey(p.getUniqueId())) {
                Particle finalParticle = Main.lastParticle.get(p.getUniqueId());
                Main.hasparticle.add(p.getUniqueId());
                if (Main.LastColor.containsKey(p.getUniqueId())) {
                    Color finalColor = Main.LastColor.get(p.getUniqueId());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Player player = p;
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
                            loc.getWorld().spawnParticle(finalParticle, loc.getX(), loc.getY(), loc.getZ(), 0, 0.001, 1, 0, 1, new Particle.DustOptions(finalColor, 1));
                        }
                    }.runTaskTimer(plugin, 0, 20);
                }
                else {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Player player = p;
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
