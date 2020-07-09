package me.cristike.hotw.commands;

import me.cristike.hotw.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class toggle implements CommandExecutor {
    Plugin plugin = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equals("hitoggle")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("holoitem.toggle")) {
                    if (Main.toggled.contains(p.getUniqueId())) {
                        Main.toggled.remove(p.getUniqueId());
                        p.sendMessage(Main.c(plugin.getConfig().getString("ToggleOn")));
                    }
                    else {
                        Main.toggled.add(p.getUniqueId());
                        p.sendMessage(Main.c(plugin.getConfig().getString("ToggleOff")));
                    }
                }
                else {
                    p.sendMessage(Main.c(plugin.getConfig().getString("NoPerm")));
                }
            }
        }
        return true;
    }
}
