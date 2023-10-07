package com.firesoftitan.play.titanbox.libs.listeners;

import com.firesoftitan.play.titanbox.libs.events.TitanBlockEvent;
import com.firesoftitan.play.titanbox.libs.events.TitanBlockInteractEvent;

public abstract class TitanBlockListener {
    public abstract void onPlayerInteract(TitanBlockInteractEvent event);
    public abstract void onBlockPlace(TitanBlockEvent event);
    public abstract void onBlockBreak(TitanBlockEvent event);
}
