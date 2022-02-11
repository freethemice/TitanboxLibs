package com.firesoftitan.play.titanbox.libs.tools;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class LibsFloatingTextTool {
    private Tools parent;

    public LibsFloatingTextTool(Tools parent) {
        this.parent = parent;
    }

    public void changeFloatingText(Location location, String text)
    {
        new BukkitRunnable() {
            @Override
            public void run() {
                ArmorStand stand = location.getWorld().spawn(location, ArmorStand.class);
                stand.setVisible(false);
                List<Entity> nearbyEntities = stand.getNearbyEntities(1, 1, 1);
                stand.remove();
                for(int i = 0; i < nearbyEntities.size(); i++)
                {
                    if (nearbyEntities.get(i).getType() == EntityType.ARMOR_STAND)
                    {
                        ArmorStand standc = (ArmorStand) nearbyEntities.get(i);
                        standc.setCustomName(ChatColor.translateAlternateColorCodes('&', text));
                        return;
                    }
                }
                setFloatingText(location, text);
            }
        }.runTaskLater(this.parent.getPlugin(), 1);

    }
    public  void deleteFloatingText(Location location)
    {
        new BukkitRunnable() {
            @Override
            public void run() {
                ArmorStand stand = location.getWorld().spawn(location, ArmorStand.class);
                stand.setVisible(false);
                List<Entity> nearbyEntities = stand.getNearbyEntities(1, 1, 1);
                stand.remove();
                for(int i = 0; i < nearbyEntities.size(); i++)
                {
                    if (nearbyEntities.get(i).getType() == EntityType.ARMOR_STAND)
                    {
                        nearbyEntities.get(i).remove();
                    }
                }
            }
        }.runTaskLater(this.parent.getPlugin(), 1);

    }
    public  void setFloatingText(Location location, String text)
    {
        new BukkitRunnable() {
            @Override
            public void run() {
                ArmorStand stand = location.getWorld().spawn(location, ArmorStand.class);
                stand.setCustomName(ChatColor.translateAlternateColorCodes('&', text));
                stand.setCustomNameVisible(true);
                stand.setVisible(false);
                stand.setCollidable(false);
                stand.setMarker(true);
                stand.setGravity(false);
            }
        }.runTaskLater(this.parent.getPlugin(), 1);

    }
}
