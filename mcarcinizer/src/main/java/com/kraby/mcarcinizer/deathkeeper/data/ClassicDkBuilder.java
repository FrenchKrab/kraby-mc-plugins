package com.kraby.mcarcinizer.deathkeeper.data;

import com.kraby.mcarcinizer.deathkeeper.DeathKeeperSubplugin;
import com.kraby.mcarcinizer.deathkeeper.config.DkConfig;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *  Old/Classic DeathKeeper logic ported to MCarcinizer. To be depecrecated.
 */
public class ClassicDkBuilder {
    // (Location location, double attack, double life, double speed,
    // ItemStack[] inventory, ItemStack[] armor, String owner, PlayerDeathEvent deathEvent)
    private ItemStack[] armor = null;
    private ItemStack[] inventory = null;
    private double attack;
    private double life;
    private double speed;
    private String owner;
    private long level;


    public ClassicDkBuilder withArmor(final ItemStack[] armor) {
        this.armor = armor.clone();
        return this;
    }

    public ClassicDkBuilder withInventory(final ItemStack[] inv) {
        this.inventory = inv.clone();
        return this;
    }

    public ClassicDkBuilder withAttack(final double attack) {
        this.attack = attack;
        return this;
    }

    public ClassicDkBuilder withLife(final double life) {
        this.life = Math.min(2048.0, life);
        return this;
    }

    public ClassicDkBuilder withSpeed(final double speed) {
        this.speed = speed;
        return this;
    }

    public ClassicDkBuilder withOwner(final String owner) {
        this.owner = owner;
        return this;
    }

    public ClassicDkBuilder withLevel(final long level) {
        this.level = level;
        return this;
    }

    /**
     * Spawn a DeathKeeper at a given location.
     * @param entityType
     * @param location
     * @return
     */
    public LivingEntity spawnAt(final EntityType entityType, final Location location) {
        LivingEntity keeper = (LivingEntity) location.getWorld().spawnEntity(location, entityType);

        //set as a deathkeeper
        DeathKeeperData.tagAsDeathKeeper(keeper);

        //store death date
        long timeMillis = System.currentTimeMillis();
        DeathKeeperData.setSpawnTime(keeper, timeMillis);

        //Set inventory
        DeathKeeperData.setInventory(keeper, inventory);

        //Set owner
        DeathKeeperData.setOwner(keeper, owner);

        //Set level
        DeathKeeperData.setLevel(keeper, level);

        //Set armor
        EntityEquipment equipment = keeper.getEquipment();
        equipment.setArmorContents(armor);
        equipment.setBootsDropChance(1);
        equipment.setChestplateDropChance(1);
        equipment.setHelmetDropChance(1);
        equipment.setLeggingsDropChance(1);
        equipment.setItemInMainHandDropChance(1);
        equipment.setItemInOffHandDropChance(1);

        //Set custom stats
        keeper.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(life);
        keeper.setHealth(life);     //Attention, si vie > 2048, crash
        keeper.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
        keeper.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(attack);

        keeper.setRemoveWhenFarAway(false);


        DkConfig config = DeathKeeperSubplugin.getSingleton().getDkConfig();
        keeper.setCustomName(config.getDeathKeeperName(keeper));
        keeper.setCustomNameVisible(true);

        if (level >= config.getEternalKeeperLevel()) {
            keeper.addPotionEffect(
                new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 1));
            keeper.addPotionEffect(
                new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1));
            keeper.addPotionEffect(
                new PotionEffect(PotionEffectType.DOLPHINS_GRACE, Integer.MAX_VALUE, 1));
        }

        return keeper;
    }
}
