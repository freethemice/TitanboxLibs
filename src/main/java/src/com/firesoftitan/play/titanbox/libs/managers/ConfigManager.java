package src.com.firesoftitan.play.titanbox.libs.managers;

import src.com.firesoftitan.play.titanbox.libs.TitanBoxLibsPlugin;

public class ConfigManager {
    private long save_frequancy = 12000;

    public ConfigManager() {
        SaveManager configFile = new SaveManager(TitanBoxLibsPlugin.instants.getName(), "config");
        if (!configFile.contains("settings.save.frequance"))
        {
            configFile.set("settings.save.frequance", save_frequancy);
        }
        save_frequancy = configFile.getLong("settings.save.frequance");


        configFile.save();
    }
    public long getSave_frequancy() {
        return save_frequancy;
    }
}
