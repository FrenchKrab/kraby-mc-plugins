package org.kraby.mcarcinizer.deathkeeper.listeners;

import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.kraby.mcarcinizer.deathkeeper.DeathKeeperSubplugin;
import org.kraby.mcarcinizer.deathkeeper.config.DkConfiguration;
import org.kraby.mcarcinizer.deathkeeper.data.ClassicDkBuilder;
import org.kraby.mcarcinizer.deathkeeper.data.DeathKeeperData;

public class ClassicDkListener implements Listener {


    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDie(PlayerDeathEvent e) {
        DkConfiguration config = DeathKeeperSubplugin.getSingleton().getDkConfig();
        if (!config.isPluginEnabled()) {
            return;
        }

        //Deathkeeper spawning
        Player p = e.getEntity();

        ItemStack[] inventory = e.getDrops().toArray(new ItemStack[0]);

        double deathKeeperLevel = Math.ceil(config.getDeathKeeperLevel(p));

        double newLife = config.getDeathKeeperHp(deathKeeperLevel);
        double attack = config.getDeathKeeperAttack(deathKeeperLevel);
        double speed = config.getDeathKeeperSpeed(deathKeeperLevel);

        new ClassicDkBuilder()
            .withArmor(p.getEquipment().getArmorContents())
            .withInventory(inventory)
            .withAttack(attack)
            .withSpeed(speed)
            .withLife(newLife)
            .withOwner(p.getName())
            .withLevel((long)deathKeeperLevel)
            .spawnAt(EntityType.WITHER_SKELETON, p.getLocation());

        e.getDrops().clear();
    }


    @EventHandler
    public void onEntityDie(EntityDeathEvent e) {
        DkConfiguration config = DeathKeeperSubplugin.getSingleton().getDkConfig();
        LivingEntity entity = e.getEntity();

        if (!config.isPluginEnabled() || !DeathKeeperData.isDeathKeeper(e.getEntity())) {
            return;
        }

        ItemStack[] newDrops = DeathKeeperData.getInventory(entity);

        e.getDrops().clear();
        if (newDrops != null && newDrops.length != 0) {
            e.getDrops().addAll(Arrays.asList(newDrops));
        }

        //Remove curse of vanishing items
        e.getDrops().removeIf(
            d -> d.getEnchantments().containsKey(Enchantment.VANISHING_CURSE)
            && d.getType() != Material.ENCHANTED_BOOK);

        DeathKeeperSubplugin.getSingleton().getOwner().getServer().broadcastMessage(
            config.getDeathKeeperDeathMessage(entity));
    }


    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        DkConfiguration config = DeathKeeperSubplugin.getSingleton().getDkConfig();
        if (!config.isPluginEnabled()) {
            return;
        }

        if (e.getCause() == DamageCause.SUFFOCATION) {
            e.setCancelled(true);
        }
    }





}