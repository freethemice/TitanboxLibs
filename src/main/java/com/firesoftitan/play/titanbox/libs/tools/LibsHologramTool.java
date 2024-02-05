package com.firesoftitan.play.titanbox.libs.tools;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.managers.HologramManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class LibsHologramTool {
    private final Tools parent;
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
        return new HologramManager(this.parent.getPlugin(), location);
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
        String strPlugin = parent.getPlugin().getName();
        List<HologramManager> returnList = new ArrayList<HologramManager>();
        HologramManager hologramManager = HologramManager.getHologramManager(location);
        if (hologramManager != null && strPlugin.equals(hologramManager.getPlugin().getName())) returnList.add(hologramManager);
        return returnList;
    }
    public List<HologramManager> getHolograms()
    {
        List<HologramManager> returnList = new ArrayList<HologramManager>();
        List<HologramManager> rawList = HologramManager.getHologramManagers();
        String strPlugin = parent.getPlugin().getName();
        for (HologramManager manager: rawList)
        {
            if (manager != null &&  manager.getPlugin().getName().equals(strPlugin)) returnList.add(manager);
        }
        return returnList;
    }
    private boolean isLong(String string)
    {
        try {
            Long.parseLong(string);
            return true;
        } catch (NumberFormatException ignored) {

        }
        return false;
    }
    public long getTimeStamp(Entity entity)
    {
        if (entity.getType() == EntityType.ARMOR_STAND) {
            List<String> tags = Tools.getNBTTool(TitanBoxLibs.instants).getListString(entity, "Tags");
            if (tags.contains("tblHG"))
            {
                for (String t: tags)
                {
                    if (isLong(t))
                    {
                        return Long.parseLong(t);
                    }
                }

            }
        }
        return -1;
    }

    public String getVersion(Entity entity)
    {
        if (isHologram(entity)) {
            if (entity.getType() == EntityType.ARMOR_STAND) {
                List<String> tags = Tools.getNBTTool(TitanBoxLibs.instants).getListString(entity, "Tags");
                for (String tag : tags) {
                    if (tag.startsWith("Version: ")) return tag.replace("Version: ", "");
                }
            }
            return "1.0.0";
        }
        return null;
    }
    public boolean isHologram(Entity entity)
    {
        if (entity.getType() == EntityType.ARMOR_STAND) {
            List<String> tags = Tools.getNBTTool(TitanBoxLibs.instants).getListString(entity, "Tags");
            return tags.contains("tblHG");
        }
        return false;
    }
    public boolean isMyHologram(Entity entity)
    {
        if (entity.getType() == EntityType.ARMOR_STAND) {
            List<String> tags = Tools.getNBTTool(TitanBoxLibs.instants).getListString(entity, "Tags");
            if (tags.contains("tblHG"))
            {
                String strPlugin = parent.getPlugin().getName();
                return tags.contains(strPlugin);
            }
        }
        return false;
    }
    public HologramManager getHologram(UUID uuid)
    {
        if (uuid == null) return null;
        return HologramManager.getHologramManager(uuid);
    }
    private HologramManager getHologram(Location location)
    {
        return HologramManager.getHologramManager(location);
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
                for (Entity nearbyEntity : nearbyEntities) {
                    if (nearbyEntity.getType() == EntityType.ARMOR_STAND) {
                        nearbyEntity.remove();
                    }
                }
            }
        }.runTaskLater(this.parent.getPlugin(), 1);

    }

}
