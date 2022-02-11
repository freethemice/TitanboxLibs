package src.com.firesoftitan.play.titanbox.libs.tools;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import src.com.firesoftitan.play.titanbox.libs.TitanBoxLibsPlugin;

import java.util.List;

public class LibsFloatingTextTool {
    public static void changeFloatingText(Location location, String text)
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
        }.runTaskLater(TitanBoxLibsPlugin.instants, 1);

    }
    public static void deleteFloatingText(Location location)
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
        }.runTaskLater(TitanBoxLibsPlugin.instants, 1);

    }
    public static void setFloatingText(Location location, String text)
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
        }.runTaskLater(TitanBoxLibsPlugin.instants, 1);

    }
}
