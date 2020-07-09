package me.cristike.hotw.commands;

import me.cristike.hotw.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class disable implements CommandExecutor {
    Plugin plugin = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("hidisable")) {
            if (sender instanceof Player) {
                if (((Player) sender).hasPermission("holoitem.admin")) {
                    Main.disabled = !Main.disabled;
                    if (Main.disabled) {
                        ((Player) sender).sendMessage(Main.c(plugin.getConfig().getString("Enabled")));
                    }
                    else ((Player) sender).sendMessage(Main.c(plugin.getConfig().getString("Disabled")));
                }
            }
            else {
                Main.disabled = !Main.disabled;
            }
        }
        return true;
    }
}
