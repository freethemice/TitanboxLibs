package com.firesoftitan.play.titanbox.libs.runnables;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.managers.HologramManager;
import com.firesoftitan.play.titanbox.libs.managers.TitanBlockManager;
import com.firesoftitan.play.titanbox.libs.tools.LibsHologramTool;
import org.bukkit.plugin.java.JavaPlugin;

public class MySaveRunnable extends TitanSaverRunnable{

    public MySaveRunnable(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void run() {
        TitanBoxLibs.barcodeManager.save();
        TitanBoxLibs.workerManager.saveAll();
        HologramManager.saveAll();
        TitanBlockManager.saveAll();
    }

}
