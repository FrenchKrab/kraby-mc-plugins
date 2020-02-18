package com.kraby.deathkeeper.commands;

import java.lang.instrument.Instrumentation;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.kraby.deathkeeper.MainClass;
import com.kraby.kurabase.tools.InventorySerializer;

public class InventorySizeCommand implements CommandExecutor {


	public static class ObjectSizeFetcher {
	    private static Instrumentation instrumentation;
	
	    public static void premain(String args, Instrumentation inst) {
	        instrumentation = inst;
	    }
	
	    public static long getObjectSize(Object o) {
	        return instrumentation.getObjectSize(o);
	    }
	}
	
    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    	Player p = sender.getServer().getPlayer(sender.getName());
    	
    	String inventoryBase64 = InventorySerializer.itemStackArrayToBase64(p.getInventory().getContents());

    	sender.getServer().broadcastMessage("size b64: " + inventoryBase64.length());
    	
    	p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 4));
    	p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 2));
        return true;
    }
    
}