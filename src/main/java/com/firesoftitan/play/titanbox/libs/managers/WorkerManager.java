package com.firesoftitan.play.titanbox.libs.managers;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.tools.Tools;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EnumMainHand;
import net.minecraft.world.entity.player.EnumChatVisibility;
import net.minecraft.world.level.EnumGamemode;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class WorkerManager {
    private final SaveManager npcSaves = new SaveManager("npc");
    private final HashMap<String, EntityPlayer> npcWorkers = new HashMap<String, EntityPlayer>();

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
/*        int count = 0;
        for(String key: npcWorkers.keySet())
        {
            EntityPlayer worker = npcWorkers.get(key);
            //c = PlayerConnection
            if (((CraftPlayer)player).getHandle().c != null && worker != null) {
                try {
                    ((CraftPlayer) player).getHandle().c.a(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.a.e, worker)); //ADD_PLAYER
                    count++;
                } catch (Exception e) {
                    Tools.getMessageTool(TitanBoxLibs.instants).sendMessageSystem("Failed to load NPC Player");
                }
            }
        }
        Tools.getMessageTool(TitanBoxLibs.instants).sendMessageSystem("Loaded " + count + " NPC");*/

    }
    private void addWorker(World world, String name) {
        addWorker(world, UUID.randomUUID(), name);
    }
    private void addWorker(World world) {
        addWorker(world, UUID.randomUUID());
    }
    private void addWorker(World world, UUID uuid) {
        if (world == null) return;
        addWorker(world, uuid, "TB_NPC_" + world.getName());
    }
    private void addWorker(World world, UUID uuid, String name) {
        if (world != null && uuid != null) {
            EntityPlayer npc = getEntityPlayer(world, uuid, name);
            CraftPlayer opCr = npc.getBukkitEntity();
            opCr.setGameMode(GameMode.SURVIVAL);
            if (opCr.getPlayer() != null) opCr.getPlayer().setGameMode(GameMode.SURVIVAL);
            opCr.getHandle().d.a(EnumGamemode.a);
            opCr.setOp(true);
            //noinspection SpellCheckingInspection
            Bukkit.dispatchCommand(opCr, "ignoreclaims");
            npcWorkers.put(name, npc);
        }
    }

    @NotNull
    private static EntityPlayer getEntityPlayer(World world, UUID uuid, String name) {
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) world).getHandle();
        GameProfile gameProfile = new GameProfile(uuid, name);
        ClientInformation clientinformation = new ClientInformation("English", 0, EnumChatVisibility.c, true, 0, EnumMainHand.b, false, true);
        //ClientInformation is new for 1.20.2 and is untested.
        EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile, clientinformation);
        npc.a(world.getSpawnLocation().getX(), world.getSpawnLocation().getY(), world.getSpawnLocation().getZ(), 0, 0);
        return npc;
    }

    public void saveAll()
    {
        for(String key: npcWorkers.keySet())
        {
            EntityPlayer worker = npcWorkers.get(key);
            npcSaves.set("worker." + key + ".uuid", worker.getBukkitEntity().getUniqueId()); //worker.cm(), was this removed in 1.20.2????
        }
        npcSaves.save();
    }
    public void loadAll()
    {
        for(String key: npcSaves.getKeys("worker"))
        {
            World world = Bukkit.getWorld(key);
            if (world != null) {
                UUID uuid = npcSaves.getUUID("worker." + key + ".uuid");
                addWorker(world, uuid);
            }
        }
        if (npcWorkers.isEmpty()) createWorker();
    }

    public ItemStack pickupItem(UUID owner, Location checkSapling, Material mat, int size) {
        try {
            List<Entity> listNear = this.getNearbyEntities(checkSapling, size);
            for (Entity e : listNear) {
                if (!e.isDead()) {
                    if (e.getType() == EntityType.DROPPED_ITEM) {
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
        catch (Exception ignored)
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
        if (loc == null || loc.getWorld() == null) return new ArrayList<Entity>();
        EntityPlayer npc = npcWorkers.get("TB_NPC_" + loc.getWorld().getName());
        if (npc == null) return null;
        npc.a(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        CraftPlayer opCr = npc.getBukkitEntity();
        return opCr.getNearbyEntities(rad,rad,rad);
    }

/*    public EntityPlayer getServerWorker(Location loc)
    {
        EntityPlayer npc = npcWorkers.get("TB_NPC_" + loc.getWorld().getName());
        if (npc == null)
        {
            addWorker(loc.getWorld());
            npc = npcWorkers.get(loc.getWorld().getName());
        }
        if (npc == null) return null;
        npc.a(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        return npc;
    }*/
public Player getCraftWorker(String name, Location loc)
{
    if (loc == null || loc.getWorld() == null) return null;
    EntityPlayer npc = npcWorkers.get(name);
    if (npc == null)
    {
        addWorker(loc.getWorld(), name);
        npc = npcWorkers.get(name);
    }
    if (npc == null) return null;
    npc.a(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
    return npc.getBukkitEntity();
}
    public Player getCraftWorker(Location loc)
    {
        if (loc == null || loc.getWorld() == null) return null;
        return getCraftWorker("TB_NPC_" + loc.getWorld().getName(), loc);
    }
    public boolean isWorker(Player player)
    {
        return isWorker(player.getUniqueId());
    }
    public  boolean isWorker(UUID uuid)
    {
        for(EntityPlayer entityPlayer: npcWorkers.values())
        {
            if (uuid.equals(entityPlayer.getBukkitEntity().getUniqueId()))
            {
                return true;
            }
        }
        return false;
    }
    public void sendCommand(Location loc, String command)
    {
        if (loc == null || loc.getWorld() == null) return;
        EntityPlayer npc = npcWorkers.get(loc.getWorld().getName());
        if (npc == null) return;
        npc.a(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        CraftPlayer opCr = npc.getBukkitEntity();
        Bukkit.dispatchCommand(opCr, command);
    }
}
