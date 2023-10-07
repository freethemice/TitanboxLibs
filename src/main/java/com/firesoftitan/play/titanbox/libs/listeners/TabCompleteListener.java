package com.firesoftitan.play.titanbox.libs.listeners;


import com.firesoftitan.play.titanbox.libs.tools.LibsProtectionTool;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabCompleteListener implements TabCompleter {
    private static final String[] ADMIN_COMMANDS = {"protection"};
    private static final String[] NON_ADMIN_COMMANDS = {"wild"};
    private final List<String> pluginNames = new ArrayList<String>();
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> commands = new ArrayList<String>();
        if (args.length == 1) {
            if (LibsProtectionTool.isAdmin((Player) commandSender)) {
                commands.addAll(List.of(ADMIN_COMMANDS));
            }
            commands.addAll(List.of(NON_ADMIN_COMMANDS));

        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("protection"))
            {
                commands.add("list");
                commands.add("enabled");
                commands.add("add");
                commands.add("remove");
            }
        }
        if (args.length == 3)
        {
            if (args[0].equalsIgnoreCase("protection") && args[1].equalsIgnoreCase("add")) {
                List<String> stringListFromText = LibsProtectionTool.getListingProtectionPlugins((Player) commandSender);
                if (!stringListFromText.isEmpty()) commands.addAll(stringListFromText);
            }
            if (args[0].equalsIgnoreCase("protection") && args[1].equalsIgnoreCase("remove")) {
                List<String> stringListFromText = LibsProtectionTool.getEnabledProtectionPlugins((Player) commandSender);
                if (!stringListFromText.isEmpty()) commands.addAll(stringListFromText);
            }
        }
        //create a new array
        final List<String> completions = new ArrayList<>();
        //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
        StringUtil.copyPartialMatches(args[args.length - 1], commands, completions);
        //sort the list
        Collections.sort(completions);


        return completions;

    }
}
