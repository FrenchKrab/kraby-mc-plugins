package com.kraby.deathkeeper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Statistic;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.kraby.kurabase.tools.ConfigHelper;
import com.kraby.kurabase.tools.InventorySerializer;



public class DeathKeeperListener implements Listener {
	
	
	@EventHandler
	public void onPlayerDie(PlayerDeathEvent e)
	{
		Player p = e.getEntity();
		
		//Deathkeeper spawning
		if(MainClass.config.getBoolean("deathkeeper.enabled"))
		{
			ItemStack[] inventory = e.getDrops().toArray(new ItemStack[0]);
			
			//(nombre de mort  *nombre d'items * temps de jeu avant dernière mort * niveau xp)/temps de jeu global * 0.1
			/*float deathKeeperLevel = Math.max(((Math.max(1,p.getStatistic(Statistic.TIME_SINCE_DEATH)/20/3600))
												* Math.max(1,inventory.length)
												* p.getStatistic(Statistic.DEATHS)
												* Math.max(1,p.getLevel()) )
												/ Math.max(1,(p.getStatistic(Statistic.PLAY_ONE_MINUTE)/20/3600))
												* 0.1f, 1);
			*/
			
			// ((xp/1000) +valeur_inv)*50 + (nb_morts^0.5) * 5 * min(h_depuis_derniere_mort, 1) * (nb_morts/h_de_jeu)
			//64 blocs differents; 4488
			//1 epee enchantée nommée A: 904
			//1 epee enchantée 20 char : 924
			float inventoryRatio = InventorySerializer.itemStackArrayToBase64(p.getInventory().getContents()).length() * 1.0f / 4488f;
			float deathKeeperLevel = ((p.getTotalExperience()*1.0f/1000) + inventoryRatio) * 60f;
			deathKeeperLevel += Math.pow(p.getStatistic(Statistic.DEATHS), 0.5d) * 5 
								* Math.max(1, p.getStatistic(Statistic.TIME_SINCE_DEATH)*1.0f/20/3600)  ///WARNING
								* (p.getStatistic(Statistic.DEATHS)/Math.max(1,p.getStatistic(Statistic.PLAY_ONE_MINUTE)/20/3600));
	
	        float deathKeeperNewLife = ConfigHelper.getLinearEquationValue(MainClass.config, "deathkeeper.life", deathKeeperLevel);
	        float deathKeeperAttack = ConfigHelper.getLinearEquationValue(MainClass.config, "deathkeeper.damage", deathKeeperLevel);
	        float deathKeeperSpeed = ConfigHelper.getLinearEquationValue(MainClass.config, "deathkeeper.movement_speed", deathKeeperLevel);
			
	        LivingEntity deathKeeper = DeathKeeper.create(p.getLocation(),
					deathKeeperAttack,
					deathKeeperNewLife,
					deathKeeperSpeed,
					inventory,
					p.getEquipment().getArmorContents(),
					p.getName(),
					e);
			
			if(deathKeeperLevel >= MainClass.config.getInt("deathkeeper.eternal_keeper_level"))
			{
		        deathKeeper.setCustomName(p.getName() + "'s EternalKeeper Lv." + ((int)deathKeeperLevel));
		        deathKeeper.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 1));
		        deathKeeper.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1));
		        deathKeeper.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, Integer.MAX_VALUE, 1));
			}
			else
			{
		        deathKeeper.setCustomName(p.getName() + "'s DeathKeeper Lv." + ((int)deathKeeperLevel));
			}

	        deathKeeper.setCustomNameVisible(true);
	        
	        e.getDrops().clear();
		}
	}
	
	
	@EventHandler
	public void onEntityDie(EntityDeathEvent e)
	{
		LivingEntity entity = e.getEntity();
		
		if(MainClass.config.getBoolean("deathkeeper.enabled") && DeathKeeper.isDeathKeeper(e.getEntity()))
		{
			ItemStack[] newDrops = DeathKeeper.getInventory(entity);
			
			e.getDrops().clear();
			if(newDrops != null && newDrops.length != 0)
			{
				e.getDrops().addAll(Arrays.asList(newDrops));
			}

			//Remove curse of vanishing items
			e.getDrops().removeIf(d -> d.getEnchantments().containsKey(Enchantment.VANISHING_CURSE) && d.getType() != Material.ENCHANTED_BOOK);
			
			//Get time since spawn
			NamespacedKey key_spawntime = new NamespacedKey(MainClass.singleton, DeathKeeper.NBT_SPAWNMILLIS);
			long spawnMillis = entity.getPersistentDataContainer().getOrDefault(key_spawntime, PersistentDataType.LONG, 0L);
			
			if(spawnMillis != 0)
			{
		        Date resultdate = new Date(spawnMillis);
		        
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat hoursFormat = new SimpleDateFormat("HH'h'mm");
		        String dateString = dateFormat.format(resultdate);
		        String hoursString = hoursFormat.format(resultdate);
		        
		        MainClass.singleton.getServer().broadcastMessage(
		        		ChatColor.YELLOW + e.getEntity().getCustomName() + ChatColor.GRAY +", spawned the " +
		        		ChatColor.YELLOW + dateString + ChatColor.GRAY +
		        		" at " + ChatColor.YELLOW + hoursString + ChatColor.GRAY + " has been killed !");
			}
		}
	}
	
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e)
	{
		if(DeathKeeper.isDeathKeeper(e.getEntity()))
		{
			if(e.getCause() == DamageCause.SUFFOCATION)
			{
				e.setCancelled(true);
			}
		}
	}
	
	
	

	
}

