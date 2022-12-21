package com.firesoftitan.play.titanbox.libs.tools;

import com.firesoftitan.play.titanbox.libs.managers.EncodeDecodeManager;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.block.entity.TileEntity;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class LibsNBTTool {
    private final Tools parent;

    public LibsNBTTool(Tools parent) {
        this.parent = parent;
    }

    public ItemStack set(ItemStack itemStack, String key, byte value)
    {
        if (key == null || itemStack == null) return itemStack.clone();
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        nbtTagCompound.a(key, value);
        ItemStack itemStack1= this.setNBT(itemStack, nbtTagCompound);
        return itemStack1.clone();
    }
    public ItemStack set(ItemStack itemStack, String key, short value)
    {
        if (key == null || itemStack == null) return itemStack.clone();
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        nbtTagCompound.a(key, value);
        ItemStack itemStack1= this.setNBT(itemStack, nbtTagCompound);
        return itemStack1.clone();
    }
    public ItemStack set(ItemStack itemStack, String key, int value)
    {
        if (key == null || itemStack == null) return itemStack.clone();
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        nbtTagCompound.a(key, value);
        ItemStack itemStack1= this.setNBT(itemStack, nbtTagCompound);
        return itemStack1.clone();
    }
    public ItemStack set(ItemStack itemStack, String key, long value)
    {
        if (key == null || itemStack == null) return itemStack.clone();
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        nbtTagCompound.a(key, value);
        ItemStack itemStack1= this.setNBT(itemStack, nbtTagCompound);
        return itemStack1.clone();
    }
    public ItemStack set(ItemStack itemStack, String key, UUID value)
    {
        if (value == null || key == null || itemStack == null) return itemStack.clone();
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        nbtTagCompound.a(key, value);
        ItemStack itemStack1= this.setNBT(itemStack, nbtTagCompound);
        return itemStack1.clone();
    }
    public ItemStack set(ItemStack itemStack, String key, float value)
    {
        if (key == null || itemStack == null) return itemStack.clone();
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        nbtTagCompound.a(key, value);
        ItemStack itemStack1= this.setNBT(itemStack, nbtTagCompound);
        return itemStack1.clone();
    }
    public ItemStack set(ItemStack itemStack, String key, double value)
    {
        if (key == null || itemStack == null) return itemStack.clone();
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        nbtTagCompound.a(key, value);
        ItemStack itemStack1= this.setNBT(itemStack, nbtTagCompound);
        return itemStack1.clone();
    }
    public ItemStack set(ItemStack itemStack, String key, String value)
    {
        if (value == null || key == null || itemStack == null) return itemStack.clone();
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        nbtTagCompound.a(key, value);
        ItemStack itemStack1= this.setNBT(itemStack, nbtTagCompound);
        return itemStack1.clone();
    }
    public ItemStack set(ItemStack itemStack, String key, Location value)
    {
        if (value == null || key == null || itemStack == null) return itemStack.clone();
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        nbtTagCompound.a(key, EncodeDecodeManager.encode(value));
        ItemStack itemStack1= this.setNBT(itemStack, nbtTagCompound);
        return itemStack1.clone();
    }
    public ItemStack set(ItemStack itemStack, String key, byte[] value)
    {
        if (value == null || key == null || itemStack == null) return itemStack.clone();
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        nbtTagCompound.a(key, value);
        ItemStack itemStack1= this.setNBT(itemStack, nbtTagCompound);
        return itemStack1.clone();
    }
    public ItemStack set(ItemStack itemStack, String key, int[] value)
    {
        if (value == null || key == null || itemStack == null) return itemStack.clone();
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        nbtTagCompound.a(key, value);
        ItemStack itemStack1= this.setNBT(itemStack, nbtTagCompound);
        return itemStack1.clone();
    }
    public ItemStack set(ItemStack itemStack, String key, long[] value)
    {
        if (value == null || key == null || itemStack == null) return itemStack.clone();
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        nbtTagCompound.a(key, value);
        ItemStack itemStack1= this.setNBT(itemStack, nbtTagCompound);
        return itemStack1.clone();
    }
    public ItemStack set(ItemStack itemStack, String key, ItemStack[] value)
    {
        List<ItemStack> itemStacks = new ArrayList<ItemStack>(List.of(value));
        return set(itemStack, key, itemStacks);
    }
    public ItemStack set(ItemStack itemStack, String key, Location[] value)
    {
        List<Location> itemStacks = new ArrayList<Location>(List.of(value));
        return set(itemStack, key, itemStacks);
    }
    public ItemStack set(ItemStack itemStack, String key, UUID[] value)
    {
        List<UUID> itemStacks = new ArrayList<UUID>(List.of(value));
        return set(itemStack, key, itemStacks);
    }
    public ItemStack set(ItemStack itemStack, String key, EntityType[] value)
    {
        List<EntityType> itemStacks = new ArrayList<EntityType>(List.of(value));
        return set(itemStack, key, itemStacks);
    }
    public ItemStack set(ItemStack itemStack, String key, boolean value)
    {
        if (key == null || itemStack == null) return itemStack.clone();
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        nbtTagCompound.a(key, value);
        ItemStack itemStack1= this.setNBT(itemStack, nbtTagCompound);
        return itemStack1.clone();
    }
/*    public ItemStack setBase(ItemStack itemStack, String key, NBTBase value)
    {
        if (value == null || key == null || itemStack == null) return itemStack.clone();
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        nbtTagCompound.a(key, value);
        ItemStack itemStack1= this.setNBT(itemStack, nbtTagCompound);
        return itemStack1.clone();
    }*/

    /**
     * supported {@link List<Integer>}
     * supported {@link List<Byte>}
     * supported {@link List<Long>}
     * supported {@link List<String>}
     * supported {@link List<ItemStack>}
     * supported {@link List<Location>}
     * supported {@link List<UUID>}
     * supported {@link List<EntityType>}
     * @param value {@link List} to save
     * @return ItemStack
     */
    public ItemStack set(ItemStack itemStack, String key, List value)
    {
        if (value == null || key == null || itemStack == null) return itemStack.clone();
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        nbtTagCompound.a(key, EncodeDecodeManager.encode(value));
        ItemStack itemStack1= this.setNBT(itemStack, nbtTagCompound);
        return itemStack1.clone();
    }
    public byte getByte(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return nbtTagCompound.d(key);
    }
    public short getShort(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return nbtTagCompound.g(key);
    }
    public Integer getInteger(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return nbtTagCompound.h(key);
    }
    public Long getLong(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return nbtTagCompound.i(key);
    }
    public UUID getUUID(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return nbtTagCompound.a(key);
    }
    public float getFloat(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return nbtTagCompound.j(key);
    }
    public double getDouble(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return nbtTagCompound.k(key);
    }
    public String getString(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return nbtTagCompound.l(key);
    }
    public byte[] getArrayByte(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return nbtTagCompound.m(key);
    }
    public int[] getArrayInteger(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return nbtTagCompound.n(key);
    }
    public long[] getArrayLong(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return nbtTagCompound.o(key);
    }
    public ItemStack[] getArrayItemStack(ItemStack itemStack, String key)
    {
        List<ItemStack> list = this.getListItemStack(itemStack, key);
        return Arrays.copyOf(list.toArray(), list.size(), ItemStack[].class);
    }
    public Location[] getArrayLocation(ItemStack itemStack, String key)
    {
        List<Location> list = this.getListLocation(itemStack, key);
        return Arrays.copyOf(list.toArray(), list.size(), Location[].class);
    }
    public UUID[] getArrayUUID(ItemStack itemStack, String key)
    {
        List<UUID> list = this.getListUUID(itemStack, key);
        return Arrays.copyOf(list.toArray(), list.size(), UUID[].class);
    }
    public EntityType[] getArrayEntityType(ItemStack itemStack, String key)
    {
        List<EntityType> list = this.getListEntityType(itemStack, key);
        return Arrays.copyOf(list.toArray(), list.size(), EntityType[].class);
    }
    public boolean getBoolean(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return nbtTagCompound.q(key);
    }
    public Location getLocation(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return EncodeDecodeManager.decodeLocation(nbtTagCompound.l(key));
    }
    public boolean containsKey(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = getNBT(itemStack);
        if (nbtTagCompound != null) {
            return nbtTagCompound.e(key);
        }
        return false;
    }
    public List<Byte> getListByte(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return EncodeDecodeManager.decodeByteList(nbtTagCompound.l(key));
    }
    public List<Long> getListLong(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return EncodeDecodeManager.decodeLongList(nbtTagCompound.l(key));
    }
    public List<Integer> getListInteger(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return EncodeDecodeManager.decodeIntList(nbtTagCompound.l(key));
    }
    public List<String> getListString(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return EncodeDecodeManager.decodeStringList(nbtTagCompound.l(key));
    }
    public List<ItemStack> getListItemStack(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return EncodeDecodeManager.decodeItemList(nbtTagCompound.l(key));
    }
    public List<UUID> getListUUID(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return EncodeDecodeManager.decodeUUIDList(nbtTagCompound.l(key));
    }
    public List<Location> getListLocation(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return EncodeDecodeManager.decodeLocationList(nbtTagCompound.l(key));
    }
    public List<EntityType> getListEntityType(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = this.getNBT(itemStack);
        return EncodeDecodeManager.decodeEntityTypeList(nbtTagCompound.l(key));
    }
    protected List<String> getNBTKeyTree(NBTTagCompound nbtTagCompound) {
        return getNBTKeyTree(nbtTagCompound, new ArrayList<>(), "");
    }
    protected List<String> getNBTKeyTree(NBTTagCompound nbtTagCompound, List<String> pass, String pre) {
        for(String key: nbtTagCompound.e())
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
    public ItemStack removeKey(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound =  Tools.tools.getNBTTool().getNBT(itemStack);
        if (nbtTagCompound != null) {
            if (nbtTagCompound.e(key)) {
                if (nbtTagCompound.e().size() == 1) {
                    return clear(itemStack);
                } else {
                    nbtTagCompound.a(key, (String)null);
                    return setNBT(itemStack, nbtTagCompound);
                }
            }
        }
        return itemStack;
    }
    public ItemStack clear(ItemStack itemStack)
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
    protected ItemStack setNBT(ItemStack itemStack, NBTTagCompound nbtTagCompound)
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
    protected NBTTagCompound getNBT(Block block)
    {

        WorldServer w = ((CraftWorld) block.getWorld()).getHandle();
        NBTTagCompound nbt = new NBTTagCompound();
        TileEntity tile = w.c_(new BlockPosition(block.getX(), block.getY(), block.getZ()));
        if (tile == null) return null;
        tile.a(nbt);
        return nbt;
    }
    protected NBTTagCompound getNBT(ItemStack itemStack)
    {
        try {

            net.minecraft.world.item.ItemStack itemStack1 = CraftItemStack.asNMSCopy(itemStack);
            if (itemStack1.u() == null)
            {
                return new NBTTagCompound();
            }
            return itemStack1.u() ;
        }
        catch (Exception E)
        {
            return new NBTTagCompound();
        }
    }

    public List<String> getKeysItemStack(ItemStack itemStackA)
    {
        NBTTagCompound compound =  this.getNBT(itemStackA);
        List<String> data = new ArrayList<>();
        getKeys(compound, "", data);
        Collections.sort(data);
        return data;
    }

    protected List<String> getKeysNBT(NBTTagCompound nbtTagCompound)
    {
        List<String> data = new ArrayList<>();
        getKeys(nbtTagCompound, "", data);
        Collections.sort(data);
        return data;
    }
    protected void getKeys(NBTTagCompound nbtTagCompound, String key, List<String> data)
    {
        for(String s: nbtTagCompound.e())
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
