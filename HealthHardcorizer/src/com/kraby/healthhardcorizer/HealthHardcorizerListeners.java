package com.kraby.healthhardcorizer;

import java.net.http.WebSocket.Listener;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;


public class HealthHardcorizerListeners implements Listener {

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e)
	{
		final FileConfiguration config = MainHH.config;
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


	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityReceiveDamage(EntityDamageEvent e)
	{
		if(e.getEntityType() == EntityType.PLAYER)
		{
			Player p = (Player)e.getEntity();
			double damages = e.getFinalDamage();

			double newLife = HardcoreRegen.getUnifiedHealth(p)-damages;
			if (newLife > 0.0)
			{
				e.setDamage(0.0);
				HardcoreRegen.setUnifiedHealth(p, newLife);
			}
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
			    	HardcoreRegen.updateUnifiedHealth(p);
			    }
			 });

		}
	}


	@EventHandler
	public void onRegeneration(EntityRegainHealthEvent e)
	{
		RegainReason reason = e.getRegainReason();
		if(e.getEntityType() == EntityType.PLAYER && (
				reason != RegainReason.SATIATED))
		{
			Player p = ((Player)e.getEntity());
			double restoredHP = e.getAmount();
			e.setCancelled(true);

			HardcoreRegen.increaseUnifiedHealth(p, restoredHP);
		}

	}


	@EventHandler
	public void onPotionEffectApplied(EntityPotionEffectEvent e)
	{
		if(e.getEntityType() == EntityType.PLAYER )
		{
			Player p = ((Player)e.getEntity());
		}
	}

}
