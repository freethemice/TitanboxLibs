package com.firesoftitan.play.titanbox.libs.listeners;

import com.firesoftitan.play.titanbox.libs.blocks.TitanBlock;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class TitanBlockListener {
    public abstract void onPlayerInteract(PlayerInteractEvent event, Location location, TitanBlock titanBlock);
}
