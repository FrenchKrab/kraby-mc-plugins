package com.kraby.mcarcinizer.mcarcinizer.commands;

import com.kraby.mcarcinizer.CarcinizerMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import net.md_5.bungee.api.ChatColor;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CarcinizerMain.getSingleton().reloadSubplugin();
        sender.sendMessage(ChatColor.GREEN + "MCarcinizer configs reloaded");
        return true;
    }

}
