package com.firesoftitan.play.titanbox.libs.tools;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.managers.HologramManager;
import com.firesoftitan.play.titanbox.libs.managers.SaveManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class LibsHologramTool {
    private Tools parent;
    private static final SaveManager holoConfig = new SaveManager(  "holograms");
    public static void save()
    {
        holoConfig.save();
    }
    public LibsHologramTool(Tools parent) {
        this.parent = parent;
    }

    public void removeHologram(Location location)
    {
        HologramManager hologramManager = getHologram(location);
        if (hologramManager != null) removeHologram(hologramManager);
    }
    public void removeHologram(HologramManager hologramManager)
    {
        if (hologramManager != null) hologramManager.delete();
    }
    public HologramManager addHologram(Location location)
    {
        HologramManager e = new HologramManager(this.parent.getPlugin(), location);
        return e;
    }


    public List<HologramManager> getHolograms(Location location, int dX, int dY, int dZ)
    {
        List<HologramManager> hologramsAll = getHolograms();
        List<HologramManager> returnList = new ArrayList<HologramManager>();
        for (HologramManager hologramManager: hologramsAll)
        {
            ArmorStand entity = hologramManager.getArmorStand();
            Location entityLocation = entity.getLocation();
            if (entityLocation.getBlockX() >= location.getBlockX() - dX && entityLocation.getBlockX() <= location.getBlockX() + dX)
            {
                if (entityLocation.getBlockY() >= location.getBlockY() - dY && entityLocation.getBlockY() <= location.getBlockY() + dY)
                {
                    if (entityLocation.getBlockZ() >= location.getBlockZ() - dZ && entityLocation.getBlockZ() <= location.getBlockZ() + dZ)
                    {
                        returnList.add(hologramManager);
                    }
                }
            }
        }
        return returnList;
    }
    public List<HologramManager> getHolograms(Location location)
    {
        List<HologramManager> hologramsAll = getHolograms();
        List<HologramManager> returnList = new ArrayList<HologramManager>();
        for (HologramManager hologramManager: hologramsAll)
        {
            ArmorStand entity = hologramManager.getArmorStand();
            List<String> tags = TitanBoxLibs.tools.getNBTTool(TitanBoxLibs.instants).getListString(entity, "Tags");
            String serializeLocation = TitanBoxLibs.tools.getSerializeTool(TitanBoxLibs.instants).serializeLocation(location);
            if (tags.contains(serializeLocation))
            {
                returnList.add(hologramManager);
            }
        }
        if (returnList.size() == 0)
        {
            returnList.add(getHologram(location));
        }
        return returnList;
    }
    public List<HologramManager> getHolograms(Long timeMilliseconds)
    {
        List<HologramManager> hologramsAll = getHolograms();
        List<HologramManager> returnList = new ArrayList<HologramManager>();
        for (HologramManager hologramManager: hologramsAll)
        {
            ArmorStand entity = hologramManager.getArmorStand();
            List<String> tags = TitanBoxLibs.tools.getNBTTool(TitanBoxLibs.instants).getListString(entity, "Tags");
            if (tags.contains(timeMilliseconds+""))
            {
                returnList.add(hologramManager);
            }
        }
        return returnList;
    }
    public List<HologramManager> getHolograms()
    {
        List<HologramManager> returnList = new ArrayList<HologramManager>();
        String strPlugin = parent.getPlugin().getName();
        List<World> worlds = Bukkit.getWorlds();
        for(World world: worlds)
        {
            Collection<Entity> nearbyEntities = world.getEntities();
            for (Entity entity : nearbyEntities) {
                if (entity.getType() == EntityType.ARMOR_STAND) {
                    List<String> tags = TitanBoxLibs.tools.getNBTTool(TitanBoxLibs.instants).getListString(entity, "Tags");
                    if (tags.contains("tblHG"))
                    {
                        if (tags.contains(strPlugin)) returnList.add(getHologram(entity.getUniqueId()));
                    }
                }
            }
        }
        return returnList;
    }
    public boolean isHologram(Entity entity)
    {
        if (entity.getType() == EntityType.ARMOR_STAND) {
            List<String> tags = TitanBoxLibs.tools.getNBTTool(TitanBoxLibs.instants).getListString(entity, "Tags");
            if (tags.contains("tblHG"))
            {
                return true;
            }
        }
        return false;
    }
    public boolean isMyHologram(Entity entity)
    {
        if (entity.getType() == EntityType.ARMOR_STAND) {
            List<String> tags = TitanBoxLibs.tools.getNBTTool(TitanBoxLibs.instants).getListString(entity, "Tags");
            if (tags.contains("tblHG"))
            {
                String strPlugin = parent.getPlugin().getName();
                if (tags.contains(strPlugin)) return true;
            }
        }
        return false;
    }
    public HologramManager getHologram(UUID uuid)
    {
        if (uuid == null) return null;
        ArmorStand entity = (ArmorStand) Bukkit.getEntity(uuid);
        if (entity != null && entity.getType() == EntityType.ARMOR_STAND) {
            HologramManager e = new HologramManager(this.parent.getPlugin(), entity);
            return e;
        }
        return null;
    }
    @Deprecated
    private HologramManager getHologram(Location location)
    {
        HologramManager closes = null;
        if (closes == null)
        {
            for(int i = 1; i< 4;i++) {
                List<Entity> entities = TitanBoxLibs.tools.getEntityTool(TitanBoxLibs.instants).findEntities(location, i);
                for (Entity entity : entities) {
                    if (entity.getType() == EntityType.ARMOR_STAND) {
                        HologramManager hologramManager = new HologramManager(parent.getPlugin(), (ArmorStand) entity);
                        return hologramManager;
                    }
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
