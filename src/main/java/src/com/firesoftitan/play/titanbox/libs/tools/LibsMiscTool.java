package src.com.firesoftitan.play.titanbox.libs.tools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import src.com.firesoftitan.play.titanbox.libs.enums.BarcodeDeviceEnum;
import src.com.firesoftitan.play.titanbox.libs.interfaces.CommandInterface;
import src.com.firesoftitan.play.titanbox.libs.managers.BarcodeManager;
import src.com.firesoftitan.play.titanbox.libs.managers.WorkerManager;
import src.com.firesoftitan.play.titanbox.libs.runnables.SaveRunnable;
import src.com.firesoftitan.play.titanbox.libs.runnables.TitanSaverRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LibsMiscTool {
    private LibsMiscTool(){}


    public static HashMap<String, CommandInterface> commandInterfaces = new HashMap<>();
    public static SaveRunnable saver = new SaveRunnable();
    public static BarcodeManager barcodeManager;
    public static WorkerManager workerManager;

    public static boolean chance(int max, int percentage) {
        if (max < 1) return false;
        Random dice = new Random(System.currentTimeMillis());
        return dice.nextInt(max) <= percentage;
    }







    public static void runCommands(Player player, int amount, Location location, List<String> commands)
    {
        for(String command: commands)
        {
            runCommand(player, amount, location, command);
        }
    }
    public static void runCommand(Player player, int amount, Location location, String commands)
    {
        commands = replaceAllPlaceHolders(player, amount, location, commands);
        if (commands.startsWith("@player "))
        {
            commands = commands.replace("@player ", "");
            player.sendMessage(commands);
        }
        else
        {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands);
        }
    }
    public static String replaceAllPlaceHolders(Player player, int amount, Location location, String command)
    {
        if (player != null) {
            command = command.replace("<name>", player.getName());
            command = command.replace("<uuid>", player.getUniqueId().toString());
        }
        if (location != null) {
            command = command.replace("<x>", location.getBlockX() + "");
            command = command.replace("<y>", location.getBlockY() + "");
            command = command.replace("<z>", location.getBlockZ() + "");
            command = command.replace("<world>", LibsLocationTool.getWorldName(location));
        }
        command = command.replace("<amount>", amount +"");
        command = ChatColor.translateAlternateColorCodes('&', command);
        return command;
    }

    public static boolean isMonster(Entity entity)
    {
        if(entity instanceof Monster) return true;

        EntityType type = entity.getType();
        switch (type)
        {
            case CAVE_SPIDER:
            case PHANTOM:
            case ENDERMAN:
            case PIGLIN:
            case BLAZE:
            case CREEPER:
            case DROWNED:
            case ELDER_GUARDIAN:
            case ENDERMITE:
            case EVOKER:
            case GHAST:
            case GUARDIAN:
            case HOGLIN:
            case HUSK:
            case MAGMA_CUBE:
            case PIGLIN_BRUTE:
            case PILLAGER:
            case RAVAGER:
            case SHULKER:
            case SILVERFISH:
            case SKELETON:
            case SKELETON_HORSE:
            case SLIME:
            case SPIDER:
            case STRAY:
            case VEX:
            case VINDICATOR:
            case WITCH:
            case WITHER_SKELETON:
            case ZOGLIN:
            case ZOMBIE:
            case ZOMBIE_VILLAGER:
            case WITHER:
            case ENDER_DRAGON:
            case GIANT:
            case ZOMBIE_HORSE:
            case ILLUSIONER:
            case ZOMBIFIED_PIGLIN:
                return true;
        }
        if(type == EntityType.RABBIT)
        {
            Rabbit rabbit = (Rabbit)entity;
            return rabbit.getRabbitType() == Rabbit.Type.THE_KILLER_BUNNY;
        }

        return false;
    }
    public static boolean runCommandListener(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (cmd.getLabel().length() > 0) {
            if (LibsMiscTool.commandInterfaces.containsKey(cmd.getLabel())) {
                CommandInterface commandInterface = LibsMiscTool.commandInterfaces.get(cmd.getLabel());
                boolean ranCommand = commandInterface.onCommand(sender, cmd, args);
                if (!ranCommand) commandInterface.help(sender);
                return true;
            }
        }
        return false;
    }
    public static void addCommandListener(String name, CommandInterface yours)
    {
        commandInterfaces.put(name, yours);
    }

    public static void addSaveRunnable(TitanSaverRunnable saver)
    {
        LibsMiscTool.saver.addSaveRunnable(saver);
    }




    public static ItemStack getHeartPart()
    {
        String Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmM4ZmI2MzdkNmUxYTdiYThmYTk3ZWU5ZDI5MTVlODQzZThlYzc5MGQ4YjdiZjYwNDhiZTYyMWVlNGQ1OWZiYSJ9fX0=";
        ItemStack placeMe = LibsSkullTool.getSkull(Texture, "player_heart_quarter", false);
        placeMe = LibsItemStackTool.changeName(placeMe, ChatColor.RED + "Player Heart Quarter");
        placeMe = LibsItemStackTool.addLore(placeMe,  ChatColor.AQUA + "Collect four and get 1 heart added to your health!", ChatColor.AQUA + "Hold in main hand and right-click to use.");
        placeMe =  LibsMiscTool.barcodeManager.getNewBarcode(placeMe);
        return  placeMe.clone();

    }
    public static void markHeartPartUsed(Player player, ItemStack item )
    {
        if (isHeartPart(item).isGood()) {
            LibsMiscTool.barcodeManager.setBarcodeTrue(item, player);
        }
    }
    public static BarcodeDeviceEnum isHeartPart(ItemStack item )
    {
        if (item != null) {
            String titanID = LibsSkullTool.getSkullTitanID(item);
            if (titanID != null && titanID.length() > 0) {
                if (titanID.equals("player_heart_quarter")) {
                    if (!LibsMiscTool.barcodeManager.hasBarcode(item))
                    {
                        //"This is an invalid device! Most likely an old one.");
                        return BarcodeDeviceEnum.INVALID;
                    }
                    String barcode = LibsMiscTool.barcodeManager.scanBarcode(item);
                    if (barcode == null)
                    {
                        // "This is an invalid device! Most likely an old one.");
                        return BarcodeDeviceEnum.INVALID;
                    }
                    boolean barcodeTrue = Boolean.parseBoolean(barcode);
                    if (barcodeTrue)
                    {
                        //TitanBox.duppedAlert(player, item);
                        //add dupped alert later
                        return BarcodeDeviceEnum.DUPPED;
                    }
                    return BarcodeDeviceEnum.VALID;
                }
            }
        }
        return BarcodeDeviceEnum.NONE;
    }
    public static ItemStack getUpgradeDevice(Player player)
    {
        String Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTc4MzQ3NzUzMzZhMmY1Mzg2NzM5OTFiZTA3NmM4NzRjN2ZhZDExYjdiN2Y0ZjMyMTQzYjYzM2UwNDMxMjEifX19";
        ItemStack placeMe = LibsSkullTool.getSkull(Texture, "upgrade_device", false);
        placeMe = LibsItemStackTool.changeName(placeMe, ChatColor.YELLOW + "Upgrade Device");
        placeMe = LibsItemStackTool.addLore(placeMe,  "Used On: " + ChatColor.WHITE + "Storage Unit, and Routers", ChatColor.WHITE + "Hold in main hand and click block thats placed!");
        placeMe =  LibsMiscTool.barcodeManager.getNewBarcode(placeMe);
        placeMe = LibsItemStackTool.addLore(placeMe, ChatColor.YELLOW  + "Made By: " + ChatColor.WHITE + player.getName());
        return  placeMe.clone();

    }
    public static void markUpgradeDeviceUsed(Player player, ItemStack item )
    {
        if (isUpgradeDevice(item).isGood()) {
            LibsMiscTool.barcodeManager.setBarcodeTrue(item, player);
        }
    }
    public static BarcodeDeviceEnum isUpgradeDevice(ItemStack item )
    {
        if (item != null) {
            if (item.hasItemMeta()) {
                if (item.getItemMeta() != null && item.getItemMeta().hasDisplayName()) {
                    if (item.getItemMeta().getDisplayName().startsWith(ChatColor.YELLOW + "Upgrade Device")) {
                        if (! LibsMiscTool.barcodeManager.hasBarcode(item))
                        {
                            //"This is an invalid device! Most likely an old one.");
                            return BarcodeDeviceEnum.INVALID;
                        }
                        String barcode =  LibsMiscTool.barcodeManager.scanBarcode(item);
                        if (barcode == null)
                        {
                            // "This is an invalid device! Most likely an old one.");
                            return BarcodeDeviceEnum.INVALID;
                        }
                        boolean barcodeTrue = Boolean.parseBoolean(barcode);
                        if (barcodeTrue)
                        {
                            //TitanBox.duppedAlert(player, item);
                            //add dupped alert later
                            return BarcodeDeviceEnum.DUPPED;
                        }
                        return BarcodeDeviceEnum.VALID;
                    }
                }
            }
        }
        return BarcodeDeviceEnum.NONE;
    }

    public static boolean rollDice(float percent)
    {
        Random dice = new Random(System.currentTimeMillis());
        int diceRoll = dice.nextInt(1000);
        int odds = (int) (percent*10);
        return diceRoll < odds;
    }






    public static boolean isAir(Material material)
    {
        return material == Material.AIR || material == Material.CAVE_AIR;
    }

}