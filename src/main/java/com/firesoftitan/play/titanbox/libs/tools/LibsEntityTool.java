package com.firesoftitan.play.titanbox.libs.tools;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.arguments.ArgumentEntity;
import net.minecraft.commands.arguments.ArgumentNBTTag;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.block.entity.TileEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LibsEntityTool {
    private final Tools parent;

    public LibsEntityTool(Tools parent) {
        this.parent = parent;
    }
    public List<Entity> findEntities(Location location, int range, String... tags)
    {
        return findEntities(null, location, range, tags);
    }

    public List<Entity> findEntities(EntityType entityType, Location location, int range)
    {
        return findEntities(entityType, location, range, (String) null);
    }
    public List<Entity> findEntities(String... tags)
    {
        return findEntities(null, null, -1, tags);
    }
    public List<Entity> findEntities(EntityType entityType, String... tags)
    {
        return findEntities(entityType, null, -1, tags);
    }
    public List<Entity> findEntities(EntityType entityType)
    {
        return findEntities(entityType, null, -1, (String) null);
    }
    public List<Entity> findEntities(EntityType entityType,Location location, int range, String... tags)
    {

        List<Entity> outage = new ArrayList<Entity>();
        String s = "";
        if (tags != null && tags.length > 0) {
            s = "tag=";
            for (String tag : tags) {
                s = s + tag + ",";
            }
            s = s.substring(0, s.length() - 1);
        }
        String type = "";
        if (entityType != null)
        {
            type = "type=" + entityType.getKey();
            if (!s.isEmpty()) type = type + ",";
        }
        ArgumentEntity argumentEntity = ArgumentEntity.b();
        try {
            EntitySelector parse = argumentEntity.parse(new StringReader("@e[" + type + "" + s + "]"));
            List<? extends net.minecraft.world.entity.Entity> entities = parse.b(((CraftServer) TitanBoxLibs.instants.getServer()).getHandle().c().aF()); //MinecraftServer.CommandListenerWrapper
            for(net.minecraft.world.entity.Entity e: entities)
            {
                CraftEntity bukkitEntity = e.getBukkitEntity();
                Location entityLocation = bukkitEntity.getLocation();
                if (location == null || (entityLocation.getWorld() == location.getWorld() && entityLocation.distance(location) <= range)) outage.add(bukkitEntity);
            }
            return outage;
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void setTag(Entity entity, String... tags)
    {
        List<String> lore = new ArrayList<>(Arrays.asList(tags));
        setTag(entity,lore);
    }

    public void setTag(Entity entity, List<String> tags)
    {
        net.minecraft.world.entity.Entity handle = ((CraftEntity) entity).getHandle();
        for(String s: tags)
        {
            handle.a(s);
        }
    }
    public List<String> getTag(Entity entity, String tag)
    {
        if (entity == null) return new ArrayList<String>();
        List<String> tags = TitanBoxLibs.tools.getNBTTool().getListString(entity, "Tags");
        if (tags == null) tags = new ArrayList<String>();
        return tags;
    }
    public boolean hasTag(Entity entity, String tag)
    {
        List<String> tags = getTag(entity, tag);
        for (String string : tags) {
            if (string.equals(tag)) return true;
        }
        return false;
    }
    private ParseResults<CommandListenerWrapper> parseCommand(String s) {
        DedicatedPlayerList server = ((CraftServer) TitanBoxLibs.instants.getServer()).getHandle();
        com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> commandDispatcher = server.b().vanillaCommandDispatcher.a();
        CommandListenerWrapper commandlistenerwrapper = ((CraftServer) TitanBoxLibs.instants.getServer()).getHandle().c().aF();
        return commandDispatcher.parse(s, commandlistenerwrapper);
    }
    public void summonEntity(World world, Entity entity)
    {
        WorldServer worldServer = ((CraftWorld) world).getHandle();
        worldServer.tryAddFreshEntityWithPassengers(((CraftEntity)entity).getHandle(), CreatureSpawnEvent.SpawnReason.COMMAND);
    }
    public String getNBTasString(Entity entity)
    {
        return "" + ((CraftEntity)entity).getHandle().f(new NBTTagCompound());
    }

    public Entity summonEntity(World world, EntityType type, String nbtString, Location location)
    {
        try {
            WorldServer worldServer = ((CraftWorld) world).getHandle();
            nbtString = "{Pos:[" + location.getX() + "d," + location.getY() + "d," + location.getZ() + "d],Rotation:[" + location.getYaw() + "f," + location.getPitch() + "f]," + nbtString.substring(1);

            var commandListenerWrapperParseResults = parseCommand("summon " + type.name().toLowerCase() + " " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + " " + nbtString);
            String string = commandListenerWrapperParseResults.getReader().getString();
            var build = commandListenerWrapperParseResults.getContext().build(string);
            NBTTagCompound nbt = ArgumentNBTTag.a(build, "nbt");
            nbt.a("id", type.getKey().toString()); //ResourceKey.MinecraftKey.toString()
            net.minecraft.world.entity.Entity entity = EntityTypes.a(nbt, worldServer, (entity1) -> entity1);
            worldServer.tryAddFreshEntityWithPassengers(entity, CreatureSpawnEvent.SpawnReason.COMMAND);
            if (entity == null) return null;
            return entity.getBukkitEntity();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public Entity summonEntity(World world, EntityType type, String nbtString)
    {
        try {
            WorldServer worldServer = ((CraftWorld) world).getHandle();
            var commandListenerWrapperParseResults = parseCommand("summon " + type.name().toLowerCase() + " 1 100 1 " + nbtString);
            String string = commandListenerWrapperParseResults.getReader().getString();
            var build = commandListenerWrapperParseResults.getContext().build(string);
            NBTTagCompound nbt = ArgumentNBTTag.a(build, "nbt");
            nbt.a("id", type.getKey().toString()); //ResourceKey.MinecraftKey.toString()
            net.minecraft.world.entity.Entity entity = EntityTypes.a(nbt, worldServer, (entity1) -> entity1);
            worldServer.tryAddFreshEntityWithPassengers(entity, CreatureSpawnEvent.SpawnReason.COMMAND);
            if (entity == null) return null;
            return entity.getBukkitEntity();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean isTileEntity(Block block)
    {
        WorldServer w = ((CraftWorld) block.getWorld()).getHandle();
        NBTTagCompound nbt = new NBTTagCompound();
        TileEntity tile = w.getBlockEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()), true);
        return tile != null;
    }
}

