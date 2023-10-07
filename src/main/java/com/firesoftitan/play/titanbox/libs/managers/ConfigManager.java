package com.firesoftitan.play.titanbox.libs.managers;

import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;

public class ConfigManager {
    private long save_frequency = 12000;

    public ConfigManager() {
        SaveManager configFile = new SettingsManager(TitanBoxLibs.instants.getName(), "config");
        if (!configFile.contains("settings.save.frequency"))
        {
            configFile.set("settings.save.frequency", save_frequency);
        }
        save_frequency = configFile.getLong("settings.save.frequency");


        configFile.save();
    }
    public long getSave_frequency() {
        return save_frequency;
    }
}
