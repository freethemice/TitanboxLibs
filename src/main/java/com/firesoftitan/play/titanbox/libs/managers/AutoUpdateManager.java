package com.firesoftitan.play.titanbox.libs.managers;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.tools.Tools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class AutoUpdateManager {
    private HashMap<Integer, JavaPlugin> pluginHashMap = new HashMap<Integer, JavaPlugin>();
    public AutoUpdateManager() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int spigotID: pluginHashMap.keySet()) {
                    JavaPlugin plugin = pluginHashMap.get(spigotID);
                    getVersion(spigotID, plugin, version -> {
                        if (plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                            Tools.getMessageTool(plugin).sendMessageSystem("Plugin is up to date.");
                        } else {
                            Tools.getMessageTool(plugin).sendMessageSystem("There is a new update available.");
                            Tools.getMessageTool(plugin).sendMessageSystem("https://www.spigotmc.org/resources/" + spigotID);
                        }
                    });
                }
            }
        }.runTaskLater(TitanBoxLibs.instants,20);
    }
    public void checkAll(Player player)
    {
        if (player.isOp() || player.hasPermission("titanbox.admin")) {
            for (int spigotID : pluginHashMap.keySet()) {
                JavaPlugin plugin = pluginHashMap.get(spigotID);
                getVersion(spigotID, plugin, version -> {
                    if (!plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                        Tools.getMessageTool(plugin).sendMessagePlayer(player, "There is a new update available.");
                        Tools.getMessageTool(plugin).sendMessagePlayer(player, "https://www.spigotmc.org/resources/" + spigotID);
                    }
                });
            }
        }
    }
    public void addPlugin(int spigotID, JavaPlugin yourPlugin)
    {
        pluginHashMap.put(spigotID, yourPlugin);
    }
    private void getVersion(int spigotID, JavaPlugin plugin, final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + spigotID).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
            }
        });
    }

}
