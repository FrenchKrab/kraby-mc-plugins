package com.kraby.deathkeeper;

import java.util.Random;

import org.bukkit.Bukkit;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.kraby.deathkeeper.commands.InventorySizeCommand;
import com.kraby.deathkeeper.commands.ReloadCommand;


public class MainClass extends JavaPlugin 
{
	
	public static MainClass singleton;
	public static FileConfiguration config;
	public static Random random = new Random();
	
	
	@Override
	public void onEnable()
	{
		singleton = this;
		
		//Add listeners
		Bukkit.getPluginManager().registerEvents(new DeathKeeperListener(), this);
	
		
		this.saveDefaultConfig();	//Create default config.yml if doesn't already exists
		config = MainClass.singleton.getConfig();
		
		
		this.getCommand("dkreload").setExecutor(new ReloadCommand());
		this.getCommand("ivs").setExecutor(new InventorySizeCommand());
	}
	
	
	@Override
	public void onDisable()
	{

	}
	
	public void reload()
	{
    	this.reloadConfig();
    	MainClass.config = this.getConfig();
		
		//If custom regen plugin enabled, remove natural regeneration
		//getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamerule naturalRegeneration " + Boolean.toString(!config.getBoolean(("custom_regen.enabled"))).toLowerCase());
	}
}
