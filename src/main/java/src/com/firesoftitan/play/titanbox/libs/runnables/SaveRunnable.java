package src.com.firesoftitan.play.titanbox.libs.runnables;

import src.com.firesoftitan.play.titanbox.libs.TitanBoxLibsPlugin;
import src.com.firesoftitan.play.titanbox.libs.tools.LibsFormattingTool;
import src.com.firesoftitan.play.titanbox.libs.tools.LibsMessageTool;

import java.util.ArrayList;
import java.util.List;

public class SaveRunnable implements Runnable {

    private final List<TitanSaverRunnable> caller = new ArrayList<>();

    public void addSaveRunnable(TitanSaverRunnable saver)
    {
        caller.add(saver);
    }

    @Override
    public void run() {
        LibsMessageTool.sendMessageSystem(TitanBoxLibsPlugin.instants, "-------" + "Starting Save" + "-------");
        long startTime = System.currentTimeMillis();
        LibsMessageTool.sendMessageSystem(TitanBoxLibsPlugin.instants, "Core Save took: " + LibsFormattingTool.formatTime(System.currentTimeMillis() - startTime));
        for(TitanSaverRunnable call: caller)
        {
            try {
                long subStartTime = System.currentTimeMillis();
                call.run();
                LibsMessageTool.sendMessageSystem(call.getPlugin(), " Save took: " + LibsFormattingTool.formatTime(System.currentTimeMillis() - subStartTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LibsMessageTool.sendMessageSystem(TitanBoxLibsPlugin.instants, "Total Save took: " + LibsFormattingTool.formatTime(System.currentTimeMillis() - startTime));
        LibsMessageTool.sendMessageSystem(TitanBoxLibsPlugin.instants, "-------" + "Save Finished" + "-------");
    }

}
