package com.kraby.healthhardcorizer.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.kraby.healthhardcorizer.MainHH;


public class ReloadCommand implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    	MainHH.singleton.reloadConfig();
    	MainHH.singleton.config = MainHH.singleton.getConfig();
    	
    	sender.sendMessage(ChatColor.YELLOW + "HealthHardcorizer reloaded !");
        return true;
    }
}