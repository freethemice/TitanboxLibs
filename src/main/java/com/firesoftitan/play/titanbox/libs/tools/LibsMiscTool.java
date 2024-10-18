package com.firesoftitan.play.titanbox.libs.tools;

import com.firesoftitan.play.titanbox.libs.interfaces.CommandInterface;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LibsMiscTool {
    private Tools parent;

    public LibsMiscTool(Tools parent) {
        this.parent = parent;

    }

    public HashMap<String, CommandInterface> commandInterfaces = new HashMap<>();



    public boolean chance(int max, int percentage) {
        if (max < 1) return false;
        Random dice = new Random(System.currentTimeMillis());
        return dice.nextInt(max) <= percentage;
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }





    public void runCommands(Player player, int amount, Location location, List<String> commands)
    {
        for(String command: commands)
        {
            runCommand(player, amount, location, command);
        }
    }
    public void runCommand(Player player, int amount, Location location, String commands)
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
    public String replaceAllPlaceHolders(Player player, int amount, Location location, String command)
    {
        if (player != null) {
            command = command.replace("<name>", player.getName());
            command = command.replace("<uuid>", player.getUniqueId().toString());
        }
        if (location != null) {
            command = command.replace("<x>", location.getBlockX() + "");
            command = command.replace("<y>", location.getBlockY() + "");
            command = command.replace("<z>", location.getBlockZ() + "");
            command = command.replace("<world>", Tools.tools.getLocationTool().getWorldName(location));
        }
        command = command.replace("<amount>", amount +"");
        command = ChatColor.translateAlternateColorCodes('&', command);
        return command;
    }

    public boolean isMonster(Entity entity)
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
            case WARDEN:
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
    public boolean runCommandListener(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!cmd.getLabel().isEmpty()) {
            if (Tools.tools.getMiscTool().commandInterfaces.containsKey(cmd.getLabel())) {
                CommandInterface commandInterface = Tools.tools.getMiscTool().commandInterfaces.get(cmd.getLabel());
                boolean ranCommand = commandInterface.onCommand(sender, cmd, args);
                if (!ranCommand) commandInterface.help(sender);
                return true;
            }
        }
        return false;
    }
    public void addCommandListener(String name, CommandInterface yours)
    {
        commandInterfaces.put(name, yours);
    }



    public boolean rollDice(float percent)
    {
        Random dice = new Random(System.currentTimeMillis());
        int diceRoll = dice.nextInt(1000);
        int odds = (int) (percent*10);
        return diceRoll < odds;
    }






    public boolean isAir(Material material)
    {
        return material == Material.AIR || material == Material.CAVE_AIR;
    }

}
