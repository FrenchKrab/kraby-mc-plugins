package com.kraby.mcarcinizer.mcarcinizer;

import com.kraby.mcarcinizer.mcarcinizer.commands.CheckConfigCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class InfoLogs {

    /**
     * Warns the commandsender about configuration file errors presence.
     * @param s
     * @param errorCount
     */
    public static void warnIfConfigsContainErrors(CommandSender s, int errorCount) {
        if (errorCount <= 0) {
            return;
        }

        String message = String.format(
            "%sYour configuration files contain at least %d errors. "
            + "Type /%s to view a detailed list, fix them and reload.",
            ChatColor.RED, errorCount, CheckConfigCommand.COMMAND_STRING);
        s.sendMessage(message);
    }
}
