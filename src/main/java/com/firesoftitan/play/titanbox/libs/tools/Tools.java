package com.firesoftitan.play.titanbox.libs.tools;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.runnables.SaveRunnable;
import com.firesoftitan.play.titanbox.libs.runnables.TitanSaverRunnable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Tools {
    private  static HashMap<JavaPlugin, Tools> allTools = new HashMap<JavaPlugin, Tools>();
    public static Tools getTools(JavaPlugin plugin)
    {
        return allTools.get(plugin);
    }
    public static LibsFormattingTool getFormattingTool(JavaPlugin plugin) {
        return allTools.get(plugin).FormattingTool;
    }
    public static LibsBlockTool getBlockTool(JavaPlugin plugin) {
        return allTools.get(plugin).BlockTool;
    }
    public static LibsMiscTool getMiscTool(JavaPlugin plugin) {
        return allTools.get(plugin).MiscTool;
    }
    public static LibsItemStackTool getItemStackTool(JavaPlugin plugin) {
        return allTools.get(plugin).ItemStackTool;
    }
    public static LibsLocationTool getLocationTool(JavaPlugin plugin) {
        return allTools.get(plugin).LocationTool;
    }
    public static LibsMessageTool getMessageTool(JavaPlugin plugin) {
        return allTools.get(plugin).MessageTool;
    }
    public static LibsNBTTool getNBTTool(JavaPlugin plugin) {
        return allTools.get(plugin).NBTTool;
    }
    public static LibsSerializeTool getSerializeTool(JavaPlugin plugin) {
        return allTools.get(plugin).SerializeTool;
    }
    public static LibsSkullTool getSkullTool(JavaPlugin plugin) {
        return allTools.get(plugin).SkullTool;
    }
    public static LibsVaultTool getVaultTool(JavaPlugin plugin) {
        return allTools.get(plugin).VaultTool;
    }
    public static LibsPlayerTool getPlayerTool(JavaPlugin plugin) {
        return allTools.get(plugin).PlayerTool;
    }
    public static LibsFloatingTextTool getFloatingTextTool(JavaPlugin plugin) {
        return allTools.get(plugin).FloatingTextTool;
    }
    public static LibsAdvancedRecipeTool getRecipeTool(JavaPlugin plugin) {
        return allTools.get(plugin).RecipeTool;
    }
    private JavaPlugin plugin;
    private int spigotID;
    private LibsFormattingTool FormattingTool;
    private LibsBlockTool BlockTool;
    private LibsMiscTool MiscTool;
    private LibsItemStackTool ItemStackTool;
    private LibsLocationTool LocationTool;
    private LibsMessageTool MessageTool;
    private LibsNBTTool NBTTool;
    private LibsSerializeTool SerializeTool;
    private LibsSkullTool SkullTool;
    private LibsVaultTool VaultTool;
    private LibsPlayerTool PlayerTool;
    private LibsFloatingTextTool FloatingTextTool;
    private LibsAdvancedRecipeTool RecipeTool;
    private static SaveRunnable saver = new SaveRunnable();
    protected static Tools tools;

    public Tools(JavaPlugin plugin, TitanSaverRunnable titanSaverRunnable, int spigotID) {

        this.plugin = plugin;
        this.spigotID = spigotID;
        this.BlockTool = new LibsBlockTool(this);
        this.FloatingTextTool = new LibsFloatingTextTool(this);
        this.FormattingTool = new LibsFormattingTool(this);
        this.ItemStackTool = new LibsItemStackTool(this);
        this.LocationTool = new LibsLocationTool(this);
        this.MessageTool = new LibsMessageTool(this);
        this.MiscTool = new LibsMiscTool(this);
        this.NBTTool = new LibsNBTTool(this);
        this.PlayerTool = new LibsPlayerTool(this);
        this.SerializeTool = new LibsSerializeTool(this);
        this.SkullTool = new LibsSkullTool(this);
        this.VaultTool = new LibsVaultTool(this);
        this.RecipeTool = new LibsAdvancedRecipeTool(this);
        allTools.put(plugin,this);
        saver.addSaveRunnable(titanSaverRunnable);
        if (tools == null && plugin.getName().equals(TitanBoxLibs.instants.getName()))
        {
            tools = this;
            Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBoxLibs.instants,  saver, TitanBoxLibs.instants.configManager.getSave_frequancy(), TitanBoxLibs.instants.configManager.getSave_frequancy());
        }
        if (spigotID > 0) TitanBoxLibs.autoUpdateManager.addPlugin(spigotID, plugin);

    }
    public static void disablePlugin()
    {
        saver.run();
    }
    public JavaPlugin getPlugin() {
        return plugin;
    }

    public LibsAdvancedRecipeTool getRecipeTool() {
        return RecipeTool;
    }

    public LibsFormattingTool getFormattingTool() {
        return FormattingTool;
    }

    public LibsBlockTool getBlockTool() {
        return BlockTool;
    }

    public LibsMiscTool getMiscTool() {
        return MiscTool;
    }

    public LibsItemStackTool getItemStackTool() {
        return ItemStackTool;
    }

    public LibsLocationTool getLocationTool() {
        return LocationTool;
    }

    public LibsMessageTool getMessageTool() {
        return MessageTool;
    }

    public LibsNBTTool getNBTTool() {
        return NBTTool;
    }

    public LibsSerializeTool getSerializeTool() {
        return SerializeTool;
    }

    public LibsSkullTool getSkullTool() {
        return SkullTool;
    }

    public LibsVaultTool getVaultTool() {
        return VaultTool;
    }

    public LibsPlayerTool getPlayerTool() {
        return PlayerTool;
    }

    public LibsFloatingTextTool getFloatingTextTool() {
        return FloatingTextTool;
    }
}
