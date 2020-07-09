package me.cristike.hotw.commands;

import me.cristike.hotw.Main;
import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class particleset implements CommandExecutor {
    private Plugin plugin = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("particleset")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("hotwparticles.add")) {
                    if (args.length == 0) {
                        player.sendMessage(Main.c(plugin.getConfig().getString("InsufficientArguments")));
                    }
                    else if (args.length == 1) {
                        player.sendMessage(Main.c(plugin.getConfig().getString("InsufficientArguments")));
                    }
                    else if (args.length == 2) {
                        Player target = Bukkit.getServer().getPlayer(args[0]);
                        if (target != null && target.isOnline()) {
                            Particle particle = null;
                            try {
                                particle = Particle.valueOf(args[1].toUpperCase());
                            }
                            catch (IllegalArgumentException e) {
                                player.sendMessage(Main.c(plugin.getConfig().getString("InvalidType")));
                            }
                            if (particle != null) {
                                if (particle != Particle.REDSTONE && particle != Particle.SPELL_MOB && particle != Particle.SPELL_MOB_AMBIENT) {
                                    if (Main.hasparticle.contains(target.getUniqueId())) {
                                        player.sendMessage(Main.c(plugin.getConfig().getString("AlreadySetted")));
                                    }
                                    else {
                                        Main.hasparticle.add(target.getUniqueId());
                                        Particle finalParticle = particle;
                                        Main.lastParticle.put(target.getUniqueId(), finalParticle);
                                        player.sendMessage(Main.c(plugin.getConfig().getString("ParticleSetted")));
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
                                else {
                                    player.sendMessage(Main.c(plugin.getConfig().getString("SpecifyColor")));
                                }
                            }
                        }
                        else player.sendMessage(Main.c(plugin.getConfig().getString("InvalidPlayer")));
                    }
                    else if (args.length == 3) {
                        Player target = Bukkit.getServer().getPlayer(args[0]);
                        if (target != null && target.isOnline()) {
                            Particle particle = null;
                            try {
                                particle = Particle.valueOf(args[1].toUpperCase());
                            }
                            catch (IllegalArgumentException e) {
                                player.sendMessage(Main.c(plugin.getConfig().getString("InvalidType")));
                            }
                            if (particle != null) {
                                if (particle == Particle.REDSTONE || particle == Particle.SPELL_MOB || particle == Particle.SPELL_MOB_AMBIENT) {
                                    Color color = null;
                                    if (Main.getColors().containsKey(args[2].toUpperCase())) {
                                        color = Main.getColors().get(args[2].toUpperCase());
                                    }
                                    if (color != null) {
                                        if (Main.hasparticle.contains(target.getUniqueId())) {
                                            player.sendMessage(Main.c(plugin.getConfig().getString("AlreadySetted")));
                                        }
                                        else {
                                            Particle finalParticle = particle;
                                            Color finalColor = color;
                                            Main.lastParticle.put(target.getUniqueId(), finalParticle);
                                            Main.LastColor.put(target.getUniqueId(), finalColor);
                                            Main.hasparticle.add(target.getUniqueId());
                                            player.sendMessage(Main.c(plugin.getConfig().getString("ParticleSetted")));
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
                                    }
                                    else {
                                        player.sendMessage(Main.c(plugin.getConfig().getString("InvalidColor")));
                                    }
                                }
                                else {
                                    player.sendMessage(Main.c(plugin.getConfig().getString("CantPutColor")));
                                }
                            }
                        }
                        else player.sendMessage(Main.c(plugin.getConfig().getString("InvalidPlayer")));
                    }
                    else {
                        player.sendMessage(Main.c(plugin.getConfig().getString("UnknownCommand")));
                    }
                }
                else {
                    player.sendMessage(Main.c(plugin.getConfig().getString("NoPerm")));
                }
            }
            else {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "Only players can do this");
            }
        }
        return true;
    }
}
