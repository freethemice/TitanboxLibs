package com.firesoftitan.play.titanbox.libs.tools;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LibsVaultTool {
    private final Tools parent;

    public LibsVaultTool(Tools parent) {
        this.parent = parent;
        this.setup();
    }

    private Economy econ = null;
    private Permission perms;
    private Chat chat = null;

    public void setup()
    {
        if ( this.isVaultInstalled()) {
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
    public Economy getEconomy() {
        return econ;
    }

    public Permission getPermissions() {
        return perms;
    }

    public Chat getChat() {
        return chat;
    }

    public boolean isVaultInstalled()
    {
        Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
        return vault != null;
    }
}
