package com.firesoftitan.play.titanbox.libs.managers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;
public class MemoryManager {

    public static String bytesToHumanReadable(long bytes) {
        if (bytes <  0) {
            throw new IllegalArgumentException("Bytes cannot be negative");
        }
        if (bytes <  1024) {
            return bytes + " B";
        }
        int unitIdx = (63 - Long.numberOfLeadingZeros(bytes)) /  10;
        return formatSize(bytes,  1L << (unitIdx *  10), " KMGTPE".charAt(unitIdx) + "iB");
    }

    private static String formatSize(long size, long unit, String unitName) {
        return DecimalFormat.getInstance().format((double) size / unit) + " " + unitName;
    }


    public static long getPluginMemoryUsage(Plugin plugin) {
        // Get the plugin's class loader
        ClassLoader classLoader = plugin.getClass().getClassLoader();

        // Get the memory usage of the class loader

        // Return the memory usage
        return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed();
    }
    public static int getNumberBukkitThreads(Plugin plugin) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        List<BukkitTask> pendingTasks = scheduler.getPendingTasks();
        List<BukkitWorker> activeWorkers = scheduler.getActiveWorkers();

        int count =  0;

        // Count pending tasks
        for (BukkitTask task : pendingTasks) {
            if (task.getOwner().equals(plugin)) {
                count++;
            }
        }

        // Count active tasks
        for (BukkitWorker worker : activeWorkers) {
            if (worker.getOwner().equals(plugin)) {
                count++;
            }
        }

        return count;
    }
    public static List<BukkitTask> getPluginPendingTask(Plugin plugin) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        List<BukkitTask> pendingTasks = scheduler.getPendingTasks();
        List<BukkitWorker> activeWorkers = scheduler.getActiveWorkers();

        List<BukkitTask> outTask = new ArrayList<BukkitTask>();

        // Count pending tasks
        for (BukkitTask task : pendingTasks) {
            if (task.getOwner().equals(plugin)) {
                outTask.add(task);
            }
        }
        return outTask;
    }
    public static List<BukkitWorker> getPluginRunningTask(Plugin plugin) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        List<BukkitTask> pendingTasks = scheduler.getPendingTasks();
        List<BukkitWorker> activeWorkers = scheduler.getActiveWorkers();

        List<BukkitWorker> outTask = new ArrayList<BukkitWorker>();

        // Count active tasks
        for (BukkitWorker worker : activeWorkers) {
            if (worker.getOwner().equals(plugin)) {
                outTask.add(worker);
            }
        }

        return outTask;
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
