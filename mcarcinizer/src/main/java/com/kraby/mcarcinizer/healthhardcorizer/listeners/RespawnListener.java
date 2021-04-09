package com.kraby.mcarcinizer.healthhardcorizer.listeners;

import org.bukkit.event.Listener;
import com.kraby.mcarcinizer.healthhardcorizer.HealthHardcorizerSubplugin;
import com.kraby.mcarcinizer.healthhardcorizer.config.RespawnConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

public class RespawnListener implements Listener {


    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        final RespawnConfig config = HealthHardcorizerSubplugin.getSingleton().getRespawnConfig();
        if (!config.isEnabled()) {
            return;
        }

        final Player p = e.getPlayer();
        // If the player is NOT dead ! (teleporting back from The End is very common)
        if (p.getHealth() > 0.0) {
            return;
        }

        Plugin plugin = HealthHardcorizerSubplugin.getSingleton().getOwner();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
            () -> {
                p.setHealth(config.getLife());
                p.setFoodLevel(config.getFood());
                p.sendMessage(config.getRespawnMessage());
            }
        );
    }
}
