package com.firesoftitan.play.titanbox.libs.managers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class MemoryManager {

    public static long getPluginMemoryUsage(Plugin plugin) {
        // Get the plugin's class loader
        ClassLoader classLoader = plugin.getClass().getClassLoader();

        // Get the memory usage of the class loader

        // Return the memory usage
        return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed();
    }

    public static Thread[] getPluginThreads(Plugin plugin) {
        // Get all active threads
        Thread[] threads = new Thread[Thread.activeCount()];
        Thread.enumerate(threads);

        // Filter threads by plugin class loader
        ClassLoader classLoader = plugin.getClass().getClassLoader();
        List<Thread> pluginThreads = new ArrayList<>();
        for (Thread thread : threads) {
            if (thread != null && thread.getContextClassLoader() == classLoader) {
                pluginThreads.add(thread);
            }
        }

        // Convert list to array
        Thread[] pluginThreadArray = new Thread[pluginThreads.size()];
        pluginThreadArray = pluginThreads.toArray(pluginThreadArray);

        // Return the plugin's threads
        return pluginThreadArray;
    }

    public static long measureThreadExecutionTime(Thread thread) {
        // Start measuring execution time
        long startTime = System.currentTimeMillis();

        // Wait for the thread to finish
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Calculate the execution time

        return System.currentTimeMillis() - startTime;
    }
    public static Plugin[] getPlugins()
    {
        return Bukkit.getServer().getPluginManager().getPlugins();
    }
}
