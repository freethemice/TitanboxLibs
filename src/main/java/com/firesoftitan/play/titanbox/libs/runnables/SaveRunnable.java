package com.firesoftitan.play.titanbox.libs.runnables;

import com.firesoftitan.play.titanbox.libs.tools.LibsFormattingTool;
import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.tools.LibsMessageTool;
import com.firesoftitan.play.titanbox.libs.tools.Tools;

import java.util.ArrayList;
import java.util.List;

public class SaveRunnable implements Runnable {

    private LibsMessageTool messageTool = Tools.getTools(TitanBoxLibs.instants).getMessageTool();
    private final List<TitanSaverRunnable> caller = new ArrayList<>();

    public void addSaveRunnable(TitanSaverRunnable saver)
    {
        caller.add(saver);
    }

    @Override
    public void run() {
        messageTool.sendMessageSystem("-------" + "Starting Save" + "-------");
        long startTime = System.currentTimeMillis();
        messageTool.sendMessageSystem("Core Save took: " +  TitanBoxLibs.tools.getFormattingTool().formatTime(System.currentTimeMillis() - startTime));
        for(TitanSaverRunnable call: caller)
        {
            try {
                long subStartTime = System.currentTimeMillis();
                call.run();
                LibsMessageTool messageTool =Tools.getTools(TitanBoxLibs.instants).getMessageTool();
                messageTool.sendMessageSystem(" Save took: " +  TitanBoxLibs.tools.getFormattingTool().formatTime(System.currentTimeMillis() - subStartTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        messageTool.sendMessageSystem( "Total Save took: " +  TitanBoxLibs.tools.getFormattingTool().formatTime(System.currentTimeMillis() - startTime));
        messageTool.sendMessageSystem( "-------" + "Save Finished" + "-------");
    }

}
