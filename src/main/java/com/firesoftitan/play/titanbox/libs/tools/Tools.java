package com.firesoftitan.play.titanbox.libs.tools;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.runnables.SaveRunnable;
import com.firesoftitan.play.titanbox.libs.runnables.TitanSaverRunnable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Tools {
    private  static final HashMap<JavaPlugin, Tools> allTools = new HashMap<JavaPlugin, Tools>();
    public static Tools getTools(JavaPlugin plugin)
    {
        return allTools.get(plugin);
    }
    public static LibsFormattingTool getFormattingTool(JavaPlugin plugin) {
        return allTools.get(plugin).FormattingTool;
    }
    public static LibsEntityTool getEntityTool(JavaPlugin plugin) {
        return allTools.get(plugin).EntityTool;
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
    public static LibsHologramTool getHologramTool(JavaPlugin plugin) {
        return allTools.get(plugin).hologramTool;
    }
    public static LibsAdvancedRecipeTool getRecipeTool(JavaPlugin plugin) {
        return allTools.get(plugin).RecipeTool;
    }
    private final JavaPlugin plugin;
    private final int spigotID;
    private final LibsFormattingTool FormattingTool;
    private final LibsEntityTool EntityTool;
    private final LibsBlockTool BlockTool;
    private final LibsStructureTool structureTool;
    private final LibsMiscTool MiscTool;
    private final LibsItemStackTool ItemStackTool;
    private final LibsLocationTool LocationTool;
    private final LibsMessageTool MessageTool;
    private final LibsNBTTool NBTTool;
    private final LibsSerializeTool SerializeTool;
    private final LibsSkullTool SkullTool;
    private final LibsVaultTool VaultTool;
    private final LibsPlayerTool PlayerTool;
    private final LibsHologramTool hologramTool;
    private final LibsAdvancedRecipeTool RecipeTool;
    private static final SaveRunnable saver = new SaveRunnable();
    protected static Tools tools;

    public Tools(JavaPlugin plugin, TitanSaverRunnable titanSaverRunnable, int spigotID) {

        this.plugin = plugin;
        this.spigotID = spigotID;
        this.structureTool = new LibsStructureTool(this);
        this.EntityTool = new LibsEntityTool(this);
        this.BlockTool = new LibsBlockTool(this);
        this.hologramTool = new LibsHologramTool(this);
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
            Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBoxLibs.instants,  saver, TitanBoxLibs.instants.configManager.getSave_frequency(), TitanBoxLibs.instants.configManager.getSave_frequency());
        }
        if (spigotID > 0) TitanBoxLibs.autoUpdateManager.addPlugin(spigotID, plugin);

    }

    public int getSpigotID() {
        return spigotID;
    }

    public static void disablePlugin()
    {
        saver.run();
    }
    public JavaPlugin getPlugin() {
        return plugin;
    }

    public LibsStructureTool getStructureTool() {
        return structureTool;
    }

    public LibsAdvancedRecipeTool getRecipeTool() {
        return RecipeTool;
    }

    public LibsFormattingTool getFormattingTool() {
        return FormattingTool;
    }

    public LibsEntityTool getEntityTool() {
        return EntityTool;
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

    public LibsHologramTool getHologramTool() {
        return hologramTool;
    }
}
