package com.firesoftitan.play.titanbox.libs.tools;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.managers.RecipeManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class LibsAdvancedRecipeTool {
    private Tools parent;
    private static final HashMap<String, RecipeManager> recipeChecks = new HashMap<String, RecipeManager>();
    public LibsAdvancedRecipeTool(Tools parent) {
        this.parent = parent;
    }

    public List<String> getKeys()
    {
        return new ArrayList<String>(recipeChecks.keySet());
    }
    public RecipeManager getAdvancedRecipe(String titanID)
    {
        return recipeChecks.get(titanID);
    }

    public RecipeManager getAdvancedRecipe(ItemStack itemStack)
    {
        if (Tools.tools.getItemStackTool().isEmpty(itemStack)) return null;
        String id = Tools.tools.getNBTTool().getString(itemStack,"TitanItemID");
        return recipeChecks.get(id);
    }
    public boolean isAdvancedRecipe(ItemStack itemStack)
    {
        if (Tools.tools.getItemStackTool().isEmpty(itemStack)) return false;
        String id = Tools.tools.getNBTTool().getString(itemStack,"TitanItemID");
        return recipeChecks.containsKey(id);
    }
    public void addAdvancedRecipe(String titanItemID, ItemStack itemStack, ItemStack[] matrix) {
        ItemStack titanItem = Tools.tools.getItemStackTool().setTitanItemID(itemStack, titanItemID);
        addAdvancedRecipe(titanItem, matrix);
    }
    public void addAdvancedRecipe(ItemStack titanItem, ItemStack[] matrix) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (matrix.length != 9)
                {
                    Tools.tools.getMessageTool().sendMessageSystem(titanItem.getType().name() + ": Matrix length must be 9!", Level.WARNING);
                    return;
                }
                String titanItemID = Tools.tools.getItemStackTool().getTitanItemID(titanItem);
                if (titanItemID == null || titanItemID.isEmpty())
                {
                    Tools.tools.getMessageTool().sendMessageSystem(titanItem.getType().name() + ": Recipe not added, no TitanItemID found!", Level.WARNING);
                    return;
                }
                RecipeManager recipeManager = new RecipeManager(parent.getPlugin(), titanItemID, titanItem, matrix.clone());
                recipeChecks.put(recipeManager.getId(), recipeManager);
                HashMap<Material, Integer> recipeCount = new HashMap<Material, Integer>();
                HashMap<Material, String> recipeLetters = new HashMap<Material, String>();
                ItemStack[] managerMatrix = recipeManager.getMatrix();
                for(ItemStack itemStack: managerMatrix)
                {
                    if (!recipeCount.containsKey(itemStack.getType())) recipeCount.put(itemStack.getType(), 0);
                    int count = recipeCount.get(itemStack.getType());
                    count++;
                    recipeCount.put(itemStack.getType(), count);
                }
                String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
                int index = 0;
                for(Material material: recipeCount.keySet())
                {
                    recipeLetters.put(material, letters[index]);
                    index++;
                }
                String firstRow = recipeLetters.get(managerMatrix[0].getType()) + recipeLetters.get(managerMatrix[1].getType()) + recipeLetters.get(managerMatrix[2].getType());
                String secondRow = recipeLetters.get(managerMatrix[3].getType()) + recipeLetters.get(managerMatrix[4].getType()) + recipeLetters.get(managerMatrix[5].getType());
                String thirdRow = recipeLetters.get(managerMatrix[6].getType()) + recipeLetters.get(managerMatrix[7].getType()) + recipeLetters.get(managerMatrix[8].getType());

                NamespacedKey key = new NamespacedKey(parent.getPlugin(), recipeManager.getId());
                ShapedRecipe recipe = new ShapedRecipe(key, recipeManager.getResult());
                recipe.shape(firstRow, secondRow, thirdRow);
                for(Material material: recipeLetters.keySet())
                {
                    char c = recipeLetters.get(material).charAt(0);
                    recipe.setIngredient(c, material);
                }
                Bukkit.addRecipe(recipe);
                Tools.tools.getMessageTool().sendMessageSystem("Recipe added, TitanItemID: " +  titanItemID, Level.INFO);
            }
        }.runTaskLater(TitanBoxLibs.instants, 10);

    }

}
