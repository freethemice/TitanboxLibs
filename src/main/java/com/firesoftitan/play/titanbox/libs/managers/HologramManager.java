package com.firesoftitan.play.titanbox.libs.managers;

import com.firesoftitan.play.titanbox.libs.tools.LibsHologramTool;
import com.firesoftitan.play.titanbox.libs.tools.Tools;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.UUID;

public class HologramManager {
    private ArmorStand armorStand;
    private JavaPlugin plugin;
    private boolean deleting = false;
    public HologramManager(JavaPlugin plugin, Location location) {
        this.plugin = plugin;
        armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setCustomNameVisible(false);
        armorStand.setCollidable(false);
        armorStand.setMarker(true);
        armorStand.setGravity(false);
    }
    public HologramManager(JavaPlugin plugin, ArmorStand armorStand) {
        this.plugin = plugin;
        this.armorStand = armorStand;
    }
    public JavaPlugin getPlugin() {
        return plugin;
    }
    public void delete()
    {
        if (!deleting) {
            deleting = true;
            LibsHologramTool hologramTool = Tools.getHologramTool(plugin);
            if (hologramTool.getHologram(this.getUUID()) != null) hologramTool.removeHologram(this);
            new BukkitRunnable() {
                @Override
                public void run() {
                    armorStand.remove();
                }
            }.runTaskLater(this.getPlugin(), 1);
        }
    }
    public void setRotate(EulerAngle angle)
    {
        armorStand.setHeadPose(angle);
    }
    public void setLocation(Location location)
    {
        armorStand.teleport(location.clone());
    }
    public Location getLocation()
    {
        return armorStand.getLocation().clone();
    }
    public UUID getUUID()
    {
        return armorStand.getUniqueId();
    }
    public void setText(String text)
    {
        armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', text));
        armorStand.setCustomNameVisible(true);
    }
    public void setEquipment(EquipmentSlot equipmentSlot, ItemStack itemStack)
    {
        EntityEquipment equipment = armorStand.getEquipment();
        equipment.setItem(equipmentSlot, itemStack.clone());
    }
    public String getText()
    {
        return armorStand.getCustomName();
    }


}
