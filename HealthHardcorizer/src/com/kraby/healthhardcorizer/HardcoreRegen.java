package com.kraby.healthhardcorizer;

import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class HardcoreRegen {
	
	public static void ApplyHardcoreRegen(Player p)
	{
		FileConfiguration config = MainHH.config;
		final double maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		
		if(config.getBoolean("hardcore_regen.enabled") && !p.isDead() && p.getHealth() < maxHealth)
		{
			int requiredFood = config.getInt("hardcore_regen.required_food");
			if(p.getFoodLevel() >= requiredFood)
			{
				double lifeRegenerated = config.getDouble("hardcore_regen.life_regenerated");
				int foodConsummed = config.getInt("hardcore_regen.food_consummed");
				
				double neededLifeGain = Math.min(lifeRegenerated, maxHealth - p.getHealth());
				double foodCost = Math.ceil((neededLifeGain/lifeRegenerated) * foodConsummed);
				
				p.setHealth(p.getHealth() + neededLifeGain);
				p.setFoodLevel(p.getFoodLevel() - (int)foodCost);
				
				//in case we have enough food for multiple healing at once
				ApplyHardcoreRegen(p);
			}
		}
	}
	
	
	public static void ApplyExtendedRegenerationOLD(Player p)
	{
		assert(p.hasPotionEffect(PotionEffectType.REGENERATION));
		
		double maxLife = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		float maxSaturation = 20.0f;
		int maxFood = 20;
		
		if(MainHH.config.getBoolean("hardcore_regen.debug_messages"))
			MainHH.singleton.getServer().broadcastMessage("life:" + p.getHealth() + "/" + maxLife + " food:" + p.getFoodLevel() + " saturation:" + p.getSaturation());
		
		boolean canHealLife = p.getHealth() < (maxLife - 0.5d);
		boolean canHealFoodLevel = p.getFoodLevel() < maxFood && MainHH.config.getBoolean("hardcore_regen.regeneration_gives_food");
		boolean canHealSaturation = p.getSaturation() < maxSaturation && MainHH.config.getBoolean("hardcore_regen.regeneration_gives_saturation");
		
		if(!canHealLife)
		{
			if((p.getHealth() == maxLife) && (canHealFoodLevel || canHealSaturation))
			{
				p.setHealth(maxLife - 0.1d);
			}
			else if(canHealFoodLevel)
			{
				p.setFoodLevel(Math.min(p.getFoodLevel() + 1, maxFood));
			}
			else if(canHealSaturation)
			{
				p.setSaturation(Math.min(p.getSaturation() + 1.0f, maxSaturation));
			}
			else
			{
				p.setHealth(maxLife);
			}
		}
		else
		{
			double maxHealableLife = maxLife;
			if(canHealFoodLevel || canHealSaturation)
				maxHealableLife = maxLife - 0.1d;
			p.setHealth(Math.min(p.getHealth()+1.0d, maxHealableLife));
		}
	}
	
	
	public static void ApplyExtendedRegeneration(Player p, boolean healFrame)
	{
		assert(p.hasPotionEffect(PotionEffectType.REGENERATION));
		
		double maxLife = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		float maxSaturation = 20.0f;
		int maxFood = 20;
		


		
		boolean isLifeFull = (p.getHealth() == maxLife);
		boolean canHealLife = p.getHealth() < (maxLife - 0.2d);
		boolean canHealFoodLevel = p.getFoodLevel() < maxFood && MainHH.config.getBoolean("hardcore_regen.regeneration_gives_food");
		boolean canHealSaturation = p.getSaturation() < maxSaturation && MainHH.config.getBoolean("hardcore_regen.regeneration_gives_saturation");
		
		
		if(MainHH.config.getBoolean("hardcore_regen.debug_messages"))
		{
			MainHH.singleton.getServer().broadcastMessage(p.getName() + "]" + p.getHealth() + "/" + maxLife + " food:" + p.getFoodLevel() + " saturation:" + p.getSaturation());
			MainHH.singleton.getServer().broadcastMessage("we can heal: food=" + canHealFoodLevel + ", saturation=" + canHealSaturation);
		}
		
		
		if(healFrame)
		{
			if(!canHealLife)
			{
				if(canHealFoodLevel)
				{
					p.setFoodLevel(Math.min(p.getFoodLevel() + 1, maxFood));
				}
				else if(canHealSaturation)
				{
					p.setSaturation(Math.min(p.getSaturation() + 1.0f, maxSaturation));
				}
				else
				{
					p.setHealth(maxLife);
				}
			}
			else
			{
				double maxHealableLife = maxLife;
				//if(canHealFoodLevel || canHealSaturation)
				//	maxHealableLife = maxLife - 0.05d;
				p.setHealth(Math.min(p.getHealth()+1.0d, maxHealableLife));
			}
		}

		
		if(!healFrame && isLifeFull && (canHealFoodLevel || canHealSaturation))
		{
			if(MainHH.config.getBoolean("hardcore_regen.debug_messages"))
				MainHH.singleton.getServer().broadcastMessage(p.getName() + "] life reduced");
			p.setHealth(maxLife - 0.05d);
		}
	}
}
