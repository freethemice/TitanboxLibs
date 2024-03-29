package com.firesoftitan.play.titanbox.libs.listeners;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.tools.Tools;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.firesoftitan.play.titanbox.libs.TitanBoxLibs.instants;

public class PluginListener implements PluginMessageListener {

    private final Tools tools;
    public PluginListener(){
        tools = Tools.getTools(instants);
        if (isBungee())
        {
            this.registerEvents("titanbox:1");
            tools.getMessageTool().sendMessageSystem("Bungee cord server enabled.");
        }
    }
    public void registerEvents(String channel){
        Messenger pm = TitanBoxLibs.instants.getServer().getMessenger();
        pm.registerIncomingPluginChannel( TitanBoxLibs.instants, channel, this );
        pm.registerOutgoingPluginChannel( TitanBoxLibs.instants, channel);
    }
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if ( !channel.equalsIgnoreCase( "titanbox:1" ) ) return;

        ByteArrayDataInput in = ByteStreams.newDataInput( message );
        String subChannel = in.readUTF();
        if ( subChannel.equalsIgnoreCase( "chat" ) )
        {
            UUID uuid  = UUID.fromString(in.readUTF());
            String chatMessage = in.readUTF();
            Player playerTarget = Bukkit.getPlayer(uuid);
            if (playerTarget == null) return;
            Set<Player> set = new HashSet<Player>();
            set.add(playerTarget);
            AsyncPlayerChatEvent chatEvent = new AsyncPlayerChatEvent(false, playerTarget, chatMessage, set);
            HandlerList handlers = chatEvent.getHandlers();
            RegisteredListener[] registeredListeners = handlers.getRegisteredListeners();
            for(RegisteredListener listener: registeredListeners)
            {
                try {
                    //This prevents other plugins from adding crap to the messages
                    chatEvent = new AsyncPlayerChatEvent(false, playerTarget, chatMessage, set);
                    listener.callEvent(chatEvent);
                } catch (EventException e) {
                    e.printStackTrace();
                }
            }
            //Bukkit.getPluginManager().callEvent(chatEvent);

            // do things with the data
        }
    }
    public boolean isBungee()
    {
        ConfigurationSection settings = TitanBoxLibs.instants.getServer().spigot().getConfig();
        return settings.getBoolean("settings.bungeecord");
    }
}
