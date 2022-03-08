package com.firesoftitan.play.titanbox.libs.tools;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.managers.HologramManager;
import com.firesoftitan.play.titanbox.libs.managers.SaveManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LibsHologramTool {
    private Tools parent;
    private static final SaveManager holoConfig = new SaveManager(  "holograms");
    public static void save()
    {
        holoConfig.save();
    }
    private List<HologramManager> hologramList;
    public LibsHologramTool(Tools parent) {
        this.parent = parent;
        this.hologramList = new ArrayList<HologramManager>();
        /*new BukkitRunnable() {
            @Override
            public void run() {
                List<UUID> inputList = holoConfig.getUUIDList("holograms." + parent.getPlugin().getName() + ".list");
                for (UUID uuid: inputList)
                {
                    ArmorStand entity = (ArmorStand) Bukkit.getEntity(uuid);
                    if (entity != null && entity.getType() == EntityType.ARMOR_STAND) hologramList.add(new HologramManager(parent.getPlugin(), entity));
                }
            }
        }.runTaskLater(TitanBoxLibs.instants, 1);*/

    }
    public List<HologramManager> getHologramList()
    {
        return hologramList;
    }
    public void removeHologram(Location location)
    {
        HologramManager hologramManager = getHologram(location);
        if (hologramManager != null) removeHologram(hologramManager);
    }
    public void removeHologram(HologramManager hologramManager)
    {
        hologramList.remove(hologramManager);
        updateList();
        hologramManager.delete();
    }
    public HologramManager addHologram(Location location)
    {
        HologramManager e = new HologramManager(this.parent.getPlugin(), location);
        hologramList.add(e);
        updateList();
        return e;
    }

    private void updateList() {
        List<UUID> list = new ArrayList<UUID>();
        for (HologramManager manager: hologramList)
        {
            list.add(manager.getUUID());
        }
        holoConfig.set("holograms." + parent.getPlugin().getName() + ".list", list);
    }

    public HologramManager getHologram(UUID uuid)
    {
        for(HologramManager hologramManager: hologramList)
        {
            if (hologramManager.getUUID().equals(uuid)) return hologramManager;
        }
        ArmorStand entity = (ArmorStand) Bukkit.getEntity(uuid);
        if (entity != null && entity.getType() == EntityType.ARMOR_STAND) {
            HologramManager e = new HologramManager(this.parent.getPlugin(), entity);
            hologramList.add(e);
            updateList();
            return e;
        }
        return null;
    }
    public HologramManager getHologram(Location location)
    {
        HologramManager closes = null;
        World worldA = location.getWorld();
        for(HologramManager hologramManager: hologramList)
        {
            World worldB = hologramManager.getLocation().getWorld();
            if (worldB.getName().equals(worldA.getName())) {
                if (closes == null) {
                    closes = hologramManager;
                } else {
                    double distanceA = closes.getLocation().distance(location);
                    double distanceB = hologramManager.getLocation().distance(location);
                    if (distanceB < distanceA) closes = hologramManager;
                }
            }
        }
        return closes;
    }
    @Deprecated
    public  void deleteOldFloatingText(Location location)
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

}
