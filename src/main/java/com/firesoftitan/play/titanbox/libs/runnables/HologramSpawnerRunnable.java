package com.firesoftitan.play.titanbox.libs.runnables;

import com.firesoftitan.play.titanbox.libs.managers.HologramManager;
import org.bukkit.scheduler.BukkitRunnable;

public class HologramSpawnerRunnable extends BukkitRunnable {
    @Override
    public void run() {
        HologramManager.run();
    }
}
