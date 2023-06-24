package com.firesoftitan.play.titanbox.libs.managers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class MemoryManager {

    public static long getPluginMemoryUsage(Plugin plugin) {
        // Get the plugin's class loader
        ClassLoader classLoader = plugin.getClass().getClassLoader();

        // Get the memory usage of the class loader
        long memoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed();

        // Return the memory usage
        return memoryUsage;
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
        long executionTime = System.currentTimeMillis() - startTime;

        return executionTime;
    }
    public static Plugin[] getPlugins()
    {
        Plugin[] plugins = Bukkit.getServer().getPluginManager().getPlugins();
        return plugins;
    }
}
