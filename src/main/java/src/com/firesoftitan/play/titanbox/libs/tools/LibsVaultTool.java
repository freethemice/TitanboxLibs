package src.com.firesoftitan.play.titanbox.libs.tools;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LibsVaultTool {
    private LibsVaultTool(){}

    private static Economy econ = null;
    private static Permission perms;
    private static Chat chat = null;

    public static void setup()
    {
        if (LibsVaultTool.isVaultInstalled()) {
            RegisteredServiceProvider<Economy> rspV = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
            if (rspV != null) {
                econ = rspV.getProvider();
            }


            RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
            if (rsp != null) {
                chat = rsp.getProvider();
            }
        }

        RegisteredServiceProvider<Permission> rsp2 = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp2 != null) {
            perms = rsp2.getProvider();
        }
    }
    public static Economy getEconomy() {
        return econ;
    }

    public static Permission getPermissions() {
        return perms;
    }

    public static Chat getChat() {
        return chat;
    }

    public static boolean isVaultInstalled()
    {
        Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
        return vault != null;
    }
}
