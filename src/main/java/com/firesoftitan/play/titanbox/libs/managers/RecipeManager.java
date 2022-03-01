package com.firesoftitan.play.titanbox.libs.managers;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class RecipeManager {
    private String id;
    private ItemStack result;
    private ItemStack[] matrix;
    private JavaPlugin plugin;

    public RecipeManager(JavaPlugin plugin, String id, ItemStack result, ItemStack[] matrix) {
        this.plugin = plugin;
        this.id = id;
        this.result = result;
        this.matrix = matrix;
    }

    public String getId() {
        return id;
    }

    public ItemStack getResult() {
        return result.clone();
    }

    public ItemStack[] getMatrix() {
        return matrix.clone();
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
