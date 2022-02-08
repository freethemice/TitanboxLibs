package src.com.firesoftitan.play.titanbox.libs.listeners;

import src.com.firesoftitan.play.titanbox.libs.TitanBoxLibsPlugin;
import src.com.firesoftitan.play.titanbox.libs.enums.BarcodeDeviceEnum;
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
import src.com.firesoftitan.play.titanbox.libs.tools.LibsMessageTool;
import src.com.firesoftitan.play.titanbox.libs.tools.LibsMiscTool;
import src.com.firesoftitan.play.titanbox.libs.tools.LibsSkullTool;

public class MainListener  implements Listener {


    public MainListener(){
        registerEvents();
    }
    private void registerEvents(){
        PluginManager pm = TitanBoxLibsPlugin.instants.getServer().getPluginManager();
        pm.registerEvents(this, TitanBoxLibsPlugin.instants);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public static boolean onBlockPlaceEvent(BlockPlaceEvent event) {
        Block placed = event.getBlockPlaced();
        String id = LibsSkullTool.getSkullTitanID(placed);
        ItemStack item = event.getItemInHand();
        boolean placeable = LibsSkullTool.isSkullPlaceable(item);
        BarcodeDeviceEnum deviceEnum = LibsMiscTool.isUpgradeDevice(item);
        if ((deviceEnum != BarcodeDeviceEnum.NONE) || (id != null && id.equals("upgrade_device")) || !placeable)
        {
            event.setCancelled(true);
            LibsMessageTool.sendMessagePlayer(TitanBoxLibsPlugin.instants, event.getPlayer(), ChatColor.RED + "Can't place this block!");
        }
        return false;
    }
    @EventHandler
    public static void onPlayerLoginEvent(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        LibsMiscTool.workerManager.hideWorker(player);
    }
}
