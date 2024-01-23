package com.firesoftitan.play.titanbox.libs.tools;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class LibsSkullTool {
    private final Tools parent;

    public LibsSkullTool(Tools parent) {
        this.parent = parent;
    }

    public Tools getParent() {
        return parent;
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

            // Create the PlayerProfile using UUID and name
            PlayerProfile profile = getPlayerProfile(texture, TitanID, placeable);
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            if (meta != null) {
                meta.setOwnerProfile(profile);
            }
            head.setItemMeta(meta);
            return head;

        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return null;
    }

    private static String convertBase64ToURL(String texture) {
        String jsonString = new String(Base64.getDecoder().decode(texture), StandardCharsets.UTF_8);

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject jsTextures = jsonObject.getJSONObject("textures");
        JSONObject skin = jsTextures.getJSONObject("SKIN");
        return skin.getString("url");
    }

    public String getSkullTexture(Block block) {
        NBTTagCompound nbtTagCompound =  Tools.tools.getNBTTool().getNBT(block);
        if (nbtTagCompound == null) return null;
        return nbtTagCompound.p("SkullOwner").p("Properties").c("textures", 10).a(0).l("Value");
    }

    private GameProfile getProfile(String skullTexture, String name, boolean placeable) {
        GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes(skullTexture.getBytes()), "TitanCore:" + name + ":" + placeable);
        profile.getProperties().put("textures", new Property("textures", skullTexture));
        return profile;
    }
    private PlayerProfile getPlayerProfile(String texture, String TitanID, boolean placeable)
    {
        try {
            PlayerProfile profile =  Bukkit.createPlayerProfile(UUID.nameUUIDFromBytes(texture.getBytes()), "TitanCore:" + TitanID + ":" + placeable);
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            PlayerTextures textures = profile.getTextures();
            String jsurl = convertBase64ToURL(texture);
            URL url = new URL(jsurl);
            textures.setSkin(url);
            profile.setTextures(textures);
            return profile;
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Deprecated
    public void placeSkull(Block block, String skullTexture) {
        placeSkull(block, skullTexture, "");
    }
    public void placeSkull(Block block, ItemStack itemStack) {
        try {
            String skullTexture = getSkullTexture(itemStack);
            String skullTitanID = getSkullTitanID(itemStack);
            if (skullTitanID == null || skullTitanID.isEmpty() || skullTitanID.isBlank())
            {
                skullTitanID = TitanBoxLibs.tools.getItemStackTool().getTitanItemID(itemStack);
            }
            placeSkull(block, skullTexture, skullTitanID);
        } catch (Exception ignored) {

        }

    }
    public void placeSkull(Block block, String skullTexture, String TitanID) {
        try {
            if (block.getType() != Material.PLAYER_HEAD) {
                block.setType(Material.PLAYER_HEAD, false);
            }
            PlayerProfile profile = getPlayerProfile(skullTexture, TitanID, true);
            block.setType(Material.PLAYER_HEAD);
            Skull skull = (Skull) block.getState();
            skull.setOwnerProfile(profile);
            skull.update(false);
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
