package com.firesoftitan.play.titanbox.libs.managers;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.tools.Tools;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SettingsManager extends SaveManager{
    public SettingsManager(String pluginname, String folder, String fileName) {
        super(pluginname, folder, fileName);
    }

    public SettingsManager(String pluginname, String fileName) {
        super(pluginname, fileName);
    }

    public SettingsManager(File file) {
        super(file);
    }

    public SettingsManager(String fileName) {
        super(fileName);
    }

    public SettingsManager() {
    }
    @Override
    public void set(String key, @SuppressWarnings("rawtypes") List list)
    {
        config.set(key, list);
    }
    @Override
    public List<String> getStringList(String path) {
        try {
            if (config.contains(path)){
                if (config.getString(path) != null) {
                    return (ArrayList<String>)config.getList(path);
                }
            }
            return new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }
    @Override
    public void set(String key, ItemStack itemStack)
    {
        config.set(key+ ".material", itemStack.getType().name());
        config.set(key + ".amount", itemStack.getAmount());
        String nbtString = TitanBoxLibs.tools.getNBTTool().getNBTString(itemStack.clone());
        config.set(key + ".nbt", nbtString);
    }
    @Override
    public ItemStack getItem(String path) {
        try {
            if (config.contains(path)){
                String mat = config.getString(path + ".material");
                int amount = config.getInt(path + ".amount");
                String nbt = config.getString(path + ".nbt");
                Material material = Material.getMaterial(mat.toLowerCase().replace("minecraft:", "").toUpperCase());
                ItemStack itemStack = TitanBoxLibs.tools.getNBTTool().getItemStack(material, amount , nbt);
                return itemStack.clone();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public SettingsManager clone()
    {
        SettingsManager managerClone = new SettingsManager();
        for (String keys: this.config.getKeys(true))
        {

            Object o1 = this.config.get(keys);
            if (o1 instanceof Integer)
            {
                Integer o = (Integer) this.config.get(keys);
                managerClone.set(keys, o);
            } else if (o1 instanceof Double)
            {
                Double o = (Double) this.config.get(keys);
                managerClone.set(keys, o);
            } else if (o1 instanceof Float)
            {
                Float o = (Float) this.config.get(keys);
                managerClone.set(keys, o);

            } else if (o1 instanceof Long)
            {
                Long o = (Long) this.config.get(keys);
                managerClone.set(keys, o);

            } else if (o1 instanceof Byte)
            {
                Byte o = (Byte) this.config.get(keys);
                managerClone.set(keys, o);
            } else
            {
                String o = this.config.getString(keys);
                managerClone.set(keys, o);
            }
        }
        return managerClone;
    }
}
