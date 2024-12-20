package com.firesoftitan.play.titanbox.libs.managers;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Manages saving and loading YAML configuration files for config/setting files.
 * Every thing will be in a format easy to read be the user.
 */
public class SettingsManager extends SaveManager{
    /**
     * Constructs a SaveManager for a YAML file in the plugin data folder.
     *
     * @param pluginName The name of the plugin.
     * @param fileName The name of the YAML file.
     * @param folder The name of the subfolder.
     */
    public SettingsManager(String pluginName, String folder, String fileName) {
        super(pluginName, folder, fileName);
    }
    /**
     * Constructs a SaveManager for a YAML file in the plugin data folder.
     *
     * @param pluginName The name of the plugin.
     * @param fileName The name of the YAML file.
     */
    public SettingsManager(String pluginName, String fileName) {
        super(pluginName, fileName);
    }

    /**
     * Constructs a SaveManager for a YAML file in the plugin data folder.
     *
     * @param file The File of the File.
     */
    public SettingsManager(File file) {
        super(file);
    }
    /**
     * Constructs a SaveManager from an InputStream.
     *
     * @param inputStream The InputStream to load the YAML data from.
     */
    public SettingsManager(InputStream inputStream) {
        super(inputStream);
    }
    /**
     * Constructs a SaveManager from a Reader.
     *
     * @param reader The Reader to load the YAML data from.
     */
    public SettingsManager(Reader reader) {
        super(reader);
    }
    /**
     * Constructs a SaveManager for a YAML file in the plugin data folder.
     *
     * @param fileName file name of the File in the data-storage folder.
     */
    public SettingsManager(String fileName) {
        super(fileName);
    }

    /**
     * Constructs a blank SaveManager with no YAML file associated.
     * Use convertToFile(file) to save it
     *
     */
    public SettingsManager() {
        super();
    }
    @Override
    public void set(String key, @SuppressWarnings("rawtypes") List list)
    {
        if (list.isEmpty()) return;
        if (list.getFirst() instanceof UUID)
        {
            List<String> convert = new ArrayList<String>();
            for (Object uuid: list)
            {
               convert.add(uuid.toString());
            }
            config.set(key, convert);
            return;
        }
        if (list.getFirst() instanceof  ItemStack)
        {
            List<String> convert = new ArrayList<String>();
            for (Object item: list)
            {
                if (item != null) convert.add(TitanBoxLibs.tools.getNBTTool().getNBTString((ItemStack) item));
            }
            config.set(key, convert);
            return;
        }
        if (list.getFirst() instanceof Location)
        {
            List<String> convert = new ArrayList<String>();
            for (Object location: list)
            {
                if (location != null)  convert.add(TitanBoxLibs.tools.getSerializeTool().serializeLocation((Location) location));
            }
            config.set(key, convert);
            return;
        }
        config.set(key, list);
    }
    @Override
    public List<Location> getLocationList(String path) {
        try {
            if (config.contains(path)){
                if (config.getString(path) != null) {
                    List<String> list = (ArrayList<String>) config.getList(path);
                    List<Location> convert = new ArrayList<Location>();
                    for(String string: list)
                    {
                        Location location = TitanBoxLibs.tools.getSerializeTool().deserializeLocation(string);
                        convert.add(location);
                    }
                    return convert;
                }
            }
            return new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<ItemStack> getItemList(String path) {
        try {
            if (config.contains(path)){
                if (config.getString(path) != null) {
                    List<String> list = (ArrayList<String>) config.getList(path);
                    List<ItemStack> convert = new ArrayList<ItemStack>();
                    for(String string: list)
                    {
                        ItemStack itemStack = TitanBoxLibs.tools.getNBTTool().getItemStack(string);
                        convert.add(itemStack);
                    }
                    return convert;
                }
            }
            return new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<UUID> getUUIDList(String path) {
        try {
            if (config.contains(path)){
                if (config.getString(path) != null) {
                    List<String> list = (ArrayList<String>) config.getList(path);
                    List<UUID> convert = new ArrayList<UUID>();
                    for(String string: list)
                    {
                        convert.add(UUID.fromString(string));
                    }
                    return convert;
                }
            }
            return new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
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
        String nbtString = TitanBoxLibs.tools.getNBTTool().getNBTString(itemStack.clone());
        config.set(key + ".nbt", nbtString);
    }
    @Override
    public ItemStack getItem(String path) {
        try {
            if (config.contains(path)){
                String nbt = config.getString(path + ".nbt");
                ItemStack itemStack = TitanBoxLibs.tools.getNBTTool().getItemStack(nbt);
                return itemStack.clone();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Clones the SettingManager
     */
    @SuppressWarnings("MethodDoesntCallSuperMethod")
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
