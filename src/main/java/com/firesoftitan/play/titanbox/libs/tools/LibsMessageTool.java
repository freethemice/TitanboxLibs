package com.firesoftitan.play.titanbox.libs.tools;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class LibsMessageTool {
    private Tools parent;
    protected LibsMessageTool(Tools parent)
    {
        this.parent = parent;
    }
    public void sendMessageSystem(String message)
    {
        sendMessageSystem(message, Level.INFO);
    }
    public void sendMessageSystem(String message, Level level)
    {
        String subName = this.parent.getPlugin().getName().replaceFirst("TitanBox", "");
        subName = subName.replaceFirst("Titan", "");
        parent.getPlugin().getLogger().log(level,   ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', message));
     //   System.out.println("[SY" + subName + "]: " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', message));
    }
    public void sendMessagePlayer(Player player, List<String> messages)
    {
        String subName = this.parent.getPlugin().getName().replaceFirst("TitanBox", "");
        subName = subName.replaceFirst("Titan", "");
        String messageHeaderFooter = ChatColor.GRAY + "" + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "-------------" + ChatColor.RESET + ChatColor.GREEN + "[" + ChatColor.BLUE + "TitanBox" + ChatColor.GREEN + "](" + ChatColor.AQUA + subName + ChatColor.GREEN + ")" + ChatColor.GRAY + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "-------------";
        if (player == null || !player.isOnline())
        {
            this.sendMessageSystem(messageHeaderFooter);
            for(String s: messages) {
                this.sendMessageSystem(ChatColor.translateAlternateColorCodes('&', s));
            }
            this.sendMessageSystem(ChatColor.GRAY + "" +ChatColor.BOLD +  ChatColor.STRIKETHROUGH + "-------------" + "-------------");
            return;
        }

        player.sendMessage(messageHeaderFooter);
        for(String s: messages) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        }
        player.sendMessage(ChatColor.GRAY + "" +ChatColor.BOLD +  ChatColor.STRIKETHROUGH + "-------------" + "-------------");
    }
    public void sendMessagePlayer(Player player, String... messages)
    {
        List<String> mes = new ArrayList<>(Arrays.asList(messages));
        sendMessagePlayer(player, mes);
    }
    public void sendMessagePlayer(Player player, String message)
    {
        sendMessagePlayer(player, message, true);
    }
    public void sendMessagePlayer(Player player, String message, boolean colorcode)
    {
        if (player == null || !player.isOnline())
        {
            this.sendMessageSystem(message);
            return;
        }
        String subName = this.parent.getPlugin().getName().replace("TitanBox", "").replace("Titan", "");
        String messageout = ChatColor.translateAlternateColorCodes('&', message);
        if (!colorcode) messageout = message;
        player.sendMessage(ChatColor.GREEN + "[" + ChatColor.BLUE + "TitanBox" + ChatColor.GREEN + "]("+ ChatColor.AQUA + subName + ChatColor.GREEN + "): " + ChatColor.RESET + messageout);
    }
}
