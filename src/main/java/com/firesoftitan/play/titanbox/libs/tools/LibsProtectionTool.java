package com.firesoftitan.play.titanbox.libs.tools;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.managers.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredListener;

import java.util.ArrayList;
import java.util.List;

import static com.firesoftitan.play.titanbox.libs.tools.Tools.getMessageTool;

public class LibsProtectionTool {
    private static final SettingsManager protectionConfig = new SettingsManager(TitanBoxLibs.instants.getName(), "protection");
    private static final List<RegisteredListener> protectionPlugins = new ArrayList<RegisteredListener>();

    public static boolean isAdmin(Player player)
    {
        return player.isOp() || player.hasPermission("titanbox.admin");
    }
    public static boolean isSetup()
    {
        return !protectionPlugins.isEmpty();
    }
    public static List<String> getListingProtectionPlugins(Player player)
    {
        Location location = player.getLocation();
        Block block = location.getBlock();
        Block blockA = location.clone().add(1, 0, 0).getBlock();
        BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(block, block.getState(), blockA, new ItemStack(Material.STONE), player, true, EquipmentSlot.HAND);
        Bukkit.getPluginManager().callEvent(blockPlaceEvent);
        HandlerList handlers = blockPlaceEvent.getHandlers();
        List<String> out = new ArrayList<String>();
        for (RegisteredListener listener: handlers.getRegisteredListeners())
        {
            if (!out.contains(listener.getPlugin().getName())) out.add(listener.getPlugin().getName());

        }
        return out;
    }
    public static List<String> getEnabledProtectionPlugins(Player player)
    {
        List<String> stringListFromText = protectionConfig.getStringList("setup.protection.enabled");
        protectionPlugins.clear();
        List<String> out = new ArrayList<String>();
        for (String listener: getListingProtectionPlugins(player))
        {
            if (stringListFromText.contains(listener) && !out.contains(listener)) out.add(listener);
        }
        return out;
    }
    public static void setupProtectionPlugins(Player player)
    {
        Location location = player.getLocation();
        Block block = location.getBlock();
        Block blockA = location.clone().add(1, 0, 0).getBlock();
        BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(block, block.getState(), blockA, new ItemStack(Material.STONE), player, true, EquipmentSlot.HAND);
        Bukkit.getPluginManager().callEvent(blockPlaceEvent);
        HandlerList handlers = blockPlaceEvent.getHandlers();
        List<String> stringListFromText = protectionConfig.getStringList("setup.protection.enabled");
        protectionPlugins.clear();
        for (RegisteredListener listener: handlers.getRegisteredListeners())
        {
            if (stringListFromText.contains(listener.getPlugin().getName()) && !protectionPlugins.contains(listener)) protectionPlugins.add(listener);
        }
        if (isAdmin(player) && protectionPlugins.isEmpty())
            getMessageTool(TitanBoxLibs.instants).sendMessagePlayer(player, ChatColor.RED + "No Protection plugin has been setup please use command " + ChatColor.WHITE + "/tb protection");
    }
    public static boolean canPlaceBlock(Player player, Location location)
    {
        if (!protectionPlugins.isEmpty())
        {
            Block block = location.getBlock();
            Block blockA = location.clone().add(1, 0, 0).getBlock();
            BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(block, block.getState(), blockA, new ItemStack(Material.STONE), player, true, EquipmentSlot.HAND);
            for (RegisteredListener listener: protectionPlugins)
            {
                try {
                    listener.callEvent(blockPlaceEvent);
                } catch (EventException e) {
                    return false;
                }
                if (blockPlaceEvent.isCancelled())
                {
                    return false;
                }
            }
        }
        return true;
    }
    public static void addPlugin(Player player, String plugin)
    {
        List<String> stringListFromText = protectionConfig.getStringList("setup.protection.enabled");
        if (!stringListFromText.contains(plugin)) stringListFromText.add(plugin);
        protectionConfig.set("setup.protection.enabled", stringListFromText);
        protectionConfig.save();
        setupProtectionPlugins(player);
    }
    public static void removePlugin(Player player, String plugin)
    {
        List<String> stringListFromText = protectionConfig.getStringList("setup.protection.enabled");
        stringListFromText.remove(plugin);
        protectionConfig.set("setup.protection.enabled", stringListFromText);
        protectionConfig.save();
        setupProtectionPlugins(player);
    }

}
