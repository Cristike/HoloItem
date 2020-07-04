package me.cristike.holoitem;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class HoloManager {
    private Plugin plugin = Main.getPlugin(Main.class);

    public void create (Player p, ItemStack i) {
        i.setAmount(1);
        double pitch = ((12 + 90) * Math.PI) / 180;
        double yaw = ((p.getLocation().getYaw() + 90) * Math.PI) / 180;
        Vector vector = p.getLocation().getDirection();
        vector.setX(Math.sin(pitch) * Math.cos(yaw));
        vector.setY(Math.cos(pitch));
        vector.setZ(Math.sin(pitch) * Math.sin(yaw));
        Location loc = p.getLocation().add(vector);
        loc.setY(loc.getY()-1);
        ArmorStand as = loc.getWorld().spawn(loc, ArmorStand.class);
        as.setVisible(false);
        as.setArms(false);
        as.setGravity(false);
        as.setInvulnerable(true);
        Item item = loc.getWorld().dropItem(loc, i);
        item.setGravity(false);
        item.setPickupDelay(Integer.MAX_VALUE);
        as.addPassenger(item);
        Main.players.put(p.getUniqueId(), (Entity) as);
    }

    private void remove(Player player, Item item, Entity entity) {
        Main.players.remove(player.getUniqueId());
        item.leaveVehicle();
        item.remove();
        entity.remove();
    }

    public void Move (Player p, Entity e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = p;
                Entity entity = e;
                Item item = (Item) entity.getPassengers().get(0);
                if (Main.disabled) {
                    remove(player, item, entity);
                    cancel();
                    return;
                }
                if (((ArmorStand) entity).isVisible()) {
                    ((ArmorStand) entity).setVisible(false);
                }
                if (!Main.notMoving.contains(player.getUniqueId()) || Main.toggled.contains(player.getUniqueId())) {
                    remove(player, item, entity);
                    cancel();
                    return;
                }
                if (player.getInventory().getItemInMainHand().getType() != item.getItemStack().getType()) {
                    if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                        remove(player, item, entity);
                        cancel();
                        return;
                    }
                    if (plugin.getConfig().getStringList("Blacklist").contains(player.getInventory().getItemInMainHand().getType().toString().toUpperCase())) {
                        remove(player, item, entity);
                        cancel();
                        return;
                    }
                    ItemStack nitem = player.getInventory().getItemInMainHand();
                    int amount = nitem.getAmount();
                    nitem.setAmount(1);
                    item.setItemStack(nitem);
                    player.getInventory().getItemInMainHand().setAmount(amount);
                }
                double pitch = ((12 + 90) * Math.PI) / 180;
                double yaw = ((player.getLocation().getYaw() + 90) * Math.PI) / 180;
                Vector vector = player.getLocation().getDirection();
                vector.setX(Math.sin(pitch) * Math.cos(yaw));
                vector.setY(Math.cos(pitch));
                vector.setZ(Math.sin(pitch) * Math.sin(yaw));
                Location loc = player.getLocation().add(vector);
                loc.setY(loc.getY()-1);
                entity.eject();
                entity.teleport(loc);
                entity.addPassenger(item);
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
