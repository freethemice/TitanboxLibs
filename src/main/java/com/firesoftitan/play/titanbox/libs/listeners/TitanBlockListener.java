package com.firesoftitan.play.titanbox.libs.listeners;

import com.firesoftitan.play.titanbox.libs.blocks.TitanBlock;
import com.firesoftitan.play.titanbox.libs.events.TitanBlockEvent;
import com.firesoftitan.play.titanbox.libs.events.TitanBlockInteractEvent;
import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class TitanBlockListener {
    public abstract void onPlayerInteract(TitanBlockInteractEvent event);
    public abstract void onBlockPlace(TitanBlockEvent event);
    public abstract void onBlockBreak(TitanBlockEvent event);
}
