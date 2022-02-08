package src.com.firesoftitan.play.titanbox.libs.tools;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.level.block.entity.TileEntitySkull;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class LibsSkullTool {
    private LibsSkullTool()
    {

    }
    public static String getSkullTexture(ItemStack item)
    {
        try {
            if (LibsItemStackTool.isEmpty(item)) return null;
            if (!(item.getItemMeta() instanceof SkullMeta)) {
                return null;
            }
            NBTTagCompound tag = LibsNBTTool.getNBTTag(item);

            return tag.p("SkullOwner").p("Properties").c("textures", 10).a(0).l("Value");
        } catch (Exception e) {
            return null;
        }
    }
    public static String getSkullOwner(Block block)
    {
        try {
            if (block.getType() != Material.PLAYER_HEAD  && block.getType() != Material.PLAYER_WALL_HEAD) return null;
            NBTTagCompound tag = LibsNBTTool.getNBTTag(block);
            if (tag == null) return null;
            NBTTagCompound skullOwner = tag.p("SkullOwner");
            if (skullOwner == null) return null;
            return skullOwner.l("Name");
        } catch (Exception e) {
            return null;
        }
    }
    public static String getSkullOwner(ItemStack item)
    {
        try {
            if (LibsItemStackTool.isEmpty(item)) return null;
            if (!(item.getItemMeta() instanceof SkullMeta)) {
                return null;
            }
            NBTTagCompound tag = LibsNBTTool.getNBTTag(item);

            return tag.p("SkullOwner").l("Name");
        } catch (Exception e) {
            return null;
        }
    }
    public static String getSkullID(ItemStack item)
    {
        try {
            if (LibsItemStackTool.isEmpty(item)) return null;
            if (!(item.getItemMeta() instanceof SkullMeta)) {
                return null;
            }
            NBTTagCompound tag = LibsNBTTool.getNBTTag(item);

            return tag.p("SkullOwner").l("Id");
        } catch (Exception e) {
            return null;
        }
    }
    public static ItemStack getSkull(String texture) {
        return getSkull(texture, "", true);
    }

    public static ItemStack getSkull(String texture, String TitanID) {
        return getSkull(texture, TitanID, true);
    }

    public static ItemStack getSkull(String texture, String TitanID, boolean placeable) {
        try {
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);

            ItemMeta headMeta = head.getItemMeta();

            GameProfile profile = getProfile(texture, TitanID, placeable);
            Field profileField = null;
            try {
                //noinspection ConstantConditions
                profileField = headMeta.getClass().getDeclaredField("profile");
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
            if (profileField == null) return null;
            profileField.setAccessible(true);
            try {
                profileField.set(headMeta, profile);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            head.setItemMeta(headMeta);
            return head;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSkullTexture(Block block) {
        NBTTagCompound nbtTagCompound = LibsNBTTool.getNBTTag(block);
        if (nbtTagCompound == null) return null;
        return nbtTagCompound.p("SkullOwner").p("Properties").c("textures", 10).a(0).l("Value");
    }

    private static GameProfile getProfile(String skulltexture, String name, boolean placeable) {
        GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes(skulltexture.getBytes()), "TitanCore:" + name + ":" + placeable);
        profile.getProperties().put("textures", new Property("textures", skulltexture));
        return profile;
    }

    @Deprecated
    public static void placeSkull(Block block, String skulltexture) {
        placeSkull(block, skulltexture, "");
    }

    public static void placeSkull(Block block, String skulltexture, String TitanID) {
        try {
            if (block.getType() != Material.PLAYER_HEAD) {
                block.setType(Material.PLAYER_HEAD, false);
            }
            GameProfile profile = getProfile(skulltexture, TitanID, true);
            TileEntitySkull skullTile = (TileEntitySkull) ((CraftWorld) block.getWorld()).getHandle().c_(new BlockPosition(block.getX(), block.getY(), block.getZ()));
            if (skullTile == null) return;
            skullTile.a(profile);
            block.getState().update(true);


        } catch (Exception ignored) {

        }

    }

    public static boolean isSkullPlaceable(ItemStack itemStack) {
        if (!LibsItemStackTool.isEmpty(itemStack)) {
            if (itemStack.getType() == Material.PLAYER_HEAD || itemStack.getType() == Material.PLAYER_WALL_HEAD) {
                String owner = LibsSkullTool.getSkullOwner(itemStack);
                if (owner == null) return true;
                if (owner.startsWith("TitanCore:")) {
                    String[] spliter = owner.split(":");
                    if (spliter.length > 2) {
                        return Boolean.parseBoolean(spliter[2]);
                    }
                }
            }
        }
        return true;
    }

    public static String getSkullTitanID(ItemStack itemStack) {
        if (!LibsItemStackTool.isEmpty(itemStack)) {
            if (itemStack.getType() == Material.PLAYER_HEAD || itemStack.getType() == Material.PLAYER_WALL_HEAD) {
                String owner = LibsSkullTool.getSkullOwner(itemStack);
                if (owner != null && owner.startsWith("TitanCore:")) {
                    String[] spliter = owner.split(":");
                    if (spliter.length > 1) {
                        return spliter[1];
                    }
                }
            }
        }
        return null;
    }

    public static String getSkullTitanID(Block block) {
        if (block != null) {
            if (block.getType() == Material.PLAYER_HEAD || block.getType() == Material.PLAYER_WALL_HEAD) {
                String owner = LibsSkullTool.getSkullOwner(block);
                if (owner == null) return null;
                if (owner.startsWith("TitanCore:")) {
                    String[] spliter = owner.split(":");
                    if (spliter.length > 1) {
                        return spliter[1];
                    }
                }
            }
        }
        return null;
    }

}
