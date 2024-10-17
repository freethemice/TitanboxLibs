package com.firesoftitan.play.titanbox.libs.listeners;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.enums.BarcodeDeviceEnum;
import com.firesoftitan.play.titanbox.libs.guis.CraftingBookGui;
import com.firesoftitan.play.titanbox.libs.guis.YesNoGui;
import com.firesoftitan.play.titanbox.libs.managers.RecipeManager;
import com.firesoftitan.play.titanbox.libs.tools.LibsItemStackTool;
import com.firesoftitan.play.titanbox.libs.tools.LibsNBTTool;
import com.firesoftitan.play.titanbox.libs.tools.LibsProtectionTool;
import com.firesoftitan.play.titanbox.libs.tools.Tools;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import static com.firesoftitan.play.titanbox.libs.TitanBoxLibs.instants;
import static com.firesoftitan.play.titanbox.libs.TitanBoxLibs.workerManager;

public class MainListener  implements Listener {


    private final Tools tools;
    private final LibsItemStackTool itemStackTool = Tools.getItemStackTool(instants);
    private final LibsNBTTool nbtTool = Tools.getNBTTool(instants);
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
            for (ItemStack itemStack : current) {
                if (tools.getRecipeTool().isAdvancedRecipe(itemStack)) {
                    inventory.setResult(null);
                    return;
                }
            }
        }
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        HumanEntity whoClicked = event.getWhoClicked();
        InventoryView openInventory = whoClicked.getOpenInventory();
        Inventory clickedInventory = event.getClickedInventory();
        if (openInventory.getTitle().equals(CraftingBookGui.guiName)) CraftBookGuiClicked(event, whoClicked, openInventory, clickedInventory);
        if (openInventory.getTitle().equals(YesNoGui.guiName)) YesNoGui.onClick(event, whoClicked, openInventory, clickedInventory);

    }
    private void CraftBookGuiClicked(InventoryClickEvent event, HumanEntity whoClicked, InventoryView openInventory, Inventory clickedInventory) {

        if (openInventory.getTitle().equals(CraftingBookGui.guiName)) {
            CraftingBookGui bookGui = CraftingBookGui.getGui((Player) whoClicked);
            if (bookGui != null) {
                if (event.getSlot() > -1 && event.getSlot() < CraftingBookGui.getSize()) {
                    ItemStack clicked = clickedInventory.getItem(event.getSlot());
                    if (!itemStackTool.isEmpty(clicked)) {
                        String action = nbtTool.getString(clicked, "buttonaction");
                        if (action != null && action.length() > 1) {
                            event.setCancelled(true);
                            switch (action.toLowerCase()) {
                                case "none":

                                    break;
                                case "down":
                                    bookGui.setScrolling(bookGui.getScrolling() + 1);
                                    bookGui.showGUI();
                                    break;
                                case "up":
                                    bookGui.setScrolling(bookGui.getScrolling() - 1);
                                    bookGui.showGUI();
                                    break;
                                default:
                                    bookGui.setShowing(action);
                                    bookGui.showGUI();
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
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
    }
    @EventHandler
    public void  onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            ItemStack itemInMainHand = event.getPlayer().getInventory().getItemInMainHand();
            String titanID = itemStackTool.getTitanItemID(itemInMainHand);
            if (titanID != null && titanID.equals("CRAFTING_BOOK")) {
                CraftingBookGui craftingBookGui = CraftingBookGui.getGui(player);
                if (craftingBookGui == null) craftingBookGui = new CraftingBookGui(player);
                craftingBookGui.showGUI();
            }
        }
    }
    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                workerManager.hideWorker(player);
                TitanBoxLibs.autoUpdateManager.checkAll(player);
                if (!LibsProtectionTool.isSetup()) LibsProtectionTool.setupProtectionPlugins(player);
            }
        }.runTaskLater(instants, 20);



    }
}
