package com.kraby.deathkeeper.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.kraby.deathkeeper.MainClass;


public class ReloadCommand implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	MainClass.singleton.reload();
    	MainClass.singleton.reloadConfig();
    	sender.sendMessage(ChatColor.YELLOW + "KrabyHardcore reloaded");
        return true;
    }
}