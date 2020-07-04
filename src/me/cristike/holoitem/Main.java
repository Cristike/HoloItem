package me.cristike.holoitem;

import me.cristike.holoitem.commands.disable;
import me.cristike.holoitem.commands.toggle;
import me.cristike.holoitem.events.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin {

    public static HashMap<UUID, Entity> players = new HashMap<>();
    public static ArrayList<UUID> notMoving = new ArrayList<>();
    public static ArrayList<UUID> toggled = new ArrayList<>();
    public static boolean disabled = false;

    @Override
    public void onEnable() {
        loadCommands();
        loadEvents();
        saveDefaultConfig();
        Bukkit.getServer().getConsoleSender().sendMessage(c("&7[&bHoloItem&7] &fThe plugin has been &aenabled"));
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
