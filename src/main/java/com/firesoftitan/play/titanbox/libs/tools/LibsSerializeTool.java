package com.firesoftitan.play.titanbox.libs.tools;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class LibsSerializeTool {
    private Tools parent;

    public LibsSerializeTool(Tools parent) {
        this.parent = parent;
    }

    public String serializeLocation(Location l) {
        return  Tools.tools.getLocationTool().getWorldName(l) + ";" + l.getBlockX() + ";" + l.getBlockY() + ";" + l.getBlockZ();
    }
    public Location deserializeLocation(String l) {
        try {
            World w = Bukkit.getWorld(l.split(";")[0]);
            if (w != null) return new Location(w, Integer.parseInt(l.split(";")[1]), Integer.parseInt(l.split(";")[2]), Integer.parseInt(l.split(";")[3]));
        } catch (NumberFormatException ignored) {
        }
        return null;
    }
    public ItemStack deserializeItemStackSimple(String itemStack)
    {
        String[] parts = itemStack.split(";");
        ItemStack out;
        if (parts.length > 2)
        {
            out =  Tools.tools.getSkullTool().getSkull(parts[2]);
        }
        else {
            out = new ItemStack(Material.valueOf(parts[0]));
        }
        return out;

    }
    public String serializeItemStack(ItemStack itemStackA)
    {
        return serializeItemStack(itemStackA, false);
    }
    public String serializeItemStack(ItemStack itemStackA, boolean complete)
    {
        String xml10pattern = "[^"
                + "\u0009\r\n"
                + "\u0020-\uD7FF"
                + "\uE000-\uFFFD"
                + "\ud800\udc00-\udbff\udfff"
                + "]";
        String xml11pattern = "[^"
                + "\u0001-\uD7FF"
                + "\uE000-\uFFFD"
                + "\ud800\udc00-\udbff\udfff"
                + "]+";
        if (itemStackA == null) return null;
        itemStackA = itemStackA.clone();
        String key = itemStackA.getType().name() + ";";
        if (itemStackA.hasItemMeta()) {
            ItemMeta a = itemStackA.getItemMeta();
            if (a instanceof SkullMeta && !complete && itemStackA.getType() == Material.PLAYER_HEAD) {
                key = key + "SkullMeta" + ";";
                if ( Tools.tools.getSkullTool().getSkullTexture(itemStackA) != null) {
                    key = key +  Tools.tools.getSkullTool().getSkullTexture(itemStackA);
                }
            }
        }
        if (complete) {
            List<String> data =  Tools.tools.getNBTTool().getKeys(itemStackA); // always get right order
            for(String s: data)
            {
                //noinspection StringConcatenationInLoop
                key = key + s + ";";
            }
        }

        //remove all illegal chars from key
        key = key.replaceAll(xml10pattern, "");
        key = key.replaceAll(xml11pattern, "");
        key = key.replace("\u002E", "");//replace period
        //TitanBoxCore.sendMessageSystem(TitanBox.instants, key);
        return new String(key.getBytes(), StandardCharsets.UTF_8);
    }

}
