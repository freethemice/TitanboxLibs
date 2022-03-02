package com.firesoftitan.play.titanbox.libs.managers;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings({"ResultOfMethodCallIgnored", "unchecked"})
public class SaveManager {
    private YamlConfiguration config;
    private File file;
    public SaveManager(String pluginname, String folder, String fileName) {
        try {
            config = new YamlConfiguration();
            File dataStorageDIR = new File("plugins");
            if (!dataStorageDIR.exists())
            {
                dataStorageDIR.mkdir();
            }
            File TitanBoxDIR = new File("plugins" + File.separator + pluginname );
            if (!TitanBoxDIR.exists())
            {
                TitanBoxDIR.mkdir();
            }
            File TitanBoxDIRF = new File("plugins" + File.separator + pluginname + File.separator + folder );
            if (!TitanBoxDIRF.exists())
            {
                TitanBoxDIRF.mkdir();
            }

            this.file = new File("plugins" + File.separator + pluginname + File.separator + folder + File.separator  + fileName + ".yml");
            if (!this.file.exists())
            {
                this.file.createNewFile();
            }
            config.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public SaveManager(String pluginname, String fileName) {
        try {
            config = new YamlConfiguration();
            File dataStorageDIR = new File("plugins");
            if (!dataStorageDIR.exists())
            {
                dataStorageDIR.mkdir();
            }
            File TitanBoxDIR = new File("plugins" + File.separator + pluginname );
            if (!TitanBoxDIR.exists())
            {
                TitanBoxDIR.mkdir();
            }
            this.file = new File("plugins" + File.separator + pluginname + File.separator  + fileName + ".yml");
            if (!this.file.exists())
            {
                this.file.createNewFile();
            }
            config.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public SaveManager(File file) {
        try {
            this.file = file;
            config = new YamlConfiguration();
            config.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public SaveManager(String fileName) {
        try {
            config = new YamlConfiguration();
            File dataStorageDIR = new File("data-storage");
            if (!dataStorageDIR.exists())
            {
                dataStorageDIR.mkdir();
            }
            File TitanBoxDIR = new File("data-storage" + File.separator + "TitanBox" );
            if (!TitanBoxDIR.exists())
            {
                TitanBoxDIR.mkdir();
            }
            this.file = new File("data-storage" + File.separator + "TitanBox" + File.separator  + fileName + "_save.yml");
            if (!this.file.exists())
            {
                this.file.createNewFile();
            }
            config.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void convertToFile(File file)
    {
        try {
            this.file = file;
            if (!this.file.exists())
            {
                this.file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void convertToFile(String fileName)
    {
        try {
            File dataStorageDIR = new File("data-storage");
            if (!dataStorageDIR.exists())
            {
                dataStorageDIR.mkdir();
            }
            File TitanBoxDIR = new File("data-storage" + File.separator + "TitanBox" );
            if (!TitanBoxDIR.exists())
            {
                TitanBoxDIR.mkdir();
            }
            this.file = new File("data-storage" + File.separator + "TitanBox" + File.separator  + fileName + "_save.yml");
            if (!this.file.exists())
            {
                this.file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public SaveManager() {
        config = new YamlConfiguration();
        this.file = null;
    }
    private SaveManager(YamlConfiguration config) {
        if (config == null) return;
        this.config = config;
    }

    public void set(String key, Object object)
    {
        if (object == null) return;
        config.set(key, object);
    }
    public void set(String key, UUID uuid)
    {
        if (uuid == null) return;
        config.set(key, uuid.toString());
    }
    public void set(String key, @SuppressWarnings("rawtypes") List list)
    {
        set(key, list, false);
    }
    public void set(String key, @SuppressWarnings("rawtypes") List list, boolean asText)
    {
        if (list == null) return;
        if (asText)
        {
            config.set(key, list);
        }
        else {
            String encode = EncodeDecodeManager.encode(list);
            config.set(key, encode);
        }

    }
    public void set(String key, Location location)
    {
        if (location == null) return;
        //String encode = SaveManager.encode(location);
        config.set(key, location);
    }
    public void set(String key, ItemStack itemStack)
    {
        if (itemStack == null) return;
        String encode = EncodeDecodeManager.encode(itemStack);
        config.set(key, encode);
    }
    public void set(String key, SaveManager saveManager)
    {
        if (saveManager == null) return;
        String encode = EncodeDecodeManager.encode(saveManager.getConfig());
        config.set(key, encode);
    }
    public void delete(String key)
    {
        config.set(key, null);
    }
    public boolean contains(String key)
    {
        return config.contains(key);
    }
    public Set<String> getKeys()
    {
        try {
            return this.config.getKeys(false);
        } catch (Exception e) {
            return new HashSet<>();
        }
    }
    public Set<String> getKeys(String key)
    {
        try {
            ConfigurationSection configurationSection = this.config.getConfigurationSection(key);
            if (configurationSection == null) return new HashSet<>();
            return configurationSection.getKeys(false);
        } catch (Exception e) {
            return new HashSet<>();
        }
    }
    private YamlConfiguration getConfig() {
        return config;
    }
    public SaveManager getSaveManager(String key) {
        if (!config.contains(key)) return new SaveManager();
        YamlConfiguration configuration = EncodeDecodeManager.decodeYaml(config.getString(key));
        if (configuration == null) return new SaveManager();
        return new SaveManager(configuration);
    }


    public void save()
    {
        try {
            this.file.delete();
            this.file.createNewFile();
            config.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public ItemStack getItem(String path) {
        try {
            if (config.contains(path)){
                    if (config.getString(path) != null) {
                        return EncodeDecodeManager.decodeItemStack(config.getString(path));
                    }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getString(String path) {
        return config.getString(path);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }
    public List<Location> getLocationList(String path) {
        try {
            if (config.contains(path)){
                if (config.getString(path) != null) {
                    return EncodeDecodeManager.decodeLocationList(config.getString(path));
                }
            }
            return new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }
    public List<EntityType> getEntityTypeList(String path) {
        try {
            if (config.contains(path)){
                if (config.getString(path) != null) {
                    return EncodeDecodeManager.decodeEntityTypeList(config.getString(path));
                }
            }
            return new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }
    public List<UUID> getUUIDList(String path) {
        try {
            if (config.contains(path)){
                if (config.getString(path) != null) {
                    return EncodeDecodeManager.decodeUUIDList(config.getString(path));
                }
            }
            return new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }
    public List<String> getStringList(String path) {
        try {
            if (config.contains(path)){
                if (config.getString(path) != null) {
                    return EncodeDecodeManager.decodeStringList(config.getString(path));
                }
            }
            return new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }
    public List<String> getStringListFromText(String path) {
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
    public List<ItemStack> getItemList(String path) {
        try {
            if (config.contains(path)){
                if (config.getString(path) != null) {
                    return EncodeDecodeManager.decodeItemList(config.getString(path));
                }
            }
            return new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }

    public List<Integer> getIntList(String path) {
        try {
            if (config.contains(path)){
                if (config.getString(path) != null) {
                    return EncodeDecodeManager.decodeIntList(config.getString(path));
                }
            }
            return new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }


    }

    public Long getLong(String path) {
        return config.getLong(path);
    }

    public UUID getUUID(String path) {
        try {
            if (config.contains(path)){
                String stringUUID = config.getString(path);
                if (stringUUID != null) {
                    return UUID.fromString(stringUUID);
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public Double getDouble(String path) {
        return config.getDouble(path);
    }

    public Location getLocation(String path) {
        return config.getLocation(path);
    }


}
