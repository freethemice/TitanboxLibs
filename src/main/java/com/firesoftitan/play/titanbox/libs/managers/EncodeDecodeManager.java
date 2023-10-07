package com.firesoftitan.play.titanbox.libs.managers;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class EncodeDecodeManager {

    private EncodeDecodeManager()
    {

    }
    /**
     * Decodes an {@link ItemStack} from a Base64 String
     * @param string Base64 encoded String to decode
     * @return Decoded {@link ItemStack}
     */
    public static ItemStack decodeItemStack(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(Base64.getDecoder().decode(string), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException | InvalidConfigurationException ignored) {

        }
        ItemStack outItem = null;
        if (config.contains("i")) {
            outItem = config.getItemStack("i", null);
            if (outItem == null) {
                config = new YamlConfiguration();
                try {
                    config.loadFromString(config.saveToString());
                } catch (IllegalArgumentException | InvalidConfigurationException e) {
                    //              e.printStackTrace();
                    return null;
                }
                outItem = config.getItemStack("i", null);
            }
        }
        if (config.contains("x"))
        {
            try {
                String item = config.getString("x");
                NBTTagCompound item2 = MojangsonParser.a(item); //parse
                outItem = CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.a(item2));
            } catch (CommandSyntaxException e) {
//            e.printStackTrace();
            }
        }

        return outItem;
    }

    public static List<EntityType> decodeEntityTypeList(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(Base64.getDecoder().decode(string), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        List<EntityType> tmp = new ArrayList<>();
        for (String key: config.getKeys(false))
        {
            if (key.equals("i"))
            {
                return tmp;
            }
            EntityType itsub = EntityType.valueOf(config.getString(key));
            tmp.add(itsub);
        }

        return tmp;
    }

    public static List<UUID> decodeUUIDList(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(Base64.getDecoder().decode(string), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        List<UUID> tmp = new ArrayList<>();
        for (String key: config.getKeys(false))
        {
            if (key.equals("i"))
            {
                return tmp;
            }
            String stringUUID = config.getString(key);
            if (stringUUID != null) {
                UUID stub = UUID.fromString(stringUUID);
                tmp.add(stub);
            }
        }

        return tmp;
    }

    public static List<Location> decodeLocationList(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(Base64.getDecoder().decode(string), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        List<Location> tmp = new ArrayList<>();
        for (String key: config.getKeys(false))
        {
            if (key.equals("i"))
            {
                return tmp;
            }
            Location itsub = config.getLocation(key);
            tmp.add(itsub);
        }

        return tmp;
    }

    /**
     * Decodes an {@link ItemStack} from a Base64 String
     * @param string Base64 encoded String to decode
     * @return Decoded {@link ItemStack}
     */
    public static List<ItemStack> decodeItemList(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(Base64.getDecoder().decode(string), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        List<ItemStack> tmp = new ArrayList<>();
        for (String key: config.getKeys(false))
        {
            if (key.equals("i"))
            {
                return tmp;
            }
            ItemStack itSub = config.getItemStack(key);
            tmp.add(itSub);
        }

        return tmp;
    }

    /**
     * Decodes an {@link ItemStack} from a Base64 String
     * @param string Base64 encoded String to decode
     * @return Decoded {@link ItemStack}
     */
    public static List<String> decodeStringList(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(Base64.getDecoder().decode(string), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        //noinspection unchecked
        return (List<String>) config.getList("i", new ArrayList<String>());
    }

    /**
     * Decodes an {@link ItemStack} from a Base64 String
     * @param string Base64 encoded String to decode
     * @return Decoded {@link ItemStack}
     */
    public static List<Long> decodeLongList(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(Base64.getDecoder().decode(string), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        //noinspection unchecked
        return (List<Long>) config.getList("i", new ArrayList<Integer>());
    }

    /**
     * Decodes an {@link ItemStack} from a Base64 String
     * @param string Base64 encoded String to decode
     * @return Decoded {@link ItemStack}
     */
    public static List<Byte> decodeByteList(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(Base64.getDecoder().decode(string), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        //noinspection unchecked
        return (List<Byte>) config.getList("i", new ArrayList<Integer>());
    }

    /**
     * Decodes an {@link ItemStack} from a Base64 String
     * @param string Base64 encoded String to decode
     * @return Decoded {@link ItemStack}
     */
    public static List<Integer> decodeIntList(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(Base64.getDecoder().decode(string), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        //noinspection unchecked
        return (List<Integer>) config.getList("i", new ArrayList<Integer>());
    }

    /**
     * Decodes an {@link Location} from a Base64 String
     * @param string Base64 encoded String to decode
     * @return Decoded {@link Location}
     */
    public static Location decodeLocation(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(Base64.getDecoder().decode(string), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        double x = config.getDouble("i.x");
        double y = config.getDouble("i.y");
        double z = config.getDouble("i.z");
        float pitch =  0;
        float yaw = 0;
        String stringYaw = config.getString("i.yaw");
        String stringPitch = config.getString("i.pitch");
        if (stringYaw != null) yaw = Float.parseFloat(stringYaw);
        if (stringPitch != null) pitch = Float.parseFloat(stringPitch);

        String worldName = config.getString("i.world");
        if (worldName == null) return null;
        World world = Bukkit.getWorld(worldName);
        Location location = new Location(world, x, y, z, yaw, pitch);
        return  location.clone();
    }

    /**
     * Encodes an {@link String} in a Base64 String
     * @param string {@link String} to encode
     * @return Base64 encoded String
     */
    public static String encode(String string) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("s", string);
        return Base64.getEncoder().encodeToString(config.saveToString().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Encodes an {@link ItemStack} in a Base64 String
     * @param itemStack {@link ItemStack} to encode
     * @return Base64 encoded String
     */
    public static String encode(ItemStack itemStack) {
        String configString;
        YamlConfiguration config;
        NBTTagCompound item = CraftItemStack.asNMSCopy(itemStack).b(new NBTTagCompound());
        config = new YamlConfiguration();
        //String??
        config.set("x", item.r_());
        configString = config.saveToString();
        byte[] configBytes = configString.getBytes(StandardCharsets.UTF_8);
            return Base64.getEncoder().encodeToString(configBytes);



    }

    /**
     * Encodes an {@link Location} in a Base64 String
     * @param location {@link Location} to encode
     * @return Base64 encoded String
     */
    public static String encode(Location location) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("i.x", location.getX());
        config.set("i.y", location.getY());
        config.set("i.z", location.getZ());
        config.set("i.pitch", location.getPitch() + "");
        config.set("i.yaw", location.getYaw() + "");
        if (location.getWorld() == null)
        {
            config.set("i.world", "worldmain");
        }
        else {
            config.set("i.world", location.getWorld().getName());
        }
        return Base64.getEncoder().encodeToString(config.saveToString().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Encodes an {@link List<Integer>} in a Base64 String
     * Encodes an {@link List<String>} in a Base64 String
     * Encodes an {@link List<ItemStack>} in a Base64 String
     * @param list {@link List} to encode
     * @return Base64 encoded String
     */
    public static String encode(@SuppressWarnings("rawtypes") List list) {
        YamlConfiguration config = new YamlConfiguration();
        if (!list.isEmpty())
        {
            if (list.get(0) instanceof  Byte)
            {
                int i = 0;
                //noinspection unchecked
                for (Byte is: (List<Byte>)list)
                {
                    config.set("i" + i, is);
                    i++;
                }
                return Base64.getEncoder().encodeToString(config.saveToString().getBytes(StandardCharsets.UTF_8));
            }
            if (list.get(0) instanceof  Long)
            {
                int i = 0;
                for (Long is: (List<Long>)list)
                {
                    config.set("i" + i, is);
                    i++;
                }
                return Base64.getEncoder().encodeToString(config.saveToString().getBytes(StandardCharsets.UTF_8));
            }
            if (list.get(0) instanceof  Integer)
            {
                int i = 0;
                //noinspection unchecked
                for (Integer is: (List<Integer>)list)
                {
                    config.set("i" + i, is);
                    i++;
                }
                return Base64.getEncoder().encodeToString(config.saveToString().getBytes(StandardCharsets.UTF_8));
            }
            if (list.get(0) instanceof  ItemStack)
            {
                int i = 0;
                //noinspection unchecked
                for (ItemStack is: (List<ItemStack>)list)
                {
                    config.set("i" + i, is);
                    i++;
                }
                return Base64.getEncoder().encodeToString(config.saveToString().getBytes(StandardCharsets.UTF_8));
            }
            if (list.get(0) instanceof  Location)
            {
                int i = 0;
                //noinspection unchecked
                for (Location is: (List<Location>)list)
                {
                    config.set("i" + i, is.clone());
                    i++;
                }
                return Base64.getEncoder().encodeToString(config.saveToString().getBytes(StandardCharsets.UTF_8));
            }
            if (list.get(0) instanceof  UUID)
            {
                int i = 0;
                //noinspection unchecked
                for (UUID is: (List<UUID>)list)
                {
                    config.set("i" + i, is.toString());
                    i++;
                }
                return Base64.getEncoder().encodeToString(config.saveToString().getBytes(StandardCharsets.UTF_8));
            }
            if (list.get(0) instanceof  EntityType)
            {
                int i = 0;
                //noinspection unchecked
                for (EntityType is: (List<EntityType>)list)
                {
                    config.set("i" + i, is.name());
                    i++;
                }
                return Base64.getEncoder().encodeToString(config.saveToString().getBytes(StandardCharsets.UTF_8));
            }


        }
        config.set("i", list);
        return Base64.getEncoder().encodeToString(config.saveToString().getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(YamlConfiguration config)
    {
        if (config == null) return null;
        return Base64.getEncoder().encodeToString(config.saveToString().getBytes(StandardCharsets.UTF_8));
    }

    public static YamlConfiguration decodeYaml(String data)
    {
        if (data == null || data.isEmpty()) return null;
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(Base64.getDecoder().decode(data), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        return config;
    }
}
