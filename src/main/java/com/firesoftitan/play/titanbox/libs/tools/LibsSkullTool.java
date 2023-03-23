package com.firesoftitan.play.titanbox.libs.tools;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.level.block.entity.TileEntitySkull;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class LibsSkullTool {
    private Tools parent;

    public LibsSkullTool(Tools parent) {
        this.parent = parent;
    }

    public String getSkullTexture(ItemStack item)
    {
        try {
            if ( Tools.tools.getItemStackTool().isEmpty(item)) return null;
            if (!(item.getItemMeta() instanceof SkullMeta)) {
                return null;
            }
            NBTTagCompound tag =  Tools.tools.getNBTTool().getNBT(item);

            return tag.p("SkullOwner").p("Properties").c("textures", 10).a(0).l("Value");
        } catch (Exception e) {
            return null;
        }
    }
    public String getSkullOwner(Block block)
    {
        try {
            if (block.getType() != Material.PLAYER_HEAD  && block.getType() != Material.PLAYER_WALL_HEAD) return null;
            NBTTagCompound tag =  Tools.tools.getNBTTool().getNBT(block);
            if (tag == null) return null;
            NBTTagCompound skullOwner = tag.p("SkullOwner");
            if (skullOwner == null) return null;
            return skullOwner.l("Name");
        } catch (Exception e) {
            return null;
        }
    }
    public String getSkullOwner(ItemStack item)
    {
        try {
            if ( Tools.tools.getItemStackTool().isEmpty(item)) return null;
            if (!(item.getItemMeta() instanceof SkullMeta)) {
                return null;
            }
            NBTTagCompound tag =  Tools.tools.getNBTTool().getNBT(item);

            return tag.p("SkullOwner").l("Name");
        } catch (Exception e) {
            return null;
        }
    }
    public String getSkullID(ItemStack item)
    {
        try {
            if ( Tools.tools.getItemStackTool().isEmpty(item)) return null;
            if (!(item.getItemMeta() instanceof SkullMeta)) {
                return null;
            }
            NBTTagCompound tag =  Tools.tools.getNBTTool().getNBT(item);

            return tag.p("SkullOwner").l("Id");
        } catch (Exception e) {
            return null;
        }
    }
    public ItemStack getSkull(String texture) {
        return getSkull(texture, "", true);
    }

    public ItemStack getSkull(String texture, String TitanID) {
        return getSkull(texture, TitanID, true);
    }

    public ItemStack getSkull(String texture, String TitanID, boolean placeable) {
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

    public String getSkullTexture(Block block) {
        NBTTagCompound nbtTagCompound =  Tools.tools.getNBTTool().getNBT(block);
        if (nbtTagCompound == null) return null;
        return nbtTagCompound.p("SkullOwner").p("Properties").c("textures", 10).a(0).l("Value");
    }

    private GameProfile getProfile(String skulltexture, String name, boolean placeable) {
        GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes(skulltexture.getBytes()), "TitanCore:" + name + ":" + placeable);
        profile.getProperties().put("textures", new Property("textures", skulltexture));
        return profile;
    }

    @Deprecated
    public void placeSkull(Block block, String skulltexture) {
        placeSkull(block, skulltexture, "");
    }

    public void placeSkull(Block block, String skulltexture, String TitanID) {
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

    public boolean isSkullPlaceable(ItemStack itemStack) {
        if (! Tools.tools.getItemStackTool().isEmpty(itemStack)) {
            if (itemStack.getType() == Material.PLAYER_HEAD || itemStack.getType() == Material.PLAYER_WALL_HEAD) {
                String owner =  Tools.tools.getSkullTool().getSkullOwner(itemStack);
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

    public String getSkullTitanID(ItemStack itemStack) {
        if (! Tools.tools.getItemStackTool().isEmpty(itemStack)) {
            if (itemStack.getType() == Material.PLAYER_HEAD || itemStack.getType() == Material.PLAYER_WALL_HEAD) {
                String owner =  Tools.tools.getSkullTool().getSkullOwner(itemStack);
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

    public String getSkullTitanID(Block block) {
        if (block != null) {
            if (block.getType() == Material.PLAYER_HEAD || block.getType() == Material.PLAYER_WALL_HEAD) {
                String owner =  Tools.tools.getSkullTool().getSkullOwner(block);
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
