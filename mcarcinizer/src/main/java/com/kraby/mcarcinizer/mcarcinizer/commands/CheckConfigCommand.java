package com.kraby.mcarcinizer.mcarcinizer.commands;

import java.util.List;
import com.kraby.mcarcinizer.CarcinizerMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CheckConfigCommand implements CommandExecutor {
    public static final String COMMAND_STRING = "carcchkcfg";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<String> errors = CarcinizerMain.getSingleton().getConfigErrors();

        for (String error : errors) {
            sender.sendMessage(error);
        }

        if (errors.isEmpty()) {
            sender.sendMessage(ChatColor.GREEN + "Your config files are error free !");
        }

        return true;
    }
}
