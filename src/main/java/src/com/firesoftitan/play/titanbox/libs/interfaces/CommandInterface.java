package src.com.firesoftitan.play.titanbox.libs.interfaces;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface CommandInterface {
    boolean onCommand(CommandSender sender, Command cmd, String[] args);
    void help(CommandSender player);
}
