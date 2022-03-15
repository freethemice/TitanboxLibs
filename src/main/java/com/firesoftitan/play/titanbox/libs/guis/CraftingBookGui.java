package com.firesoftitan.play.titanbox.libs.guis;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.managers.RecipeManager;
import com.firesoftitan.play.titanbox.libs.tools.LibsAdvancedRecipeTool;
import com.firesoftitan.play.titanbox.libs.tools.LibsItemStackTool;
import com.firesoftitan.play.titanbox.libs.tools.LibsNBTTool;
import com.firesoftitan.play.titanbox.libs.tools.Tools;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CraftingBookGui {

    private static HashMap<UUID, CraftingBookGui> activeGuis = new HashMap<UUID, CraftingBookGui>();
    public static String guiName = "Titan Crafting Book";
    public static int size = 54;
    public static CraftingBookGui getGui(Player player)
    {
        if (activeGuis.containsKey(player.getUniqueId())) {
            CraftingBookGui plainFillerGUI = activeGuis.get(player.getUniqueId());
            return plainFillerGUI;
        }
        return null;
    }

    public static int getSize() {
        return size;
    }

    private Player viewer;
    private LibsAdvancedRecipeTool advancedRecipeTool;
    private LibsNBTTool nbtTool;
    private LibsItemStackTool itemStackTool;
    private Inventory inventory;
    private String showing = null;
    private int scrolling = 0;
    public CraftingBookGui(Player viewer) {
        this.viewer = viewer;
        nbtTool = Tools.getNBTTool(TitanBoxLibs.instants);
        itemStackTool = Tools.getItemStackTool(TitanBoxLibs.instants);
        advancedRecipeTool = Tools.getRecipeTool(TitanBoxLibs.instants);
        activeGuis.put(viewer.getUniqueId(), this);
        inventory = Bukkit.createInventory(null, 54, CraftingBookGui.guiName);
        setup();
    }
    public void setShowing(String showing)
    {
        this.showing = showing;
    }

    public int getScrolling() {
        return scrolling;
    }

    public void setScrolling(int scrolling) {
        if (scrolling < 0) scrolling = 0;
        int max = advancedRecipeTool.getKeys().size();
        if (scrolling > max - 4) scrolling = max - 4;
        this.scrolling = scrolling;

    }
    public void setup()
    {
        int[] slots_crafting = {20, 21, 22, 29, 30 ,31, 38, 39, 40};
        int intcrafted = 33;

        ItemStack button = itemStackTool.getCustomModel(71009);
        button = nbtTool.set(button, "buttonaction", "none");
        for (int i = 0; i < size; i++) {
            if (!ArrayUtils.contains(slots_crafting, i) && i != intcrafted)
                inventory.setItem(i, button.clone());
        }
        button = itemStackTool.getCustomModel(71013);
        button = nbtTool.set(button, "buttonaction", "up");
        inventory.setItem(0, button);

        button = itemStackTool.getCustomModel(71012);
        button = nbtTool.set(button, "buttonaction", "down");
        inventory.setItem(45, button);
    }
    public void mainDraw()
    {
        int[] slots = {9, 18, 27, 36};
        int[] slots_crafting = {20, 21, 22, 29, 30 ,31, 38, 39, 40};
        int intcrafted = 33;

        ItemStack button;

        for(int s: slots)
        {
            button = itemStackTool.getCustomModel(71025);
            button = nbtTool.set(button, "buttonaction", "none");
            inventory.setItem(s, button);
        }
        int i = 0;
        List<String> keys = advancedRecipeTool.getKeys();
        for(String key: keys)
        {
            RecipeManager recipeManager = advancedRecipeTool.getAdvancedRecipe(key);
            if (showing != null && showing.equals(key)) {
                ItemStack[] list = recipeManager.getMatrix();
                for (int x = 0; x < 9; x++) {
                    button = list[x].clone();
                    String ttID = itemStackTool.getTitanItemID(button);
                    if (ttID == null || ttID.length() < 1) ttID = "none";
                    button = nbtTool.set(button, "buttonaction", ttID);
                    inventory.setItem(slots_crafting[x], button);
                }
                button = recipeManager.getResult();
                button = nbtTool.set(button, "buttonaction", "none");
                inventory.setItem(intcrafted, button);
            }
            if (i - scrolling > -1 && i - scrolling < slots.length) {
                button = recipeManager.getResult();
                button = nbtTool.set(button, "buttonaction", key);
                inventory.setItem(slots[i - scrolling], button);
            }
            i++;
        }
    }
    public void showGUI()
    {
        mainDraw();
        viewer.openInventory(inventory);
    }
    public boolean isGuiOpen()
    {
        if (viewer != null) {
            if (viewer.getOpenInventory().getTitle().equals(guiName)) {
                return true;
            }
        }
        return false;
    }
    public Player getViewer()
    {
        if (viewer != null) {
            return viewer;
        }
        return null;
    }

}
