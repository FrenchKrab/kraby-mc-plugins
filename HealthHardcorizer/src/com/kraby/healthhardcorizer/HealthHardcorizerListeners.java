package com.kraby.healthhardcorizer;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent.Action;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.kraby.deathkeeper.MainClass;

public class HealthHardcorizerListeners implements Listener {

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e)
	{
		final FileConfiguration config = MainHH.singleton.config;
		if(config.getBoolean("hardcore_respawn.enabled"))
		{
			
			final Player p = e.getPlayer();
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(MainHH.singleton, new Runnable() {
			    @Override
			    public void run() 
			    {
			    	
					p.setHealth(config.getDouble("hardcore_respawn.life"));
			    	p.setFoodLevel(config.getInt("hardcore_respawn.food"));
			    	
			    	p.sendMessage(config.getString("hardcore_respawn.respawn_message"));
			    }
			 });


		}
	}
	
	@EventHandler
	public void onEntityReceiveDamage(EntityDamageEvent e)
	{
		if(e.getEntityType() == EntityType.PLAYER)
		{
			Player p = (Player)e.getEntity();
			Bukkit.getScheduler().scheduleSyncDelayedTask(MainHH.singleton, new Runnable() {
			    @Override
			    public void run() 
			    {
						HardcoreRegen.ApplyHardcoreRegen(p);
						//HardcoreRegen.ApplyExtendedRegeneration(p, false);
			    }
			 });
		}
	}
	
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e)
	{
		if(e.getEntityType() == EntityType.PLAYER)
		{
			Player p = (Player)e.getEntity();
			Bukkit.getScheduler().scheduleSyncDelayedTask(MainHH.singleton, new Runnable() {
			    @Override
			    public void run() 
			    {
						HardcoreRegen.ApplyHardcoreRegen(p);
						//HardcoreRegen.ApplyExtendedRegeneration(p, false);
			    }
			 });

		}
	}
	
	
	@EventHandler
	public void onRegeneration(EntityRegainHealthEvent e)
	{
		if(e.getEntityType() == EntityType.PLAYER && (e.getRegainReason() == RegainReason.MAGIC ||
				e.getRegainReason() == RegainReason.MAGIC_REGEN))
		{
			Player p = ((Player)e.getEntity());
			double restoredHP = e.getAmount();
			e.setCancelled(true);
			//HardcoreRegen.ApplyExtendedRegeneration(p, true);
		}
		
	}
	
	@EventHandler
	public void test(EntityPotionEffectEvent e)
	{
		if(e.getEntityType() == EntityType.PLAYER)
		{
			Player p = ((Player)e.getEntity());
			//HardcoreRegen.ApplyExtendedRegeneration(p, false);
		}
	}
	
}
