package com.firesoftitan.play.titanbox.libs.tools;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.Random;

public class LibsLocationTool {
    private Tools parent;

    public LibsLocationTool(Tools parent) {
        this.parent = parent;
    }

    public String getWorldName(Location location)
    {
        if (location == null) return "NULL";
        return getWorldName(location.getWorld());
    }
    public String getWorldName(World world)
    {
        if (world == null) return "NULL";
        return world.getName();
    }
    public boolean isLoaded(Location loc)
    {
        if (loc == null) return false;
        if (loc.getWorld() == null) return false;
        World world = loc.getWorld();
        return world.isChunkLoaded(loc.getBlockX() >> 4, loc.getBlockZ() >> 4);
    }
    public Location getRandomLocation(World world, int size)
    {
        Random letsGo = new Random(System.currentTimeMillis());
        double x = letsGo.nextInt(size * 2);
        double y = 300;
        double z = letsGo.nextInt(size * 2);
        x = x - size;
        z = z - size;
        Location location = new Location(world, x, y, z);
        y = world.getHighestBlockYAt(location);
        Location location1 = new Location(world, x, y, z);
        if (location1.clone().add(0, -1, 0).getBlock().getType() == Material.WATER || location1.clone().add(0, -1, 0).getBlock().getType() == Material.LAVA)
        {
            return getRandomLocation(world, size);
        }
        return location1.clone();
    }
    public boolean isLocationsEqual(Location location1, Location location2)
    {
        World world1 = location1.getWorld();
        World world2 = location2.getWorld();
        if (world1 != null && world2 != null && world1.getName().equals(world2.getName()) || world1 == world2)
        {
            if (location1.getBlockX() == location2.getBlockX())
            {
                if (location1.getBlockY() == location2.getBlockY())
                {
                    return location1.getBlockZ() == location2.getBlockZ();
                }
            }
        }
        return false;
    }
}
