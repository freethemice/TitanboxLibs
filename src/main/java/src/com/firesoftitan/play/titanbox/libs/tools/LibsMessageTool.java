package src.com.firesoftitan.play.titanbox.libs.tools;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class LibsMessageTool {
    private  static HashMap<JavaPlugin, LibsMessageTool> allTools = new HashMap<>();

    private JavaPlugin plugin;
    public static LibsMessageTool getMessageTool(JavaPlugin plugin)
    {
        return allTools.get(plugin);
    }
    public LibsMessageTool(JavaPlugin plugin)
    {
        this.plugin = plugin;
        allTools.put(plugin, this);
    }
    public void sendMessageSystem(String message)
    {
        sendMessageSystem(message, Level.INFO);
    }
    public void sendMessageSystem(String message, Level level)
    {
        plugin.getLogger().log(level,  ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', message));
    }
    public void sendMessagePlayer(Player player, List<String> messages)
    {
        String subName = this.plugin.getName().replaceFirst("TitanBox", "");
        subName = subName.replaceFirst("Titan", "");
        String messageHeaderFooter = ChatColor.GRAY + "" + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "-------------" + ChatColor.RESET + ChatColor.GREEN + "[" + ChatColor.BLUE + "TitanBox" + ChatColor.GREEN + "](" + ChatColor.AQUA + subName + ChatColor.GREEN + ")" + ChatColor.GRAY + "" + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "-------------";
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
        if (player == null || !player.isOnline())
        {
            this.sendMessageSystem(message);
            return;
        }
        String subName = this.plugin.getName().replace("TitanBox", "");
        player.sendMessage(ChatColor.GREEN + "[" + ChatColor.BLUE + "TitanBox" + ChatColor.GREEN + "]("+ ChatColor.AQUA + subName + ChatColor.GREEN + "): " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', message));
    }
}
