package me.cristike.hotw;

import me.cristike.hotw.commands.disable;
import me.cristike.hotw.commands.particleremove;
import me.cristike.hotw.commands.particleset;
import me.cristike.hotw.commands.toggle;
import me.cristike.hotw.events.*;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin {

    public static HashMap<UUID, Entity> players = new HashMap<>();
    public static HashMap<UUID, Double> previous = new HashMap<>();
    public static HashMap<UUID, Particle> lastParticle = new HashMap<>();
    public static HashMap<UUID, Color> LastColor = new HashMap<>();
    public static HashMap<UUID, Integer> time = new HashMap<>();
    public static ArrayList<UUID> notMoving = new ArrayList<>();
    public static ArrayList<UUID> toggled = new ArrayList<>();
    public static ArrayList<UUID> hasparticle = new ArrayList<>();
    public static ArrayList<UUID> isAfk = new ArrayList<>();
    public static boolean disabled = false;

    public static HashMap<String, Color> getColors() {
        HashMap<String, Color> colors = new HashMap<>();
        colors.put("AQUA", Color.AQUA);
        colors.put("BLUE", Color.BLUE);
        colors.put("BLACK", Color.BLACK);
        colors.put("FUCHSIA", Color.FUCHSIA);
        colors.put("GRAY", Color.GRAY);
        colors.put("GREEN", Color.GREEN);
        colors.put("LIME", Color.LIME);
        colors.put("MAROON", Color.MAROON);
        colors.put("NAVY", Color.NAVY);
        colors.put("OLIVE", Color.OLIVE);
        colors.put("ORANGE", Color.ORANGE);
        colors.put("PURPLE", Color.PURPLE);
        colors.put("RED", Color.RED);
        colors.put("SILVER", Color.SILVER);
        colors.put("TEAL", Color.TEAL);
        colors.put("WHITE", Color.WHITE);
        colors.put("YELLOW", Color.YELLOW);
        return colors;
    }

    @Override
    public void onEnable() {
        loadCommands();
        loadEvents();
        saveDefaultConfig();
        HoloManager hm = new HoloManager();
        Bukkit.getServer().getConsoleSender().sendMessage(c("&7[&BHOTW&7] &fThe plugin has been &aenabled"));
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (p.isOnline()) {
                        double previous = Main.previous.get(p.getUniqueId());
                        double current = Main.previous.put(p.getUniqueId(), p.getLocation().getX() + p.getLocation().getY() + p.getLocation().getZ() + p.getLocation().getPitch() + p.getLocation().getYaw());
                        if (current == previous && !isAfk.contains(p.getUniqueId())) {
                            if (Main.players.containsKey(p.getUniqueId())) return;
                            if (p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getType() == Material.AIR) return;
                            if (getConfig().getStringList("Blacklist").contains(p.getInventory().getItemInMainHand().getType().toString().toUpperCase())) return;
                            ItemStack nitem = p.getInventory().getItemInMainHand();
                            int amount = nitem.getAmount();
                            nitem.setAmount(1);
                            hm.create(p, p.getInventory().getItemInMainHand());
                            hm.Move(p, Main.players.get(p.getUniqueId()));
                            p.getInventory().getItemInMainHand().setAmount(amount);
                            Main.notMoving.add(p.getUniqueId());
                        }
                        else {
                            return;
                        }
                        Main.previous.remove(p.getUniqueId());
                        Main.previous.put(p.getUniqueId(), current);
                    }
                }
            }
        }.runTaskTimer(this, 0, 20);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (p.isOnline()) {
                        double previous = Main.previous.get(p.getUniqueId());
                        double current = Main.previous.put(p.getUniqueId(), p.getLocation().getX() + p.getLocation().getY() + p.getLocation().getZ() + p.getLocation().getPitch() + p.getLocation().getYaw());
                        if (current == previous && !isAfk.contains(p.getUniqueId())) {
                            int nr = time.get(p.getUniqueId()) + 1;
                            if (nr == getConfig().getInt("afktime")) {
                                isAfk.add(p.getUniqueId());
                                if (notMoving.contains(p.getUniqueId())) {
                                    notMoving.remove(p.getUniqueId());
                                }
                                if (hasparticle.contains(p.getUniqueId())) {
                                    hasparticle.remove(p.getUniqueId());
                                }
                                return;
                            }
                            time.remove(p.getUniqueId());
                            time.put(p.getUniqueId(), nr);
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getConsoleSender().sendMessage(c("&7[&bHOTW&7] &fThe plugin has been &cdisabled"));
    }

    private void loadEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new HeldEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PickupEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new MoveEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new QuitEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new DeathEvent(), this);
    }

    private void loadCommands() {
        this.getCommand("hitoggle").setExecutor(new toggle());
        this.getCommand("hidisable").setExecutor(new disable());
        this.getCommand("particleset").setExecutor(new particleset());
        this.getCommand("particleremove").setExecutor(new particleremove());
    }

    public static String c(String mess) {
        return ChatColor.translateAlternateColorCodes('&', mess);
    }
}
