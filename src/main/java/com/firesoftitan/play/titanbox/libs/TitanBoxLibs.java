package com.firesoftitan.play.titanbox.libs;

import com.firesoftitan.play.titanbox.libs.listeners.MainListener;
import com.firesoftitan.play.titanbox.libs.managers.BarcodeManager;
import com.firesoftitan.play.titanbox.libs.managers.ConfigManager;
import com.firesoftitan.play.titanbox.libs.managers.WorkerManager;
import com.firesoftitan.play.titanbox.libs.runnables.MySaveRunnable;
import com.firesoftitan.play.titanbox.libs.tools.*;
import org.jetbrains.annotations.NotNull;
import com.firesoftitan.play.titanbox.libs.interfaces.CommandInterface;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.*;

public class TitanBoxLibs extends JavaPlugin {
    public static TitanBoxLibs instants;
    public MainListener mainListener;
    protected ConfigManager configManager;
    protected static Tools tools;
    public static BarcodeManager barcodeManager;
    public static WorkerManager workerManager;
    public void onEnable() {
        TitanBoxLibs.instants = this;
        TitanBoxLibs.tools = new Tools(this);
        TitanBoxLibs.workerManager = new WorkerManager();
        TitanBoxLibs.barcodeManager = new BarcodeManager();
        mainListener = new MainListener();

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

        configManager = new ConfigManager();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBoxLibs.instants,  TitanBoxLibs.tools.getMiscTool().saver, configManager.getSave_frequancy(), configManager.getSave_frequancy());

        new BukkitRunnable() {
            @Override
            public void run() {
                TitanBoxLibs.workerManager.loadAll();
            }
        }.runTaskLater(this, 1);

         TitanBoxLibs.tools.getMiscTool().addSaveRunnable(new MySaveRunnable(this));

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
           if (label.equalsIgnoreCase("titanbox") || label.equalsIgnoreCase("tb")) {
            if (args.length > 0) {
                String name  = args[0];
                if (  TitanBoxLibs.tools.getMiscTool().commandInterfaces.containsKey(name))
                {
                    CommandInterface commandInterface =   TitanBoxLibs.tools.getMiscTool().commandInterfaces.get(name);
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
         TitanBoxLibs.tools.getMiscTool().saver.run();
    }
}
