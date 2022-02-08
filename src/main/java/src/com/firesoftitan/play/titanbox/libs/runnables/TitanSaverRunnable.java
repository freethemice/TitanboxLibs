package src.com.firesoftitan.play.titanbox.libs.runnables;

import org.bukkit.plugin.java.JavaPlugin;

public class TitanSaverRunnable implements Runnable {
    protected JavaPlugin plugin;
    private TitanSaverRunnable()
    {

    }
    public TitanSaverRunnable(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public String getPluginName() {
        return plugin.getName();
    }
    @Override
    public void run() {

    }
}
