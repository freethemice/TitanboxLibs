package com.firesoftitan.play.titanbox.libs;

import com.firesoftitan.play.titanbox.libs.listeners.MainListener;
import com.firesoftitan.play.titanbox.libs.listeners.PluginListener;
import com.firesoftitan.play.titanbox.libs.managers.*;
import com.firesoftitan.play.titanbox.libs.runnables.MySaveRunnable;
import com.firesoftitan.play.titanbox.libs.runnables.WildTeleportRunnable;
import com.firesoftitan.play.titanbox.libs.tools.*;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTCompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.StreamTagVisitor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.DefinedStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_19_R2.CraftServer;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_19_R2.util.CraftMagicNumbers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventException;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredListener;
import org.jetbrains.annotations.NotNull;
import com.firesoftitan.play.titanbox.libs.interfaces.CommandInterface;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class TitanBoxLibs extends JavaPlugin {
    public static TitanBoxLibs instants;
    public MainListener mainListener;
    public PluginListener pluginListener;
    public ConfigManager configManager;
    public static Tools tools;
    public static BarcodeManager barcodeManager;
    public static WorkerManager workerManager;
    public static AutoUpdateManager autoUpdateManager;
    public void onEnable() {

        TitanBoxLibs.instants = this;
        configManager = new ConfigManager();
        TitanBoxLibs.tools = new Tools(this, new MySaveRunnable(this), -1);

        TitanBoxLibs.workerManager = new WorkerManager();
        TitanBoxLibs.barcodeManager = new BarcodeManager();
        mainListener = new MainListener();
        pluginListener = new PluginListener();
        Timer getPlayers = new Timer();
        getPlayers.schedule(new TimerTask() {
            @Override
            public void run() {
                TitanBoxLibs.tools.getMessageTool().sendMessageSystem( "Loading players textures on the server.");
                for(OfflinePlayer player1: Bukkit.getOfflinePlayers())
                {
                    try {
                        String texture =  TitanBoxLibs.tools.getPlayerTool().getPlayersTexture(player1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                TitanBoxLibs.tools.getMessageTool().sendMessageSystem("All players textures loaded.");

            }
        }, 10);



        new BukkitRunnable() {
            @Override
            public void run() {
                TitanBoxLibs.workerManager.loadAll();
            }
        }.runTaskLater(this, 1);

        autoUpdateManager = new AutoUpdateManager();



        new BukkitRunnable() {
            @Override
            public void run() {
                addCraftingBook();
            }
        }.runTaskLater(TitanBoxLibs.instants, 1);

        SettingsManager config = new SettingsManager(TitanBoxLibs.instants.getName(), "wild_config");
        if (!config.contains("wild.use_worldborder"))
        {
            config.set("wild.use_worldborder", true);
            config.set("wild.x", 10000);
            config.set("wild.z", 10000);
            config.save();
        }

    }
    private void addCraftingBook() {
        ItemStack[] matrix = new ItemStack[9];
        matrix[0] = new ItemStack(Material.LEATHER);
        matrix[1] = new ItemStack(Material.LEATHER);
        matrix[2] = new ItemStack(Material.LEATHER);
        matrix[3] = new ItemStack(Material.PAPER);
        matrix[4] = new ItemStack(Material.PAPER);
        matrix[5] = new ItemStack(Material.PAPER);
        matrix[6] = new ItemStack(Material.LEATHER);
        matrix[7] = new ItemStack(Material.LEATHER);
        matrix[8] = new ItemStack(Material.LEATHER);

        ItemStack partItem = getCraftingBook();
        tools.getRecipeTool().addAdvancedRecipe(partItem, matrix);
    }

    public ItemStack getCraftingBook()
    {
        ItemStack itemStack = new ItemStack(Material.BOOK);
        itemStack = tools.getItemStackTool().setTitanItemID(itemStack, "CRAFTING_BOOK");
        itemStack = tools.getItemStackTool().changeName(itemStack, ChatColor.AQUA + "Titan Crafting Book");
        return itemStack.clone();
    }
    public void help(CommandSender player)
    {
        List<String> commandHelp = new ArrayList<>();
        for(String key:   TitanBoxLibs.tools.getMiscTool().commandInterfaces.keySet())
        {
            commandHelp.add(ChatColor.GOLD + "/tb " + ChatColor.AQUA + key + ChatColor.GREEN + " help");
        }
        TitanBoxLibs.tools.getMessageTool().sendMessagePlayer( (Player) player, commandHelp);
    }
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("titan") || label.equalsIgnoreCase("titanguide") || label.equalsIgnoreCase("tguide")) {
            if (sender instanceof Player player)
            {
                if (args.length > 0) {
                    if (player.isOp() || player.hasPermission("titanbox.admin"))
                    {
                        String name  = args[0];
                        Player player2 = Bukkit.getPlayer(name);
                        if (player == null) sender.sendMessage(name + " doesn't exist.");
                        else player2.getInventory().addItem(getCraftingBook().clone());
                    }
                    else
                    {
                        player.getInventory().addItem(getCraftingBook().clone());
                    }
                }
                else {
                    player.getInventory().addItem(getCraftingBook().clone());
                }
            }
            else
            {
                if (args.length > 0) {
                    String name  = args[0];
                    Player player = Bukkit.getPlayer(name);
                    if (player == null) sender.sendMessage(name + " doesn't exist.");
                    else player.getInventory().addItem(getCraftingBook().clone());
                }
            }
            return true;
        }
        if (label.equalsIgnoreCase("titankill"))
        {
            if (sender instanceof ConsoleCommandSender) {
                if (args.length > 1) {
                    try {
                        int BR = Integer.parseInt(args[1]);
                        Player player = Bukkit.getPlayer(args[0]);
                        if (BR > 0 && player != null && player.isOnline()) {
                            List<Entity> nearbyEntities = player.getNearbyEntities(BR, BR, BR);
                            for (Entity entity : nearbyEntities) {
                                if (entity instanceof LivingEntity && !(entity instanceof Villager) && !(entity instanceof Player)) {

                                    new BukkitRunnable() {
                                        int i =0;
                                        @Override
                                        public void run() {
                                            if (!entity.isDead()) ((LivingEntity) entity).damage(1000000, player);
                                            if (entity.isDead()) i++;
                                            if (i > 50) this.cancel();
                                        }
                                    }.runTaskTimer(TitanBoxLibs.instants, 1,1);

                                }
                            }
                        }
                        return true;
                    } catch (NumberFormatException e) {
                        //e.printStackTrace();
                    }

                }
            }
        }
        if (label.equalsIgnoreCase("titanbox") || label.equalsIgnoreCase("tb")) {
            if (args.length > 0) {
                String name  = args[0];
                if (name.toLowerCase().equalsIgnoreCase("wild"))
                {
                    if (sender instanceof  Player) {
                        Player player = (Player) sender;
                        WildTeleportRunnable wildTeleportRunnable = new WildTeleportRunnable(player, TitanBoxLibs.tools);
                        wildTeleportRunnable.runTaskTimer(this, 1, 10);
                        return true;
                    }

                }
                if (name.toLowerCase().equalsIgnoreCase("kill"))
                {
/*                    if (args.length > 2)
                    {
                        try {
                            int BR = Integer.parseInt(args[1]);
                            Player player = Bukkit.getPlayer(args[2]);
                            if (BR > 0 && player != null && player.isOnline())
                            {
                                List<Entity> nearbyEntities = player.getNearbyEntities(BR, BR, BR);
                                for(Entity entity: nearbyEntities)
                                {
                                    EntityDamageEvent entityDamageEvent = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.FALL, 100000000);
                                    Bukkit.getPluginManager().callEvent(entityDamageEvent);
                                }
                            }
                            return true;
                        } catch (NumberFormatException e) {
                            //e.printStackTrace();
                        }

                    }*/
                }

                if (TitanBoxLibs.tools.getMiscTool().commandInterfaces.containsKey(name))
                {
                    CommandInterface commandInterface = TitanBoxLibs.tools.getMiscTool().commandInterfaces.get(name);
                    if (args.length == 1) {
                        commandInterface.help(sender);
                        return true;
                    }
                    args = Arrays.copyOfRange(args, 1, args.length);
                    boolean ranCommand = commandInterface.onCommand(sender, cmd, args);
                    if (!ranCommand) commandInterface.help(sender);
                    return true;
                }
            }
        }
        this.help(sender);
        return true;
    }
    public void onDisable()
    {
        this.saveALL();
    }
    public void saveALL()
    {
         Tools.disablePlugin();
    }
}
