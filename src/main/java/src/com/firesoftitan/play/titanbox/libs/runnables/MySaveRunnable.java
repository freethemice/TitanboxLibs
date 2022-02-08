package src.com.firesoftitan.play.titanbox.libs.runnables;

import org.bukkit.plugin.java.JavaPlugin;
import src.com.firesoftitan.play.titanbox.libs.tools.LibsMiscTool;

public class MySaveRunnable extends TitanSaverRunnable{

    public MySaveRunnable(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void run() {
        LibsMiscTool.barcodeManager.save();
        LibsMiscTool.workerManager.saveAll();
    }

}
