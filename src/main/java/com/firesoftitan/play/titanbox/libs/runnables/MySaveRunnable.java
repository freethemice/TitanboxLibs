package com.firesoftitan.play.titanbox.libs.runnables;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.tools.LibsMiscTool;
import org.bukkit.plugin.java.JavaPlugin;

public class MySaveRunnable extends TitanSaverRunnable{

    public MySaveRunnable(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void run() {
         TitanBoxLibs.tools.getMiscTool().barcodeManager.save();
         TitanBoxLibs.tools.getMiscTool().workerManager.saveAll();
    }

}
