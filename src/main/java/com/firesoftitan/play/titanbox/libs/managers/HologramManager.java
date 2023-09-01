package com.firesoftitan.play.titanbox.libs.managers;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.tools.LibsHologramTool;
import com.firesoftitan.play.titanbox.libs.tools.Tools;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class HologramManager {

    private final ArmorStand armorStand;
    private final JavaPlugin plugin;
    private boolean deleting = false;
    public HologramManager(JavaPlugin plugin, @NotNull Location location) {
        this.plugin = plugin;
        armorStand = Objects.requireNonNull(location.getWorld()).spawn(location, ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setCustomNameVisible(false);
        armorStand.setCollidable(false);
        armorStand.setMarker(true);
        armorStand.setFireTicks(1000000);
        armorStand.setGravity(false);
        addID();
    }



    public HologramManager(JavaPlugin plugin, ArmorStand armorStand) {
        this.plugin = plugin;
        this.armorStand = armorStand;
        addID();
    }
    private void addID() {
        String strLocation = Tools.getSerializeTool(TitanBoxLibs.instants).serializeLocation(this.armorStand.getLocation());
        String strPlugin = this.plugin.getName();
        Tools.getEntityTool(TitanBoxLibs.instants).setTag(armorStand, "tblHG", strPlugin, strLocation, System.currentTimeMillis() +"");
    }
    public long getTimeStamp()
    {
        List<String> tags = Tools.getNBTTool(TitanBoxLibs.instants).getListString(armorStand, "Tags");
        String strPlugin = plugin.getName();
        for(String tag: tags)
        {
            try{ return Long.parseLong( tag ) ; }catch(Exception e){}
        }
        return 0;
    }
    public List<String> getTags()
    {
        List<String> tags = Tools.getNBTTool(TitanBoxLibs.instants).getListString(armorStand, "Tags");
        return tags;
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
            List<Entity> nearbyEntities = armorStand.getNearbyEntities(1, 1, 1);
            List<ArmorStand> armorStands = new ArrayList<ArmorStand>();
            for(Entity entity: nearbyEntities)
            {
                if (entity.getType() == EntityType.ARMOR_STAND)
                {
                    ArmorStand armor = (ArmorStand) entity;
                    List<String> tags = Tools.getNBTTool(TitanBoxLibs.instants).getListString(entity, "Tags");
                    String strPlugin = plugin.getName();
                    if (tags.contains("tblHG"))
                    {
                        if (tags.contains(strPlugin)) armorStands.add(armor);
                    }
                }
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    armorStand.remove();
                    for(int i = 0; i < armorStands.size(); i++)
                    {
                        ArmorStand armorStand = armorStands.get(i);
                        armorStand.remove();
                    }
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
    public ArmorStand getArmorStand()
    {
        return armorStand;
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
