package com.firesoftitan.play.titanbox.libs.guis;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.tools.Tools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class YesNoGui {
    private static final HashMap<UUID, YesNoGui> activeGuis = new HashMap<UUID, YesNoGui>();
    public static final String guiName = "Mouseover Sign to read Question";
    private static final int size = 9;

    private final Inventory inventory;
    private final List<String> lore;
    private final BukkitRunnable confirmation;
    private final String question;
    public YesNoGui(JavaPlugin plugin, Player viewer, BukkitRunnable confirmation, String question, String... Message) {
        activeGuis.put(viewer.getUniqueId(), this);
        inventory = Bukkit.createInventory(null, YesNoGui.size, YesNoGui.guiName);
        lore = new ArrayList<>(Arrays.asList(Message));
        this.confirmation = confirmation;
        this.question = question;
        mainDraw();
        viewer.openInventory(inventory);
        Tools.getMessageTool(plugin).sendMessagePlayer(viewer, question);
    }
    public void mainDraw()
    {
        ItemStack itemStack = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        itemStack = TitanBoxLibs.tools.getItemStackTool().changeName(itemStack, ChatColor.GREEN + "YES");
        for (int i =0; i < 4; i++)
        {

            inventory.setItem(i, itemStack);
        }

        itemStack = new ItemStack(Material.OAK_HANGING_SIGN);
        itemStack = TitanBoxLibs.tools.getItemStackTool().changeName(itemStack, question);
        if (!lore.isEmpty()) itemStack = TitanBoxLibs.tools.getItemStackTool().addLore(itemStack, lore);


        inventory.setItem(4, itemStack);

        itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        itemStack = TitanBoxLibs.tools.getItemStackTool().changeName(itemStack, ChatColor.RED + "NO");
        for (int i =5; i < 9; i++)
        {

            inventory.setItem(i, itemStack);
        }
    }
    public static void onClick(InventoryClickEvent event, HumanEntity whoClicked, InventoryView openInventory, Inventory clickedInventory) {
        YesNoGui yesNoGui = YesNoGui.activeGuis.get(whoClicked.getUniqueId());
        if (yesNoGui == null) return;
        if (event.getSlot() > -1 && event.getSlot() < YesNoGui.size && clickedInventory.equals(yesNoGui.inventory)) {
            event.setCancelled(true);
            ItemStack clicked = clickedInventory.getItem(event.getSlot());
            if (clicked == null) return;
            if(clicked.getType() == Material.OAK_HANGING_SIGN) return;
            if(clicked.getType() == Material.GREEN_STAINED_GLASS_PANE)
            {
                yesNoGui.confirmation.runTask(TitanBoxLibs.instants);
            }
            whoClicked.closeInventory();
            YesNoGui.activeGuis.remove(whoClicked.getUniqueId());
        }

    }
}
