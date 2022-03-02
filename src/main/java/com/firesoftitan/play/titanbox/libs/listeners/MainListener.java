package com.firesoftitan.play.titanbox.libs.listeners;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.enums.BarcodeDeviceEnum;
import com.firesoftitan.play.titanbox.libs.managers.RecipeManager;
import com.firesoftitan.play.titanbox.libs.tools.Tools;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import com.firesoftitan.play.titanbox.libs.tools.LibsMiscTool;
import com.firesoftitan.play.titanbox.libs.tools.LibsSkullTool;
import org.bukkit.scheduler.BukkitRunnable;

import static com.firesoftitan.play.titanbox.libs.TitanBoxLibs.*;

public class MainListener  implements Listener {


    private Tools tools;
    public MainListener(){
        registerEvents();
        tools = Tools.getTools(instants);
    }
    private void registerEvents(){
        PluginManager pm = instants.getServer().getPluginManager();
        pm.registerEvents(this, instants);
    }

    @EventHandler
    public void onPrepareAnvilEvent(PrepareAnvilEvent event)
    {
        ItemStack[] contents = event.getInventory().getContents();
        for(ItemStack stack: contents) {
            if (tools.getRecipeTool().isAdvancedRecipe(stack))
            {
                event.setResult(null);
            }
        }
    }
    @EventHandler
    public void onPrepareSmithingEvent(PrepareSmithingEvent event)
    {
        ItemStack[] contents = event.getInventory().getContents();
        for(ItemStack stack: contents) {
            if (tools.getRecipeTool().isAdvancedRecipe(stack))
            {
                event.setResult(null);
            }
        }
    }

    @EventHandler
    public void onPrepareItemCraftEvent(PrepareItemCraftEvent event)
    {
        CraftingInventory inventory = event.getInventory();
        ItemStack[] current = inventory.getMatrix().clone();
        ItemStack result = inventory.getResult();
        if (tools.getRecipeTool().isAdvancedRecipe(result))
        {
            RecipeManager recipeManager = tools.getRecipeTool().getAdvancedRecipe(result);
            ItemStack[] matrix = recipeManager.getMatrix();
            for (int i = 0; i < current.length; i++) {
                String recipeIDC = tools.getItemStackTool().getTitanItemID(current[i]);
                String recipeIDM = tools.getItemStackTool().getTitanItemID(matrix[i]);
                if (recipeIDM != null)
                {
                    if (!recipeIDM.equals(recipeIDC))
                    {
                        inventory.setResult(null);
                        return;
                    }
                }
                else if (!tools.getItemStackTool().isItemEqual(matrix[i], current[i])) {
                    inventory.setResult(null);
                    return;
                }
            }
        } else {
            for (int i = 0; i < current.length; i++) {
                if (tools.getRecipeTool().isAdvancedRecipe(current[i])) {
                    inventory.setResult(null);
                    return;
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public boolean onBlockPlaceEvent(BlockPlaceEvent event) {
        Block placed = event.getBlockPlaced();
        String id = tools.getSkullTool().getSkullTitanID(placed);
        ItemStack item = event.getItemInHand();
        boolean placeable = tools.getSkullTool().isSkullPlaceable(item);
        BarcodeDeviceEnum deviceEnum = tools.getMiscTool().isUpgradeDevice(item);
        if ((deviceEnum != BarcodeDeviceEnum.NONE) || (id != null && id.equals("upgrade_device")) || !placeable) {
            event.setCancelled(true);
            tools.getMessageTool().sendMessagePlayer(event.getPlayer(), ChatColor.RED + "Can't place this block!");
        }


        ItemStack itemInHand = event.getItemInHand();
        if (!tools.getItemStackTool().isPlaceable(itemInHand)) event.setCancelled(true);
        return false;
    }
    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                 workerManager.hideWorker(player);
            }
        }.runTaskLater(instants, 20);
        TitanBoxLibs.autoUpdateManager.checkAll(player);
    }
}
