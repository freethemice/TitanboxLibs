package com.firesoftitan.play.titanbox.libs;

import com.firesoftitan.play.titanbox.libs.interfaces.CommandInterface;
import com.firesoftitan.play.titanbox.libs.listeners.MainListener;
import com.firesoftitan.play.titanbox.libs.listeners.PluginListener;
import com.firesoftitan.play.titanbox.libs.listeners.TabCompleteListener;
import com.firesoftitan.play.titanbox.libs.managers.*;
import com.firesoftitan.play.titanbox.libs.runnables.MySaveRunnable;
import com.firesoftitan.play.titanbox.libs.runnables.WildTeleportRunnable;
import com.firesoftitan.play.titanbox.libs.tools.LibsProtectionTool;
import com.firesoftitan.play.titanbox.libs.tools.Tools;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.Level;

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

        new BukkitRunnable() {
            @Override
            public void run() {
                checkUpdate();
            }
        }.runTaskLater(TitanBoxLibs.instants, 1200); //60 Seconds
        configManager = new ConfigManager();
        TitanBoxLibs.tools = new Tools(this, new MySaveRunnable(this), -1);

        TitanBoxLibs.workerManager = new WorkerManager();
        TitanBoxLibs.barcodeManager = new BarcodeManager();
        mainListener = new MainListener();
        pluginListener = new PluginListener();
        TitanBlockManager.initialize();
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
                        //noinspection CallToPrintStackTrace
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
        }.runTaskLater(this, 20);

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
        PluginCommand tb = this.getCommand("tb");
        if (tb != null) tb.setTabCompleter(new TabCompleteListener());
        PluginCommand titanbox = this.getCommand("titanbox");
        if (titanbox != null) titanbox.setTabCompleter(new TabCompleteListener());

    }



    private void checkUpdate()
    {
        String gitURL = "https://api.github.com/repos/freethemice/TitanboxLibs/releases";
        try {
            JsonArray jsonObject = readJsonFromUrl(gitURL);
            String tag_name = jsonObject.get(0).getAsJsonObject().get("tag_name").getAsString();
            if (this.getDescription().getVersion().equalsIgnoreCase(tag_name))
            {
                this.getLogger().log(Level.INFO,   ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', "Plugin is up to date."));
            } else {
                this.getLogger().log(Level.WARNING,   ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', ">>>>>>>>>>>>-------------<<<<<<<<<<<<"));
                this.getLogger().log(Level.WARNING,   ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', "There is a new update available."));
                this.getLogger().log(Level.WARNING,   ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', jsonObject.get(0).getAsJsonObject().get("html_url").getAsString()));
                this.getLogger().log(Level.WARNING,   ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', ">>>>>>>>>>>>-------------<<<<<<<<<<<<"));
            }

        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }
    public static JsonArray readJsonFromUrl(String url) throws IOException {


        String jsonText =  IOUtils.toString(new URL(url), Charset.defaultCharset());
        JsonElement jsonElement = JsonParser.parseString(jsonText);
        return jsonElement.getAsJsonArray();
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
                        if (player2 == null) sender.sendMessage(name + " doesn't exist.");
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
                if (name.equalsIgnoreCase("wild"))
                {
                    if (sender instanceof Player player) {
                        WildTeleportRunnable wildTeleportRunnable = new WildTeleportRunnable(player, TitanBoxLibs.tools);
                        wildTeleportRunnable.runTaskTimer(this, 1, 10);
                        return true;
                    }

                }
                if (name.equalsIgnoreCase("protection"))
                {
                    if (args.length == 1)
                    {
                        tools.getMessageTool().sendMessagePlayer((Player) sender, ChatColor.GREEN + "You can add plugins by: ");
                        tools.getMessageTool().sendMessagePlayer((Player) sender, ChatColor.GRAY + "     /tb protection " + ChatColor.WHITE + "add " + ChatColor.AQUA + "plugin_name");
                        tools.getMessageTool().sendMessagePlayer((Player) sender, ChatColor.GREEN + "Please remove plugins by: ");
                        tools.getMessageTool().sendMessagePlayer((Player) sender, ChatColor.GRAY + "     /tb protection " + ChatColor.WHITE + "remove " + ChatColor.AQUA + "plugin_name");
                        tools.getMessageTool().sendMessagePlayer((Player) sender, ChatColor.GREEN + "You can list available plugins: ");
                        tools.getMessageTool().sendMessagePlayer((Player) sender, ChatColor.GRAY + "     /tb protection " + ChatColor.WHITE + "list ");
                        tools.getMessageTool().sendMessagePlayer((Player) sender, ChatColor.GREEN + "You can list enabled plugins: ");
                        tools.getMessageTool().sendMessagePlayer((Player) sender, ChatColor.GRAY + "     /tb protection " + ChatColor.WHITE + "enabled ");
                    }
                    if (args.length > 1)
                    {
                        if (args[1].equalsIgnoreCase("list"))
                        {
                            List<String> listingProtectionPlugins = LibsProtectionTool.getListingProtectionPlugins((Player) sender);
                            tools.getMessageTool().sendMessagePlayer((Player) sender, ChatColor.UNDERLINE + "-------------------");
                            for (String plugin : listingProtectionPlugins) {
                                tools.getMessageTool().sendMessagePlayer((Player) sender, "Plugin: " + plugin);
                            }
                            tools.getMessageTool().sendMessagePlayer((Player) sender, ChatColor.UNDERLINE + "-------------------");
                        }
                        if (args[1].equalsIgnoreCase("enabled"))
                        {
                            List<String> listingProtectionPlugins = LibsProtectionTool.getEnabledProtectionPlugins((Player) sender);
                            tools.getMessageTool().sendMessagePlayer((Player) sender, ChatColor.UNDERLINE + "-------------------");
                            for (String plugin : listingProtectionPlugins) {
                                tools.getMessageTool().sendMessagePlayer((Player) sender, "Plugin: " + plugin);
                            }
                            tools.getMessageTool().sendMessagePlayer((Player) sender, ChatColor.UNDERLINE + "-------------------");
                        }
                    }
                    if (args.length > 2)
                    {
                        if (args[1].equalsIgnoreCase("add"))
                        {
                            LibsProtectionTool.addPlugin((Player) sender, args[2]);
                            tools.getMessageTool().sendMessagePlayer((Player) sender, "plugin added to the protection plugin list.");
                        }
                        if (args[1].equalsIgnoreCase("remove"))
                        {
                            LibsProtectionTool.removePlugin((Player) sender, args[2]);
                            tools.getMessageTool().sendMessagePlayer((Player) sender, "plugin was removed from protection plugin list.");
                        }
                    }
                    return true;
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
