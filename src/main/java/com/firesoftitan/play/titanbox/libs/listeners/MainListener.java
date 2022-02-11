package com.firesoftitan.play.titanbox.libs.listeners;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.enums.BarcodeDeviceEnum;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import com.firesoftitan.play.titanbox.libs.tools.LibsMiscTool;
import com.firesoftitan.play.titanbox.libs.tools.LibsSkullTool;
import org.bukkit.scheduler.BukkitRunnable;

public class MainListener  implements Listener {


    public MainListener(){
        registerEvents();
    }
    private void registerEvents(){
        PluginManager pm = TitanBoxLibs.instants.getServer().getPluginManager();
        pm.registerEvents(this, TitanBoxLibs.instants);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public static boolean onBlockPlaceEvent(BlockPlaceEvent event) {
        Block placed = event.getBlockPlaced();
        String id =  TitanBoxLibs.tools.getSkullTool().getSkullTitanID(placed);
        ItemStack item = event.getItemInHand();
        boolean placeable =  TitanBoxLibs.tools.getSkullTool().isSkullPlaceable(item);
        BarcodeDeviceEnum deviceEnum =  TitanBoxLibs.tools.getMiscTool().isUpgradeDevice(item);
        if ((deviceEnum != BarcodeDeviceEnum.NONE) || (id != null && id.equals("upgrade_device")) || !placeable)
        {
            event.setCancelled(true);
            TitanBoxLibs.tools.getMessageTool().sendMessagePlayer(event.getPlayer(), ChatColor.RED + "Can't place this block!");
        }
        return false;
    }
    @EventHandler
    public static void onPlayerLoginEvent(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                 TitanBoxLibs.tools.getMiscTool().workerManager.hideWorker(player);
            }
        }.runTaskLater(TitanBoxLibs.instants, 20);
    }
}
