package com.firesoftitan.play.titanbox.libs.blocks;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.managers.SaveManager;
import com.firesoftitan.play.titanbox.libs.managers.TitanBlockManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TitanBlock {

    public static TitanBlock getBlock(Location location)
    {
        return TitanBlockManager.getTitanBlock(location);
    }
    public static void updateBlock(TitanBlock titanBlock)
    {
        TitanBlockManager.setTitanBlock(titanBlock, titanBlock.getLocation());
    }
    public static void breakBlock(TitanBlock titanBlock)
    {
        TitanBlockManager.delete(titanBlock, titanBlock.getLocation());
        ItemStack itemStack = titanBlock.getItemStack();
        if (!TitanBoxLibs.tools.getItemStackTool().isEmpty(itemStack)) {
            itemStack.setAmount(1);
            if (titanBlock.getLocation().getWorld() != null) titanBlock.getLocation().getWorld().dropItem(titanBlock.getLocation(), itemStack);
            titanBlock.getLocation().getBlock().setType(Material.AIR);
        }
    }

    protected final SaveManager saveManager;
    public TitanBlock(String titanID, ItemStack itemStack, Location location) {
        saveManager = new SaveManager();
        saveManager.set("location", location);
        saveManager.set("itemStack", itemStack);
        saveManager.set("titanID", titanID);
        saveManager.set("redraw", true);
        saveManager.set("break", true);
    }
    public TitanBlock(SaveManager saveManager) {
        this.saveManager = saveManager;
        if (!this.saveManager.contains("redraw")) this.saveManager.set("redraw", true);
        if (!this.saveManager.contains("break")) this.saveManager.set("break", true);

    }
    public void setBreakBlockAllowed(Boolean updateBlock)
    {
        saveManager.set("break", updateBlock);
        updateBlock(this);
    }

    public boolean isBreakBlockAllowed()
    {
        return saveManager.getBoolean("break");
    }
    public void setRedrawBlock(Boolean updateBlock)
    {
        saveManager.set("redraw", updateBlock);
        updateBlock(this);
    }

    public boolean isRedrawBlock()
    {
        return saveManager.getBoolean("redraw");
    }
    public SaveManager getSaveManager() {
        return saveManager;
    }

    public ItemStack getItemStack() {
        return saveManager.getItem("itemStack");
    }

    public Location getLocation() {
        return saveManager.getLocation("location");
    }

    public String getTitanID() {
        return saveManager.getString("titanID");
    }

}

