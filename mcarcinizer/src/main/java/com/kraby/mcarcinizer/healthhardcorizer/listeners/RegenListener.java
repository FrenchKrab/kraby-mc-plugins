package com.kraby.mcarcinizer.healthhardcorizer.listeners;

import com.kraby.mcarcinizer.healthhardcorizer.HealthHardcorizerSubplugin;
import com.kraby.mcarcinizer.healthhardcorizer.logic.CustomRegenLogic;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.plugin.Plugin;


public class RegenListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityReceiveDamage(EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.PLAYER) {
            CustomRegenLogic.applyDamageEventPlayerUnifiedHealth(e);
        }
    }


    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (e.getEntityType() == EntityType.PLAYER) {
            Player p = (Player)e.getEntity();

            Plugin plugin = HealthHardcorizerSubplugin.getSingleton().getOwner();
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                () -> CustomRegenLogic.updateUnifiedHealth(p)
            );
        }
    }


    @EventHandler
    public void onRegeneration(EntityRegainHealthEvent e) {
        RegainReason reason = e.getRegainReason();
        if (e.getEntityType() == EntityType.PLAYER && (reason != RegainReason.SATIATED)) {
            Player p = ((Player)e.getEntity());
            double restoredHP = e.getAmount();
            e.setCancelled(true);

            CustomRegenLogic.increaseUnifiedHealth(p, restoredHP);
        }
    }
}
