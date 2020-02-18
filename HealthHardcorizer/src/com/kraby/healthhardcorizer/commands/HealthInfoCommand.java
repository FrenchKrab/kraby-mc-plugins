package com.kraby.healthhardcorizer.commands;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kraby.healthhardcorizer.MainHH;

public class HealthInfoCommand implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	if(sender instanceof Player)
    	{
    		Player p = (Player)sender;
    		double maxLife = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        	sender.sendMessage("life:" + p.getHealth() + "/" + maxLife + " food:" + p.getFoodLevel() + " saturation:" + p.getSaturation());
    	}
    	

        return true;
    }
}