package com.kraby.kurabase;

import org.bukkit.plugin.java.JavaPlugin;


public class KurabaseMain extends JavaPlugin
{
	public static KurabaseMain singleton;
	
	@Override
	public void onEnable()
	{
		singleton = this;
	}
	
	
	@Override
	public void onDisable()
	{

	}
	
}