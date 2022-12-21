package com.firesoftitan.play.titanbox.libs.runnables;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.tools.Tools;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class WildTeleportRunnable extends BukkitRunnable {
    private static List<UUID> runningPlayers = new ArrayList<UUID>();
    private HashSet<Material> bad_blocks;
    private int attempts;
    private Player player;
    protected Tools tools;

    public WildTeleportRunnable(Player player, Tools tools) {
        this.player = player;
        this.tools = tools;
        if (runningPlayers.contains(player.getUniqueId()))
        {
            attempts = -1;
            return;
        }
        if (player.getWorld().getEnvironment() != World.Environment.NORMAL)
        {
            tools.getMessageTool().sendMessagePlayer( (Player) player, ChatColor.AQUA + "Can't use in this world.");
            attempts = -1;
            return;
        }
        runningPlayers.add(player.getUniqueId());
        bad_blocks = new HashSet<>();
        bad_blocks.add(Material.LAVA);
        bad_blocks.add(Material.WATER);
        bad_blocks.add(Material.FIRE);
        bad_blocks.add(Material.CACTUS);
        bad_blocks.add(Material.MAGMA_BLOCK);
        bad_blocks.add(Material.SWEET_BERRY_BUSH);
        bad_blocks.add(Material.COBWEB);
        bad_blocks.add(Material.CAMPFIRE);
        bad_blocks.add(Material.SOUL_CAMPFIRE);
        bad_blocks.add(Material.NETHER_PORTAL);
        bad_blocks.add(Material.END_PORTAL);
        bad_blocks.add(Material.VOID_AIR);
        bad_blocks.add(Material.POWDER_SNOW);
        bad_blocks.add(Material.POINTED_DRIPSTONE);
        bad_blocks.add(Material.BIG_DRIPLEAF);
        bad_blocks.add(Material.SMALL_DRIPLEAF);
        bad_blocks.add(Material.BEDROCK);
        attempts = 0;
        double size = player.getWorld().getWorldBorder().getSize();
        tools.getMessageTool().sendMessagePlayer( (Player) player, ChatColor.AQUA + "Looking for safe place to teleport, please wait...");
        tools.getMessageTool().sendMessagePlayer( (Player) player, ChatColor.GRAY + "World Boarder Size: " + tools.getFormattingTool().formatCommas(size));
    }

    @Override
    public void run() {
        if (attempts < 0)
        {
            this.cancel();
            return;
        }
        attempts++;
        Random random = new Random(System.currentTimeMillis());
        int x = 0;
        int z = 0;
        int y = 0;
        double size = player.getWorld().getWorldBorder().getSize() / 2;
        if (size > 100000) size = 100000;
        x = (int) (random.nextInt((int) (size * 2)) - size);
        z = (int) (random.nextInt((int) (size * 2)) - size);
        y = 150;


        Location randomLocation = new Location(player.getWorld(), x, y, z);
        y = randomLocation.getWorld().getHighestBlockYAt(randomLocation);
        randomLocation.setX(x + 0.5D);
        randomLocation.setY((y + 1));
        randomLocation.setZ(z + 0.5D);
        if (isLocationSafe(randomLocation))
        {
            tools.getMessageTool().sendMessagePlayer( (Player) player, ChatColor.AQUA + "Safe place found... (" + ChatColor.WHITE + randomLocation.getBlockX() + ", " + randomLocation.getBlockZ() + ChatColor.AQUA + ")");
            this.cancel();
            player.teleport(randomLocation);
            runningPlayers.remove(player.getUniqueId());
        }
        else
        {
            if (attempts > 10)
            {
                tools.getMessageTool().sendMessagePlayer( (Player) player, ChatColor.AQUA + "Couldn't find safe place to teleport, please try again...");
                this.cancel();
                runningPlayers.remove(player.getUniqueId());
            }
            else {
                tools.getMessageTool().sendMessagePlayer( (Player) player, ChatColor.AQUA + "Looking, please wait...");
            }

        }
    }

    private boolean isLocationSafe(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        if (location.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
            Block block1 = location.getWorld().getBlockAt(x, y, z);
            Block block2 = location.getWorld().getBlockAt(x, y - 1, z);
            Block block3 = location.getWorld().getBlockAt(x, y + 1, z);
            if (y > 125)
                return false;
            return (!bad_blocks.contains(block2.getType()) || block1.getType().isSolid() || block3.getType().isSolid());
        }
        Block block = location.getWorld().getBlockAt(x, y, z);
        Block below = location.getWorld().getBlockAt(x, y - 1, z);
        Block above = location.getWorld().getBlockAt(x, y + 1, z);
        return (!bad_blocks.contains(below.getType()) || block.getType().isSolid() || above.getType().isSolid());
    }
}
