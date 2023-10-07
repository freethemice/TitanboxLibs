package com.firesoftitan.play.titanbox.libs.managers;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.*;

/**
 * Manages saving and loading YAML configuration files for data files.
 * some stuff will be encoded and user will not be-able to read.
 */
public class SaveManager {
    protected YamlConfiguration config;
    protected File file;
    /**
     * Constructs a SaveManager for a YAML file in the plugin data folder.
     *
     * @param pluginName The name of the plugin.
     * @param fileName The name of the YAML file.
     * @param folder The name of the subfolder.
     */
    public SaveManager(String pluginName, String folder, String fileName) {
        try {
            config = new YamlConfiguration();
            File dataStorageDIR = new File("plugins");
            if (!dataStorageDIR.exists())
            {
                boolean mkdir = dataStorageDIR.mkdir();
            }
            File TitanBoxDIR = new File("plugins" + File.separator + pluginName );
            if (!TitanBoxDIR.exists())
            {
                boolean mkdir = TitanBoxDIR.mkdir();
            }
            File TitanBoxDIRF = new File("plugins" + File.separator + pluginName + File.separator + folder );
            if (!TitanBoxDIRF.exists())
            {
                boolean mkdir = TitanBoxDIRF.mkdir();
            }

            this.file = new File("plugins" + File.separator + pluginName + File.separator + folder + File.separator  + fileName + ".yml");
            if (!this.file.exists())
            {
                boolean newFile = this.file.createNewFile();
            }

            config.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    /**
     * Constructs a SaveManager for a YAML file in the plugin data folder.
     *
     * @param pluginName The name of the plugin.
     * @param fileName The name of the YAML file.
     */
    public SaveManager(String pluginName, String fileName) {
        try {
            config = new YamlConfiguration();
            File dataStorageDIR = new File("plugins");
            if (!dataStorageDIR.exists())
            {
                boolean mkdir = dataStorageDIR.mkdir();
            }
            File TitanBoxDIR = new File("plugins" + File.separator + pluginName );
            if (!TitanBoxDIR.exists())
            {
                boolean mkdir = TitanBoxDIR.mkdir();
            }
            this.file = new File("plugins" + File.separator + pluginName + File.separator  + fileName + ".yml");
            if (!this.file.exists())
            {
                boolean newFile = this.file.createNewFile();
            }
            config.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    /**
     * Gets the File object representing the YAML file.
     *
     * @return The File.
     */
    public File getFile() {
        return new File(file.getAbsolutePath());
    }

    /**
     * Constructs a SaveManager from an InputStream.
     *
     * @param inputStream The InputStream to load the YAML data from.
     */
    public SaveManager(InputStream inputStream) {
        try {
            Reader reader = new InputStreamReader(inputStream);
            config = new YamlConfiguration();
            config.load(reader);
            reader.close();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    /**
     * Constructs a SaveManager from a Reader.
     *
     * @param reader The Reader to load the YAML data from.
     */
    public SaveManager(Reader reader) {
        try {
            config = new YamlConfiguration();
            config.load(reader);
            reader.close();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    /**
     * Constructs a SaveManager for a YAML file in the plugin data folder.
     *
     * @param file The File of the File.
     */
    public SaveManager(File file) {
        try {
            this.file = file;
            if (!this.file.exists()) {
                boolean newFile = this.file.createNewFile();
            }
            config = new YamlConfiguration();
            config.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    /**
     * Constructs a SaveManager for a YAML file in the plugin data folder.
     *
     * @param fileName file name of the File in the data-storage folder.
     */
    public SaveManager(String fileName) {
        try {
            config = new YamlConfiguration();
            File dataStorageDIR = new File("data-storage");
            if (!dataStorageDIR.exists())
            {
                boolean mkdir = dataStorageDIR.mkdir();
            }
            File TitanBoxDIR = new File("data-storage" + File.separator + "TitanBox" );
            if (!TitanBoxDIR.exists())
            {
                boolean mkdir = TitanBoxDIR.mkdir();
            }
            this.file = new File("data-storage" + File.separator + "TitanBox" + File.separator  + fileName + "_save.yml");
            if (!this.file.exists())
            {
                boolean newFile = this.file.createNewFile();
            }
            config.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    /**
     * Associates a SaveManager to YAML file.
     * You must use save() to write the config to file.
     *
     */
    public void convertToFile(File file)
    {
        try {
            this.file = file;
            if (!this.file.exists())
            {
                boolean newFile = this.file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Associates a SaveManager to YAML file in the data-storage folder.
     * You must use save() to write the config to file.
     *
     */
    public void convertToFile(String fileName)
    {
        try {
            File dataStorageDIR = new File("data-storage");
            if (!dataStorageDIR.exists())
            {
                boolean mkdir = dataStorageDIR.mkdir();
            }
            File TitanBoxDIR = new File("data-storage" + File.separator + "TitanBox" );
            if (!TitanBoxDIR.exists())
            {
                boolean mkdir = TitanBoxDIR.mkdir();
            }
            this.file = new File("data-storage" + File.separator + "TitanBox" + File.separator  + fileName + "_save.yml");
            if (!this.file.exists())
            {
                boolean newFile = this.file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Constructs a blank SaveManager with no YAML file associated.
     * Use convertToFile(file) to save it
     *
     */
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
        String encode = EncodeDecodeManager.encode(list);
        config.set(key, encode);
    }
    @Deprecated
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
    /**
     * Clones the saveManager
     */
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public SaveManager clone()
    {
        SaveManager managerClone = new SaveManager();
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
    protected YamlConfiguration getConfig() {
        return config;
    }
    public SaveManager getSaveManager(String key) {
        if (!config.contains(key)) return new SaveManager();
        YamlConfiguration configuration = EncodeDecodeManager.decodeYaml(config.getString(key));
        if (configuration == null) return new SaveManager();
        return new SaveManager(configuration);
    }
    /**
     * Saves the configuration to the YAML file.
     */
    public void save()
    {
        try {
            boolean delete = this.file.delete();
            boolean newFile = this.file.createNewFile();
            config.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * Reloads the configuration from the YAML file.
     */
    public void reload() {
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
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
    @Deprecated
    public List<String> getStringListFromText(String path) {
        try {
            if (config.contains(path)){
                if (config.getString(path) != null) {
                    //noinspection unchecked
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
