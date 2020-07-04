package me.cristike.holoitem;

import me.cristike.holoitem.commands.disable;
import me.cristike.holoitem.commands.toggle;
import me.cristike.holoitem.events.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin {

    public static HashMap<UUID, Entity> players = new HashMap<>();
    public static HashMap<UUID, Location> previous = new HashMap<>();
    public static ArrayList<UUID> notMoving = new ArrayList<>();
    public static ArrayList<UUID> toggled = new ArrayList<>();
    public static boolean disabled = false;

    @Override
    public void onEnable() {
        loadCommands();
        loadEvents();
        saveDefaultConfig();
        HoloManager hm = new HoloManager();
        Bukkit.getServer().getConsoleSender().sendMessage(c("&7[&bHoloItem&7] &fThe plugin has been &aenabled"));
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p :Bukkit.getServer().getOnlinePlayers()) {
                    Location previous = Main.previous.get(p.getUniqueId());
                    Location current = p.getLocation();
                    if (current == previous) {
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
                    Main.previous.remove(p.getUniqueId());
                    Main.previous.put(p.getUniqueId(), current);
                }
            }
        }.runTaskTimer(this, 0, 2);
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getConsoleSender().sendMessage(c("&7[&bHoloItem&7] &fThe plugin has been &cdisabled"));
    }

    private void loadEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new HeldEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PickupEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new MoveEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new QuitEvent(), this);
    }

    private void loadCommands() {
        this.getCommand("hitoggle").setExecutor(new toggle());
        this.getCommand("hidisable").setExecutor(new disable());
    }

    public static String c(String mess) {
        return ChatColor.translateAlternateColorCodes('&', mess);
    }
}
