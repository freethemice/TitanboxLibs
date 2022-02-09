package src.com.firesoftitan.play.titanbox.libs.runnables;

import src.com.firesoftitan.play.titanbox.libs.TitanBoxLibsPlugin;
import src.com.firesoftitan.play.titanbox.libs.tools.LibsFormattingTool;
import src.com.firesoftitan.play.titanbox.libs.tools.LibsMessageTool;

import java.util.ArrayList;
import java.util.List;

public class SaveRunnable implements Runnable {

    private LibsMessageTool messageTool = LibsMessageTool.getMessageTool(TitanBoxLibsPlugin.instants);
    private final List<TitanSaverRunnable> caller = new ArrayList<>();

    public void addSaveRunnable(TitanSaverRunnable saver)
    {
        caller.add(saver);
    }

    @Override
    public void run() {
        messageTool.sendMessageSystem("-------" + "Starting Save" + "-------");
        long startTime = System.currentTimeMillis();
        messageTool.sendMessageSystem("Core Save took: " + LibsFormattingTool.formatTime(System.currentTimeMillis() - startTime));
        for(TitanSaverRunnable call: caller)
        {
            try {
                long subStartTime = System.currentTimeMillis();
                call.run();
                LibsMessageTool messageTool = LibsMessageTool.getMessageTool(call.getPlugin());
                messageTool.sendMessageSystem(" Save took: " + LibsFormattingTool.formatTime(System.currentTimeMillis() - subStartTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        messageTool.sendMessageSystem( "Total Save took: " + LibsFormattingTool.formatTime(System.currentTimeMillis() - startTime));
        messageTool.sendMessageSystem( "-------" + "Save Finished" + "-------");
    }

}
