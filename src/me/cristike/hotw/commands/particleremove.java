package me.cristike.hotw.commands;

import me.cristike.hotw.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class particleremove implements CommandExecutor {
    private Plugin plugin = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("particleremove")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("hotwparticles.remove")) {
                    if (args.length == 0) {
                        player.sendMessage(Main.c(plugin.getConfig().getString("InsufficientArguments")));
                    }
                    else if (args.length == 1) {
                        Player p = Bukkit.getServer().getPlayer(args[0]);
                        if (p != null && p.isOnline()) {
                            if (Main.hasparticle.contains(p.getUniqueId())) {
                                if (Main.LastColor.containsKey(p.getUniqueId())) {
                                    Main.LastColor.remove(p.getUniqueId());
                                }
                                Main.lastParticle.remove(p.getUniqueId());
                                Main.hasparticle.remove(p.getUniqueId());
                                player.sendMessage(Main.c(plugin.getConfig().getString("ParticlesRemoved")));
                            }
                            else {
                                player.sendMessage(Main.c(plugin.getConfig().getString("NoParticles")));
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
