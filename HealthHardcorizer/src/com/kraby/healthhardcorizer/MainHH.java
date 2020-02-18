package com.kraby.healthhardcorizer;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.kraby.healthhardcorizer.commands.HealthInfoCommand;
import com.kraby.healthhardcorizer.commands.ReloadCommand;


public class MainHH extends JavaPlugin 
{
	public static MainHH singleton;
	public static FileConfiguration config;
	
	@Override
	public void onEnable()
	{
		singleton = this;
		
		//Add listeners
		Bukkit.getPluginManager().registerEvents(new HealthHardcorizerListeners(), this);
	
		
		this.saveDefaultConfig();	//Create default config.yml if doesn't already exists
		config = getConfig();
		
		
		this.getCommand("hhreload").setExecutor(new ReloadCommand());
		this.getCommand("hhinfo").setExecutor(new HealthInfoCommand());
	}
	
	
	@Override
	public void onDisable()
	{

	}
}
