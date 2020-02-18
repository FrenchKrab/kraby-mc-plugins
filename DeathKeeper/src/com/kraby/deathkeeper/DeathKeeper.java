package com.kraby.deathkeeper;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.kraby.kurabase.tools.InventorySerializer;

public class DeathKeeper
{
	public static final String NBT_ISDEATHKEEPER = "is_deathkeeper";
	public static final String NBT_INVENTORY = "deathkeeper_inventory";
	public static final String NBT_SPAWNMILLIS = "deathkeeper_spawntime";
	
	
	public static LivingEntity create(Location location, double attack, double life, double speed, ItemStack[] inventory, ItemStack[] armor, String owner, PlayerDeathEvent deathEvent)
	{
		LivingEntity deathKeeper = (LivingEntity) location.getWorld().spawnEntity(location, EntityType.WITHER_SKELETON);
		
		PersistentDataContainer dataContainer = deathKeeper.getPersistentDataContainer();
		//set as a deathkeeper
		NamespacedKey isDeathKeeperKey = new NamespacedKey(MainClass.singleton, DeathKeeper.NBT_ISDEATHKEEPER);
		dataContainer.set(isDeathKeeperKey, PersistentDataType.BYTE, (byte)0);
		
		//store death date
		long timeMillis = System.currentTimeMillis();
        NamespacedKey timeKey = new NamespacedKey(MainClass.singleton, DeathKeeper.NBT_SPAWNMILLIS);
        dataContainer.set(timeKey, PersistentDataType.LONG, timeMillis);
		
        //Set inventory
        String inventoryBase64 = InventorySerializer.itemStackArrayToBase64(inventory);
        NamespacedKey inventoryKey = new NamespacedKey(MainClass.singleton, DeathKeeper.NBT_INVENTORY);
        dataContainer.set(inventoryKey, PersistentDataType.STRING, inventoryBase64);
        

        //Set armor
        deathKeeper.getEquipment().setArmorContents(armor);
        deathKeeper.getEquipment().setBootsDropChance(1);
        deathKeeper.getEquipment().setChestplateDropChance(1);
        deathKeeper.getEquipment().setHelmetDropChance(1);
        deathKeeper.getEquipment().setLeggingsDropChance(1);
        deathKeeper.getEquipment().setItemInMainHandDropChance(1);
        deathKeeper.getEquipment().setItemInOffHandDropChance(1);
        //Set custom stats
        deathKeeper.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(life);
        deathKeeper.setHealth(life);	//Attention, si vie > 2048, crash
        deathKeeper.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
        
        deathKeeper.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(attack);

        
        deathKeeper.setRemoveWhenFarAway(false);


		return deathKeeper;
	}
	
	
	public static ItemStack[] getInventory(Entity entity)
	{

		NamespacedKey key_inventory = new NamespacedKey(MainClass.singleton, DeathKeeper.NBT_INVENTORY);
		String inventoryString = entity.getPersistentDataContainer().getOrDefault(key_inventory, PersistentDataType.STRING, null);

		ItemStack[] newDrops = null;
		try {
			newDrops = InventorySerializer.itemStackArrayFromBase64(inventoryString);
		} catch (IOException e1) {
			MainClass.singleton.getServer().broadcastMessage(ChatColor.RED + "DEATHKEEPER ERROR CONVERTING , INVENTORY LOST || "+inventoryString);
			newDrops = null;
		}
		
		return newDrops;
	}
	
	
	public static boolean isDeathKeeper(Entity entity)
	{
		PersistentDataContainer container = entity.getPersistentDataContainer();
		NamespacedKey isDeathKeeperKey = new NamespacedKey(MainClass.singleton, DeathKeeper.NBT_ISDEATHKEEPER);
		return container.has(isDeathKeeperKey, PersistentDataType.BYTE);
	}
}
