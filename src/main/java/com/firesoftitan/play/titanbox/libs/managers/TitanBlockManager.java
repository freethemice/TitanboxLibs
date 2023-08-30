package com.firesoftitan.play.titanbox.libs.managers;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.blocks.TitanBlock;
import com.firesoftitan.play.titanbox.libs.listeners.TitanBlockListener;
import com.firesoftitan.play.titanbox.libs.tools.Tools;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public abstract class TitanBlockManager  {
    protected static final HashMap<String, TitanBlockManager> allBlocks = new HashMap<String, TitanBlockManager>();
    private static final TitanBlockListener tbmListener = new TitanBlockListener();
    public static TitanBlockManager getTitanBlockManager(String name)
    {
        return allBlocks.get(name);
    }
    public static Collection<TitanBlockManager> getAllBlocks()
    {
        return allBlocks.values();
    }
    public static void saveAll()
    {
        for(TitanBlockManager tbm: allBlocks.values())
        {
            tbm.save();
        }
    }

    private final Set<Location> mySet = new HashSet<Location>();
    private final SaveManager TBMSaveManager;
    protected final String titanID;
    private final ItemStack titanItem;
    private final ItemStack[] recipe;


    public TitanBlockManager(final JavaPlugin plugin, final ItemStack titanItem, final ItemStack[] recipe){
        Tools.getTools(plugin).getRecipeTool().addAdvancedRecipe(titanItem, recipe);
        this.titanID = Tools.getTools(plugin).getItemStackTool().getTitanItemID(titanItem);
        this.TBMSaveManager  = new SaveManager( "tbm_" + titanID.toLowerCase());
        this.titanItem = titanItem.clone();
        this.recipe = recipe.clone();
        allBlocks.put(titanID, this);

    }

    public ItemStack getTitanItem() {
        return titanItem;
    }

    public ItemStack[] getRecipe() {
        return recipe;
    }

    public String getTitanID() {
        return titanID;
    }

    public Boolean isTitanBlock(Location location)
    {
        if (location == null) return false;
        String key = TitanBoxLibs.tools.getSerializeTool().serializeLocation(location);
        if (this.TBMSaveManager.contains(key) && this.TBMSaveManager.contains(key + ".location")) {
            Location hoppersLocation = this.TBMSaveManager.getLocation(key + ".location");
            return TitanBoxLibs.tools.getLocationTool().isLocationsEqual(hoppersLocation, location);
        }
        return false;
    }
    public TitanBlock getTitanBlock(Location location)
    {
        String key = TitanBoxLibs.tools.getSerializeTool().serializeLocation(location);
        return new TitanBlock(this.TBMSaveManager.getSaveManager(key + ".data"));
    }
    public void setTitanBlock(Location location, TitanBlock titanBlock)
    {
        String key = TitanBoxLibs.tools.getSerializeTool().serializeLocation(location);
        this.TBMSaveManager.set(key + ".location", location);
        this.TBMSaveManager.set(key + ".data", titanBlock.getSaveManager());
    }

    public void setLocation(Location location)
    {
        String key = TitanBoxLibs.tools.getSerializeTool().serializeLocation(location);
        this.TBMSaveManager.set(key + ".location", location);
        TitanBlock titanBlock = new TitanBlock(this.titanID, this.titanItem, location);
        this.TBMSaveManager.set(key + ".data", titanBlock.getSaveManager());
    }
    public void delete(Location location)
    {
        String key = TitanBoxLibs.tools.getSerializeTool().serializeLocation(location);
        this.TBMSaveManager.delete(key);
    }
    public void save()
    {
        this.TBMSaveManager.save();
    }

    public Set<Location> getLocations() {
        mySet.clear();
        for(String key: this.TBMSaveManager.getKeys())
        {
            Location location = this.TBMSaveManager.getLocation(key + ".location");
            mySet.add(location);
        }
        return mySet;
    }

    public abstract void onPlayerInteract(PlayerInteractEvent event, Location location, TitanBlock titanBlock);



}
