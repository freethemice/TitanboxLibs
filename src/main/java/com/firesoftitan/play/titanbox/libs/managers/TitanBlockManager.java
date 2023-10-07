package com.firesoftitan.play.titanbox.libs.managers;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.blocks.TitanBlock;
import com.firesoftitan.play.titanbox.libs.listeners.MainTBListener;
import com.firesoftitan.play.titanbox.libs.listeners.TitanBlockListener;
import com.firesoftitan.play.titanbox.libs.tools.Tools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class TitanBlockManager  {
    private static final MainTBListener tbmListener = new MainTBListener();
    public static void saveAll()
    {
        for(SaveManager saveManager: allSaveManagersByTitanID.values())
        {
            saveManager.save();
        }
    }


    private static final HashMap<String, ItemStack> allItemStacksByTitanID = new HashMap<String, ItemStack>();
    private static final HashMap<String, String> allPluginsByTitanID = new HashMap<String, String>();
    private static final HashMap<String, SaveManager> allSaveManagersByTitanID = new HashMap<String, SaveManager>();
    private static final HashMap<String, TitanBlockListener> allTBListenersByPlugin = new HashMap<String, TitanBlockListener>();

    public static void registerTitanBlock(final JavaPlugin plugin, final ItemStack titanItem, final ItemStack[] recipe)
    {
        Tools.getTools(plugin).getRecipeTool().addAdvancedRecipe(titanItem, recipe); //this has to be there plugin
        String titanID = Tools.getTools(plugin).getItemStackTool().getTitanItemID(titanItem); //this can be ours
        String name = removeInvalidFilenameChars(plugin.getName());
        SaveManager saveManager  = new SaveManager(  "load_test"); //this is to make all the directories
        File TitanBoxDIR = new File("data-storage" + File.separator + "TitanBox" + File.separator + name);
        if (!TitanBoxDIR.exists())
        {
            boolean mkdir = TitanBoxDIR.mkdir();
        }
        saveManager  = new SaveManager(  name + File.separator  +"tbm_" + titanID.toLowerCase());
        allItemStacksByTitanID.put(titanID, titanItem.clone());
        allPluginsByTitanID.put(titanID, plugin.getName());
        allSaveManagersByTitanID.put(titanID, saveManager);
    }
    public static TitanBlockListener getTitanBlockListener(final JavaPlugin plugin)
    {
        return allTBListenersByPlugin.get(plugin.getName());
    }
    public static TitanBlockListener getTitanBlockListener(TitanBlock titanBlock)
    {
        return getTitanBlockListener(titanBlock.getTitanID());
    }
    public static TitanBlockListener getTitanBlockListener(ItemStack titanItem)
    {
        String titanID = Tools.getTools(TitanBoxLibs.instants).getItemStackTool().getTitanItemID(titanItem);
        if (titanID == null || titanID.isBlank() || titanID.isEmpty()) return null;
        return getTitanBlockListener(titanID);
    }
    public static TitanBlockListener getTitanBlockListener(String TitanID)
    {
        String s = allPluginsByTitanID.get(TitanID);
        return allTBListenersByPlugin.get(s);
    }
    public static void registerListener(final JavaPlugin plugin, final TitanBlockListener titanBlockListener)
    {
        allTBListenersByPlugin.put(plugin.getName(), titanBlockListener);
    }

    public static String removeInvalidFilenameChars(String filename) {
        return filename.replaceAll("[\\\\/:*?\"<>|]", "_");
    }

    public static ItemStack getItemStack(String titanID)
    {
        return allItemStacksByTitanID.get(titanID);
    }
    public static RecipeManager getRecipe(String titanID) {
        JavaPlugin plugin = (JavaPlugin) Bukkit.getPluginManager().getPlugin(allPluginsByTitanID.get(titanID));
        return Tools.getTools(plugin).getRecipeTool().getAdvancedRecipe(titanID);
    }
    public static List<String> getTitanIDs (JavaPlugin plugin)
    {
        List<String> out = new ArrayList<String>();
        for (String p: allPluginsByTitanID.keySet())
        {
            if (allPluginsByTitanID.get(p).equals(plugin.getName())) out.add(p);
        }
        return out;
    }
    public static Boolean isTitanBlock(Location location)
    {
        for(String titanID: allPluginsByTitanID.keySet())
        {
            if (isTitanBlock(titanID, location)) return true;
        }
        return false;
    }
    public static Boolean isTitanBlock(TitanBlock titanBlock, Location location)
    {
        return isTitanBlock(titanBlock.getTitanID(), location);
    }
    public static Boolean isTitanBlock(ItemStack titanItem, Location location)
    {
        String titanID = Tools.getTools(TitanBoxLibs.instants).getItemStackTool().getTitanItemID(titanItem);
        return isTitanBlock(titanID, location);
    }
    public static Boolean isTitanBlock(String titanID, Location location)
    {
        if (location == null) return false;
        if (titanID == null || titanID.isBlank() || titanID.isEmpty()) return false;
        String key = TitanBoxLibs.tools.getSerializeTool().serializeLocation(location);
        SaveManager saveManager = allSaveManagersByTitanID.get(titanID);
        if (saveManager != null) {
            if (saveManager.contains(key) && saveManager.contains(key + ".location")) {
                Location hoppersLocation = saveManager.getLocation(key + ".location");
                return TitanBoxLibs.tools.getLocationTool().isLocationsEqual(hoppersLocation, location);
            }
        }
        return false;
    }
    public static TitanBlock getTitanBlock(Location location)
    {
        for(String titanID: allPluginsByTitanID.keySet())
        {
            if (isTitanBlock(titanID, location)) return getTitanBlock(titanID, location);
        }
        return null;
    }
    public static TitanBlock getTitanBlock(ItemStack titanItem, Location location)
    {
        String titanID = Tools.getTools(TitanBoxLibs.instants).getItemStackTool().getTitanItemID(titanItem);
        return getTitanBlock(titanID, location);
    }
    public static TitanBlock getTitanBlock(String titanID, Location location)
    {
        if (location == null) return null;
        if (titanID == null || titanID.isBlank() || titanID.isEmpty()) return null;
        String key = TitanBoxLibs.tools.getSerializeTool().serializeLocation(location);
        SaveManager saveManager = allSaveManagersByTitanID.get(titanID);
        if (saveManager != null) {
            return new TitanBlock(saveManager.getSaveManager(key + ".data"));
        }
        return null;
    }
    public static void setTitanBlock(TitanBlock titanBlock, Location location)
    {
        if (location == null) return;
        String titanID = titanBlock.getTitanID();
        String key = TitanBoxLibs.tools.getSerializeTool().serializeLocation(location);
        SaveManager saveManager = allSaveManagersByTitanID.get(titanID);
        if (saveManager != null) {
            saveManager.set(key + ".location", location);
            saveManager.set(key + ".data", titanBlock.getSaveManager());
        }
    }
    public static void createTitanBlock(ItemStack itemStack, Location location)
    {
        String titanID = Tools.getTools(TitanBoxLibs.instants).getItemStackTool().getTitanItemID(itemStack);
        createTitanBlock(titanID, location);
    }
    public static void createTitanBlock(String titanID, Location location)
    {
        if (location == null) return;
        if (titanID == null || titanID.isBlank() || titanID.isEmpty()) return;
        String key = TitanBoxLibs.tools.getSerializeTool().serializeLocation(location);
        SaveManager saveManager = allSaveManagersByTitanID.get(titanID);
        if (saveManager != null) {
            saveManager.set(key + ".location", location);
            ItemStack titanItem = allItemStacksByTitanID.get(titanID);
            TitanBlock titanBlock = new TitanBlock(titanID, titanItem, location);
            saveManager.set(key + ".data", titanBlock.getSaveManager());
        }
    }
    public static void delete(TitanBlock titanBlock, Location location)
    {
        delete(titanBlock.getTitanID(), location);
    }
    public static void delete(ItemStack itemStack, Location location)
    {
        String titanID = Tools.getTools(TitanBoxLibs.instants).getItemStackTool().getTitanItemID(itemStack);
        delete(titanID, location);
    }
    public static void delete(String titanID, Location location)
    {
        if (location == null) return;
        if (titanID == null || titanID.isBlank() || titanID.isEmpty()) return;
        String key = TitanBoxLibs.tools.getSerializeTool().serializeLocation(location);
        SaveManager saveManager = allSaveManagersByTitanID.get(titanID);
        if (saveManager != null) {
            saveManager.delete(key);
        }
    }
    public static void save(TitanBlock titanBlock)
    {
        save(titanBlock.getTitanID());
    }
    public static void save(ItemStack itemStack)
    {
        String titanID = Tools.getTools(TitanBoxLibs.instants).getItemStackTool().getTitanItemID(itemStack);
        save(titanID);
    }
    public static void save(String titanID)
    {
        if (titanID == null || titanID.isBlank() || titanID.isEmpty()) return;
        SaveManager saveManager = allSaveManagersByTitanID.get(titanID);
        saveManager.save();

    }

    public static Set<Location> getLocations(String titanID) {
        Set<Location> mySet = new HashSet<Location>();
        if (titanID == null || titanID.isBlank() || titanID.isEmpty()) return mySet;
        SaveManager saveManager = allSaveManagersByTitanID.get(titanID);
        for(String key: saveManager.getKeys())
        {
            Location location = saveManager.getLocation(key + ".location");
            mySet.add(location);
        }
        return mySet;
    }

}
