package com.kraby.mcarcinizer.deathkeeper.listeners;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import com.kraby.mcarcinizer.CarcinizerMain;
import com.kraby.mcarcinizer.deathkeeper.DeathKeeperSubplugin;
import com.kraby.mcarcinizer.deathkeeper.config.DkConfig;
import com.kraby.mcarcinizer.deathkeeper.data.ClassicDkBuilder;
import com.kraby.mcarcinizer.deathkeeper.data.DeathKeeperData;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.BanList.Type;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
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

public class ClassicDkListener implements Listener {


    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDie(PlayerDeathEvent e) {
        final DeathKeeperSubplugin singleton = DeathKeeperSubplugin.getSingleton();
        final DkConfig config = singleton.getDkConfig();

        //Deathkeeper spawning
        final Player p = e.getEntity();

        final ItemStack[] inventory = e.getDrops().toArray(new ItemStack[0]);

        final double deathKeeperLevel = Math.ceil(config.getDeathKeeperLevel(p));

        final double newLife = config.getDeathKeeperHp(deathKeeperLevel);
        final double attack = config.getDeathKeeperAttack(deathKeeperLevel);
        final double speed = config.getDeathKeeperSpeed(deathKeeperLevel);

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

        //ban
        if (config.isBanOnDeathEnabled()) {
            final double banTime = config.getDeathBanTime(p, deathKeeperLevel);

            if (banTime > 0) {
                Instant expireInstant = Instant.now().plusSeconds((long) banTime);
                Date expireDate = Date.from(expireInstant);
                String banMessage = config.getDeathBanMessage(p, expireDate, e.getDeathMessage());

                final BanList banlist = singleton.getOwner().getServer().getBanList(Type.NAME);
                banlist.addBan(
                    p.getName(),
                    banMessage,
                    expireDate,
                    DeathKeeperSubplugin.getSingleton().getName());

                Bukkit.getScheduler().runTask(
                    CarcinizerMain.getSingleton(),
                    () -> p.kickPlayer(banMessage));
            }
        }
    }


    @EventHandler
    public void onEntityDie(EntityDeathEvent e) {
        final DkConfig config = DeathKeeperSubplugin.getSingleton().getDkConfig();
        final LivingEntity entity = e.getEntity();

        if (!DeathKeeperData.isDeathKeeper(e.getEntity())) {
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
        final DkConfig config = DeathKeeperSubplugin.getSingleton().getDkConfig();
        if (!config.isPluginEnabled()) {
            return;
        }

        final Entity entity = e.getEntity();
        final DamageCause cause = e.getCause();

        if (DeathKeeperData.isDeathKeeper(entity)
                && (cause == DamageCause.SUFFOCATION
                || cause == DamageCause.LAVA
                || cause == DamageCause.FALL
                || cause == DamageCause.DROWNING)) {
            e.setCancelled(true);
        }
    }





}