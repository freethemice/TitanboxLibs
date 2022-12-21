package com.firesoftitan.play.titanbox.libs.tools;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.EnumProtocolDirection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_19_R2.CraftServer;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LibsPlayerTool {
    private Tools parent;

    public LibsPlayerTool(Tools parent) {
        this.parent = parent;
    }

    private final HashMap<String, String> players = new HashMap<>();
    public ItemStack addItemsToPlayer(Player player, ItemStack placing)
    {
        placing = placing.clone();
        List<Integer> emptySlos = new ArrayList<>();
        Inventory playersInv = player.getInventory();
        for (int i = 0; i < 36; i++) {
            ItemStack checkItem = playersInv.getItem(i);
            if ( Tools.tools.getItemStackTool().isEmpty(checkItem))
            {
                emptySlos.add(i); //this slot is empty lets keep that in mind for later
            }
            else
            {
                checkItem = checkItem.clone();
                if ( Tools.tools.getItemStackTool().isItemEqual(checkItem, placing))// is this the same thing
                {
                    if (checkItem.getAmount() < checkItem.getMaxStackSize()) //is there room in this stack for more
                    {
                        int placeAmount = Math.min(checkItem.getMaxStackSize() - checkItem.getAmount(), placing.getAmount()); //how much more can it hold
                        checkItem.setAmount(checkItem.getAmount() + placeAmount); //update the item
                        playersInv.setItem(i, checkItem.clone());//place the item back
                        if (placing.getAmount() - placeAmount <= 0) //is there any left over we need to place
                        {
                            return null; //we are done
                        }
                        placing.setAmount(placing.getAmount() - placeAmount); //some was left, lets keep looking
                    }
                }
            }

        }
        //we finished looking for more room now lets fill in the empty slots
        for (Integer slot: emptySlos)
        {
            int howMuch = Math.min(placing.getMaxStackSize(), placing.getAmount());
            ItemStack itemStack = placing.clone();
            itemStack.setAmount(howMuch);
            playersInv.setItem(slot, itemStack.clone());
            if (placing.getAmount() - howMuch <= 0) //is there any left over we need to place
            {
                return null; //we are done
            }
            placing.setAmount(placing.getAmount() - howMuch); //some was left, lets keep looking
        }

        return placing.clone(); // didn't have enough room, sorry

    }
    public void pickAHand(Player player, BukkitRunnable bukkitRunnable)
    {
        player.closeInventory();
        BukkitTask bukkitTask = new BukkitRunnable() {
            private int time = 5;

            @Override
            public void run() {
                if (time > 0)
                    player.sendMessage(ChatColor.RED + "Hold Item In Main Hand in... " + time + " Seconds");
                time--;
                if (time == 0) {
                    if (! Tools.tools.getItemStackTool().isEmpty(player.getInventory().getItemInMainHand())) {
                        bukkitRunnable.runTask(TitanBoxLibs.instants);
                    }
                    else
                    {
                        player.sendMessage(ChatColor.RED + "Canceled.");
                    }
                }
            }
        }.runTaskTimer(TitanBoxLibs.instants, 20, 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                bukkitTask.cancel();
            }
        }.runTaskLater(TitanBoxLibs.instants, 11 * 20);
    }
    public void startTeleport(Player player, Location location)
    {
        int time = 9;
        startTeleport(player, location, time);
    }
    public void startTeleport(Player player, Location location, int time)
    {
        final int x =player.getLocation().getBlockX();
        final int y =player.getLocation().getBlockY();
        final int z =player.getLocation().getBlockZ();
        location.setPitch(player.getLocation().getPitch());
        location.setYaw(player.getLocation().getYaw());
        //if (time < 1) player.sendMessage(ChatColor.RED + "You Will Be Teleported now.");
        //else player.sendMessage(ChatColor.RED + "You Will Be Teleported in " + (time) +" Seconds");
        Runnable runnable = new Runnable() {
            private int passed = 0;
            private boolean ran = false;

            @Override
            public void run() {
                if (!ran) {
                    passed++;
                    int countT = (time - passed) + 1;
                    if (passed > time) {
                        if ((player.getLocation().getBlockX() != x) || (player.getLocation().getBlockY() != y) || (player.getLocation().getBlockZ() != z)) {
                            player.sendMessage(ChatColor.RED + "Teleport Canceled!");
                        } else {
                            player.teleport(location.clone().add(0, 2, 0));
                            player.sendMessage(ChatColor.RED + "Your new location is: " + location.getBlock().getX() + ", " + location.getBlock().getY() + ", " + location.getBlock().getZ());
                        }
                        ran = true;
                    } else {
                        if ((player.getLocation().getBlockX() != x) || (player.getLocation().getBlockY() != y) || (player.getLocation().getBlockZ() != z)) {
                            player.sendMessage(ChatColor.RED + "Teleport Canceled!");
                            ran = true;
                        } else {
                            player.sendMessage(ChatColor.RED + "Teleporting in:  " + countT + " seconds.");
                        }
                    }
                }
            }
        };
        if (time < 1) runnable.run();
        else
        {
            int myId = Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBoxLibs.instants, runnable, 20, 20);
            Bukkit.getScheduler().scheduleSyncDelayedTask(TitanBoxLibs.instants, () -> Bukkit.getScheduler().cancelTask(myId), (time + 1)* 20L);
        }
    }
    public void addInventoryOrDrop(Player player, ItemStack itemStack)
    {
        addInventoryOrDrop(player.getLocation(), player.getInventory(), itemStack);
    }
    public void addInventoryOrDrop(Location location, Inventory inventory, ItemStack itemStack)
    {

        if (inventory.firstEmpty() > -1) {
            inventory.addItem(itemStack.clone());
        } else {
            World world = location.getWorld();
            if (world == null) return;
            world.dropItemNaturally(location, itemStack.clone());
        }
    }
    public boolean isItemInInventory(Inventory inventory, ItemStack itemStack)
    {
        return  Tools.tools.getBlockTool().isItemInInventory(inventory, itemStack);
    }
    public boolean isPlayerTextureLoaded(OfflinePlayer player) {
        return isPlayerTextureLoaded(player.getUniqueId());
    }
    public boolean isPlayerTextureLoaded(Player player) {
        return isPlayerTextureLoaded(player.getUniqueId());
    }
    public boolean isPlayerTextureLoaded(UUID player){
        String uuid = player.toString();
        return players.containsKey(uuid);
    }
    public String getPlayersTexture(OfflinePlayer player) throws IOException
    {
        return getPlayersTexture(player.getUniqueId());
    }

    public String getPlayersSignature(Player player) throws IOException
    {
        return getPlayersTexture(player.getUniqueId());
    }
    public Property getPlayerTextureProperty(Player player) throws IOException
    {
        return getPlayerTextureProperty(player.getUniqueId());
    }
    public Property getPlayerTextureProperty(UUID uuid) throws IOException
    {
        return getPlayerTextureProperty(uuid.toString());
    }
    public Property getPlayerTextureProperty(String uuid) throws IOException
    {
        if (uuid == null) {
            throw new NullPointerException("name is marked non-null but is null");
        } else {
            uuid = uuid.replace("-", "");
            URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
            BufferedReader in = new BufferedReader(reader_1);
            String inputLine;
            String allInput = "";
            while ((inputLine = in.readLine()) != null)
                //noinspection StringConcatenationInLoop
                allInput = allInput + inputLine;
            in.close();
            String[] NotTheRightWay = allInput.split("value\" : \"");
            NotTheRightWay =  NotTheRightWay[1].split("\",");
            String texture = NotTheRightWay[0];

            NotTheRightWay = allInput.split("signature\" : \"");
            NotTheRightWay =  NotTheRightWay[1].split("\"");
            String signature = NotTheRightWay[0];
            return new Property("textures", texture, signature);
        }
    }
    public String getPlayersSignature(UUID player) throws IOException {
        String uuid = player.toString();
        if (uuid == null) {
            throw new NullPointerException("name is marked non-null but is null");
        } else {
            uuid = uuid.replace("-", "");
            URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
            BufferedReader in = new BufferedReader(reader_1);
            String inputLine;
            String allInput = "";
            while ((inputLine = in.readLine()) != null)
                //noinspection StringConcatenationInLoop
                allInput = allInput + inputLine;
            in.close();
            String[] NotTheRightWay = allInput.split("value\" : \"");
            NotTheRightWay =  NotTheRightWay[1].split("\",");
            return NotTheRightWay[0];
        }
    }
    public String getPlayersTexture(Player player) throws IOException
    {
        return getPlayersTexture(player.getUniqueId());
    }

    public boolean doesPlayersHaveTexture(UUID player){
        String uuid = player.toString();
        String fulluuid = player.toString();
        if (uuid == null) {
            return false;
        } else {
            try {
                if (players.containsKey(uuid))
                {
                    return true;
                }
                uuid = uuid.replace("-", "");
                URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
                InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
                BufferedReader in = new BufferedReader(reader_1);
                String inputLine;
                String allInput = "";
                while ((inputLine = in.readLine()) != null)
                    //noinspection StringConcatenationInLoop
                    allInput = allInput + inputLine;
                in.close();
                String[] NotTheRightWay = allInput.split("value\" : \"");
                if (NotTheRightWay.length < 2) return false;
                NotTheRightWay =  NotTheRightWay[1].split("\",");
                players.put(fulluuid, NotTheRightWay[0]);
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }
    public String getPlayersTexture(UUID player) throws IOException {
        String uuid = player.toString();
        String fulluuid = player.toString();
        if (uuid == null) {
            throw new NullPointerException("UUID is marked non-null but is null");
        } else {
            if (players.containsKey(uuid))
            {
                return players.get(uuid);
            }
            uuid = uuid.replace("-", "");
            URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
            BufferedReader in = new BufferedReader(reader_1);
            String inputLine;
            String allInput = "";
            while ((inputLine = in.readLine()) != null)
                //noinspection StringConcatenationInLoop
                allInput = allInput + inputLine;
            in.close();
            String[] NotTheRightWay = allInput.split("value\" : \"");
            if (NotTheRightWay.length < 2) throw new NullPointerException("UUID: " + uuid.toString() +"\n Index out of bounds");
            NotTheRightWay =  NotTheRightWay[1].split("\",");
            players.put(fulluuid, NotTheRightWay[0]);
            return NotTheRightWay[0];
        }
    }
    private void loadOffServerPlayer(UUID uuid)
    {
        String name = uuid.toString();
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        World world = Bukkit.getWorlds().get(0);
        WorldServer nmsWorld = ((CraftWorld)world).getHandle();
        if (name.length() > 16) name = name.substring(0, 16);
        GameProfile gameProfile = new GameProfile(uuid, name);
        EntityPlayer entityPlayer = new EntityPlayer(nmsServer, nmsWorld, gameProfile);
        entityPlayer.b = new PlayerConnection(nmsServer, new NetworkManager(EnumProtocolDirection.a), entityPlayer);
    }
    public Player loadOfflinePlayer(OfflinePlayer offline)
    {
        return getEntityPlayer(offline).getBukkitEntity();
    }
    public EntityPlayer getEntityPlayer(OfflinePlayer offline)
    {
        if ((offline == null) || (!offline.hasPlayedBefore())) {
            return null;
        }

        GameProfile profile = new GameProfile(offline.getUniqueId(), offline.getName());
        MinecraftServer server = ((CraftServer)Bukkit.getServer()).getServer();

        WorldServer worldServer = server.a(net.minecraft.world.level.World.f);
        if (worldServer == null) return null;
        EntityPlayer entity = new EntityPlayer(server, worldServer, profile);
        Player target = entity.getBukkitEntity();
        if (target != null)
        {
            target.loadData();
        }
        return entity;
    }

}
