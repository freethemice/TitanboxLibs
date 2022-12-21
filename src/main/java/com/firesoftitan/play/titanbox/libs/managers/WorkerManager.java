package com.firesoftitan.play.titanbox.libs.managers;

//import com.mojang.authlib.GameProfile;
import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.tools.Tools;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.EnumGamemode;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_19_R2.CraftServer;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class WorkerManager {
    private SaveManager npcSaves = new SaveManager("npc");
    private HashMap<String, EntityPlayer> npcWorkers = new HashMap<String, EntityPlayer>();

    public WorkerManager() {

    }

    private void createWorker()
    {
        for (World world: Bukkit.getWorlds())
        {
            addWorker(world);
        }
    }
    public void hideWorker(Player player)
    {
        for(String key: npcWorkers.keySet())
        {
            EntityPlayer worker = npcWorkers.get(key);
            ((CraftPlayer)player).getHandle().b.a(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.a.e  ,worker)); //ADD_PLAYER
        }

    }
    private void addWorker(World world) {
        addWorker(world, UUID.randomUUID());
    }
    private void addWorker(World world, UUID uuid) {
        if (world != null && uuid != null) {
            MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
            WorldServer nmsWorld = ((CraftWorld) world).getHandle();
            GameProfile gameProfile = new GameProfile(uuid, "TB_NPC_" + world.getName());
            EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile);
            npc.a(world.getSpawnLocation().getX(), world.getSpawnLocation().getY(), world.getSpawnLocation().getZ(), 0, 0);
            CraftPlayer opCr = npc.getBukkitEntity();
            opCr.setGameMode(GameMode.SURVIVAL);
            opCr.getPlayer().setGameMode(GameMode.SURVIVAL);
            opCr.getHandle().d.a(EnumGamemode.a);
            opCr.setOp(true);

            npcWorkers.put(world.getName(), npc);
        }
    }

    public void saveAll()
    {
        for(String key: npcWorkers.keySet())
        {
            EntityPlayer worker = npcWorkers.get(key);
            npcSaves.set("worker." + key + ".uuid", worker.cm());
        }
        npcSaves.save();
    }
    public void loadAll()
    {
        for(String key: npcSaves.getKeys("worker"))
        {
            World world = Bukkit.getWorld(key);
            UUID uuid = npcSaves.getUUID("worker." + key + ".uuid");
            addWorker(world, uuid);
        }
        if (npcWorkers.size() == 0 ) createWorker();
    }

    public ItemStack pickupItem(UUID owner, Location checkSapling, Material mat, int size) {
        try {
            List<Entity> listnear = this.getNearbyEntities(checkSapling, size);
            for (Entity e : listnear) {
                if (!e.isDead()) {
                    if (e.getType() == EntityType.DROPPED_ITEM) { ;
                        ItemStack dropped = ((Item) e).getItemStack().clone();
                        String check =  Tools.getItemStackTool(TitanBoxLibs.instants).getName(dropped,true);
                        if (check.toLowerCase().startsWith("altar probe")) return null; //slimefun item ignore it
                        if (mat == null)
                        {
                            e.remove();
                            return dropped.clone();
                        }
                        else {
                            if (dropped.getType() == mat) {
                                e.remove();
                                return dropped.clone();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {

        }
        return null;
    }
    public List<Entity> getNearbyEntities(Location loc)
    {
        return getNearbyEntities(loc, 5);
    }
    public List<Entity>  getNearbyEntities(Location loc, int rad)
    {
        EntityPlayer npc = npcWorkers.get(loc.getWorld().getName());
        if (npc == null) return null;
        npc.a(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        CraftPlayer opCr = npc.getBukkitEntity();
        List<Entity> nearEntity = opCr.getNearbyEntities(rad,rad,rad);
        return nearEntity;
    }

    public EntityPlayer getServerWorker(Location loc)
    {
        EntityPlayer npc = npcWorkers.get(loc.getWorld().getName());
        if (npc == null)
        {
            addWorker(loc.getWorld());
            npc = npcWorkers.get(loc.getWorld().getName());
        }
        if (npc == null) return null;
        npc.a(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        return npc;
    }
    public CraftPlayer getCraftWorker(Location loc)
    {
        EntityPlayer npc = npcWorkers.get(loc.getWorld().getName());
        if (npc == null)
        {
            addWorker(loc.getWorld());
            npc = npcWorkers.get(loc.getWorld().getName());
        }
        if (npc == null) return null;
        npc.a(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        CraftPlayer opCr = npc.getBukkitEntity();
        return opCr;
    }
    public boolean isWorker(Player player)
    {
        return isWorker(player.getUniqueId());
    }
    public  boolean isWorker(UUID uuid)
    {
        for(EntityPlayer entityPlayer: npcWorkers.values())
        {
            if (uuid.equals(entityPlayer.cm()))
            {
                return true;
            }
        }
        return false;
    }
    public void sendCommand(Location loc, String command)
    {
        EntityPlayer npc = npcWorkers.get(loc.getWorld().getName());
        if (npc == null) return;
        npc.a(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        CraftPlayer opCr = npc.getBukkitEntity();
        Bukkit.dispatchCommand(opCr, command);
    }
}
