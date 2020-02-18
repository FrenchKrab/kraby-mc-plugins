package com.kraby.kurabase.tools;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHelper {
	public static float getLinearEquationValue(FileConfiguration config, String configString, float x)
	{
		float base = (float)config.getDouble(configString + ".base");
		float factor = (float)config.getDouble(configString + ".factor");
		float y = base + x*factor;
		if(config.contains(configString + ".max"))
		{
			y = Math.min(y, (float)config.getDouble(configString + ".max"));
		}
		if(config.contains(configString + ".min"))
		{
			y = Math.max(y, (float)config.getDouble(configString + ".min"));
		}
		return y;
	}
}
