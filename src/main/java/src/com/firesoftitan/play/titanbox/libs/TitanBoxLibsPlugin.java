package src.com.firesoftitan.play.titanbox.libs;

import org.jetbrains.annotations.NotNull;
import src.com.firesoftitan.play.titanbox.libs.interfaces.CommandInterface;
import src.com.firesoftitan.play.titanbox.libs.listeners.MainListener;
import src.com.firesoftitan.play.titanbox.libs.managers.BarcodeManager;
import src.com.firesoftitan.play.titanbox.libs.managers.ConfigManager;
import src.com.firesoftitan.play.titanbox.libs.managers.WorkerManager;
import src.com.firesoftitan.play.titanbox.libs.runnables.MySaveRunnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import src.com.firesoftitan.play.titanbox.libs.tools.*;

import java.io.IOException;
import java.util.*;

public class TitanBoxLibsPlugin extends JavaPlugin {
    public static TitanBoxLibsPlugin instants;
    public MainListener mainListener;
    protected ConfigManager configManager;
    public void onEnable() {
        TitanBoxLibsPlugin.instants = this;
        mainListener = new MainListener();
        LibsVaultTool.setup();
        LibsMiscTool.workerManager = new WorkerManager();
        LibsMiscTool.barcodeManager = new BarcodeManager();
        Timer getPlayers = new Timer();
        getPlayers.schedule(new TimerTask() {
            @Override
            public void run() {
                LibsMessageTool.sendMessageSystem(TitanBoxLibsPlugin.instants, "Loading players textures on the server.");
                for(OfflinePlayer player1: Bukkit.getOfflinePlayers())
                {
                    try {
                        String texture = LibsPlayerTool.getPlayersTexture(player1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                LibsMessageTool.sendMessageSystem(TitanBoxLibsPlugin.instants, "All players textures loaded.");

            }
        }, 10);

        configManager = new ConfigManager();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBoxLibsPlugin.instants, LibsMiscTool.saver, configManager.getSave_frequancy(), configManager.getSave_frequancy());

        new BukkitRunnable() {
            @Override
            public void run() {
                LibsMiscTool.workerManager.loadAll();
            }
        }.runTaskLater(this, 1);

        LibsMiscTool.addSaveRunnable(new MySaveRunnable(this));

    }
    public void help(CommandSender player)
    {
        List<String> commandHelp = new ArrayList<>();
        for(String key:  LibsMiscTool.commandInterfaces.keySet())
        {
            commandHelp.add(ChatColor.GOLD + "/tb " + ChatColor.AQUA + key + ChatColor.GREEN + " help");
        }
        LibsMessageTool.sendMessagePlayer(this, (Player) player, commandHelp);
    }
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, String label, String[] args) {
           if (label.equalsIgnoreCase("titanbox") || label.equalsIgnoreCase("tb")) {
            if (args.length > 0) {
                String name  = args[0];
                if ( LibsMiscTool.commandInterfaces.containsKey(name))
                {
                    CommandInterface commandInterface =  LibsMiscTool.commandInterfaces.get(name);
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
        LibsMiscTool.saver.run();
    }
}
