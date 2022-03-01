package com.firesoftitan.play.titanbox.libs.runnables;

import com.firesoftitan.play.titanbox.libs.tools.LibsFormattingTool;
import com.firesoftitan.play.titanbox.libs.TitanBoxLibs;
import com.firesoftitan.play.titanbox.libs.tools.LibsMessageTool;
import com.firesoftitan.play.titanbox.libs.tools.Tools;

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
        Tools.getTools(TitanBoxLibs.instants).getMessageTool().sendMessageSystem("-------" + "Starting Save" + "-------");
        long startTime = System.currentTimeMillis();
        LibsFormattingTool formattingTool = Tools.getFormattingTool(TitanBoxLibs.instants);
        Tools.getTools(TitanBoxLibs.instants).getMessageTool().sendMessageSystem("Core Save took: " +  formattingTool.formatTime(System.currentTimeMillis() - startTime));
        for(TitanSaverRunnable call: caller)
        {
            try {
                long subStartTime = System.currentTimeMillis();
                call.run();
                LibsMessageTool messageTool =Tools.getTools(TitanBoxLibs.instants).getMessageTool();
                messageTool.sendMessageSystem(" Save took: " +  formattingTool.formatTime(System.currentTimeMillis() - subStartTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Tools.getTools(TitanBoxLibs.instants).getMessageTool().sendMessageSystem( "Total Save took: " +  formattingTool.formatTime(System.currentTimeMillis() - startTime));
        Tools.getTools(TitanBoxLibs.instants).getMessageTool().sendMessageSystem( "-------" + "Save Finished" + "-------");
    }

}
