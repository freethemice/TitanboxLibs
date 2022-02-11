package com.firesoftitan.play.titanbox.libs.tools;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.block.entity.TileEntity;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LibsNBTTool {
    private Tools parent;

    public LibsNBTTool(Tools parent) {
        this.parent = parent;
    }

    public List<String> getNBTKeyTree(NBTTagCompound nbtTagCompound) {
        return getNBTKeyTree(nbtTagCompound, new ArrayList<>(), "");
    }
    private List<String> getNBTKeyTree(NBTTagCompound nbtTagCompound, List<String> pass, String pre) {
        for(String key: nbtTagCompound.d())
        {
            NBTTagCompound compound = nbtTagCompound.p(key);
            if (pre.length() < 1) {
                pass.add(key);
                if (compound != null) getNBTKeyTree(compound, pass, key);
            } else {
                pass.add(pre + "." + key);
                if (compound != null) getNBTKeyTree(compound, pass, pre + "." + key);
            }
        }
        return pass;
    }
    public boolean hasNBTTag(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = getNBTTag(itemStack);
        if (nbtTagCompound != null) {
            return nbtTagCompound.e(key);
        }
        return false;
    }
    public ItemStack removeNBTTag(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound =  TitanBoxLibs.tools.getNBTTool().getNBTTag(itemStack);
        if (nbtTagCompound != null) {
            if (nbtTagCompound.e(key)) {
                if (nbtTagCompound.d().size() == 1) {
                    return clearNBTTag(itemStack);
                } else {
                    nbtTagCompound.a(key, (String)null);
                    return setNBTTag(itemStack, nbtTagCompound);
                }
            }
        }
        return itemStack;
    }
    public ItemStack clearNBTTag(ItemStack itemStack)
    {
        try {

            net.minecraft.world.item.ItemStack itemStack1 = CraftItemStack.asNMSCopy(itemStack);
            itemStack1.c((NBTTagCompound) null);
            return CraftItemStack.asBukkitCopy(itemStack1);
        }
        catch (Exception E)
        {
            E.printStackTrace();
            return null;
        }
    }
    public ItemStack setNBTTag(ItemStack itemStack, NBTTagCompound nbtTagCompound)
    {
        try {
            net.minecraft.world.item.ItemStack itemStack1 = CraftItemStack.asNMSCopy(itemStack);

            itemStack1.c(nbtTagCompound);
            return CraftItemStack.asBukkitCopy(itemStack1);
        }
        catch (Exception E)
        {
            E.printStackTrace();
            return null;
        }
    }

    public NBTTagCompound getNBTTag(Block block)
    {

        WorldServer w = ((CraftWorld) block.getWorld()).getHandle();
        NBTTagCompound nbt = new NBTTagCompound();
        TileEntity tile = w.c_(new BlockPosition(block.getX(), block.getY(), block.getZ()));
        if (tile == null) return null;
        tile.a(nbt);
        return nbt;
    }
    public NBTTagCompound getNBTTag(ItemStack itemStack)
    {
        try {

            net.minecraft.world.item.ItemStack itemStack1 = CraftItemStack.asNMSCopy(itemStack);
            if (itemStack1.s() == null)
            {
                return new NBTTagCompound();
            }
            return itemStack1.s();
        }
        catch (Exception E)
        {
            return new NBTTagCompound();
        }
    }



    public List<String> getKeys(NBTTagCompound nbtTagCompound)
    {
        List<String> data = new ArrayList<>();
        getKeys(nbtTagCompound, "", data);
        Collections.sort(data);
        return data;
    }
    private void getKeys(NBTTagCompound nbtTagCompound, String key, List<String> data)
    {
        for(String s: nbtTagCompound.d())
        {
            String pass = s;
            if (key.length() > 1) {
                pass = key + "." + s;
            }
            if (nbtTagCompound.p(s) != null && nbtTagCompound.p(s).toString().length() > 2)
            {
                getKeys(nbtTagCompound.p(s), pass, data);
            }
            else
            {

                String S = "" + nbtTagCompound.l(s);

                data.add(pass + "=" + S);


            }
        }
    }
}
