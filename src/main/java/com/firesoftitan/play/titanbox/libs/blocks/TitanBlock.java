package com.firesoftitan.play.titanbox.libs.blocks;

import com.firesoftitan.play.titanbox.libs.managers.SaveManager;
import com.firesoftitan.play.titanbox.libs.managers.TitanBlockManager;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class TitanBlock {

    public static void updateBlock(TitanBlock titanBlock)
    {
        TitanBlockManager.setTitanBlock(titanBlock, titanBlock.getLocation());
    }

    protected final SaveManager saveManager;
    public TitanBlock(String titanID, ItemStack itemStack, Location location) {
        saveManager = new SaveManager();
        saveManager.set("location", location);
        saveManager.set("itemStack", itemStack);
        saveManager.set("titanID", titanID);
    }
    public TitanBlock(SaveManager saveManager) {
        this.saveManager = saveManager;

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

