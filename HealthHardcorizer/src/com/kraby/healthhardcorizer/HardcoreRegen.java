package com.kraby.healthhardcorizer;

import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

public class HardcoreRegen {
	
	private static final double MAX_HEALTH = 20.0d;
	private static final double HEALTH_OFFSET = 0.15d;	//HP that keeps life from being full, in order to make the regen effect tick.
	
	public static double getUnifiedHealth(Player p)
	{
		FileConfiguration config = MainHH.config;
		
		double uhealth = Math.min(p.getHealth(), MAX_HEALTH - HEALTH_OFFSET);

		int minimumFood = config.getInt("hardcore_regen.minimum_food");
		double foodPointWorth = config.getDouble("hardcore_regen.food_point_worth");
		
		int useableFood = Math.max(0, p.getFoodLevel()-minimumFood);
		uhealth += useableFood * foodPointWorth;
		
		return uhealth;
	}
	
	
	public static void setUnifiedHealth(Player p, double uhealth)
	{
		FileConfiguration config = MainHH.config;
		final double maxHealth = MAX_HEALTH-HEALTH_OFFSET;	//If we set it to 20, the regeneration effect won't tick when the life is full
		
		double uhealthNext = Math.max(0, uhealth-maxHealth);
		double heartsBar = uhealth - uhealthNext;
		p.setHealth(Math.max(0.001d, heartsBar));
		
		uhealth = uhealthNext;
		
	
		int minimumFood = config.getInt("hardcore_regen.minimum_food");
		double foodPointWorth = config.getDouble("hardcore_regen.food_point_worth");
		
		uhealthNext = Math.max(0, uhealth-(20-minimumFood));
		int foodBar = minimumFood + (int) ((uhealth - uhealthNext)/foodPointWorth);
		p.setFoodLevel(foodBar);
	}
	

	
	public static double increaseUnifiedHealth(Player p, double amount)
	{
		double newValue = Math.max(0, getUnifiedHealth(p) + amount);
		setUnifiedHealth(p, newValue);
		return newValue;
	}
	
	public static void updateUnifiedHealth(Player p)
	{
		setUnifiedHealth(p, getUnifiedHealth(p));
	}
}
