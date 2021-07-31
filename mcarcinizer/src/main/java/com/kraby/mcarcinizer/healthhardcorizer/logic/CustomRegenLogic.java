package com.kraby.mcarcinizer.healthhardcorizer.logic;

import com.kraby.mcarcinizer.carcinizer.vanilla.ArmorDamageFormulas;
import com.kraby.mcarcinizer.healthhardcorizer.HealthHardcorizerSubplugin;
import com.kraby.mcarcinizer.healthhardcorizer.config.RegenConfig;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class CustomRegenLogic {
    // HP that keeps life from being full, in order to make the regen effect tick.
    private static final double HEALTH_OFFSET = 0.15d;


    private CustomRegenLogic() {
    }


    /**
     * Returns the current unified health of a given player.
     * It amounts to its current health points added to its usable food points (approximately).
     * @param p
     * @return
     */
    public static double getUnifiedHealth(Player p) {
        RegenConfig config = HealthHardcorizerSubplugin.getSingleton().getRegenConfig();
        final double maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

        int minimumFood = config.getMinimumFood();
        double foodPointWorth = config.getFoodPointWorth();
        int useableFood = Math.max(0, p.getFoodLevel() - minimumFood);

        double uhealth = Math.min(p.getHealth(), maxHealth - HEALTH_OFFSET);
        uhealth += useableFood * foodPointWorth;

        return uhealth;
    }

    /**
     * Set the new unified health of a given player.
     * This will try to keep the player's life at max value at the cost of food.
     * @param p
     * @param uhealth
     */
    public static void setUnifiedHealth(Player p, double uhealth) {
        RegenConfig config = HealthHardcorizerSubplugin.getSingleton().getRegenConfig();

        //If we set it to 20, the regeneration effect won't tick when the life is full
        final double maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        final double cappedMaxHealth = maxHealth - HEALTH_OFFSET;

        // put everything we can in life
        double uhealthNext = Math.max(0, uhealth - cappedMaxHealth);
        double heartsBar = uhealth - uhealthNext;
        p.setHealth(Math.max(0.001d, heartsBar));

        // now put the rest in food
        uhealth = uhealthNext;

        int minimumFood = config.getMinimumFood();
        double foodPointWorth = config.getFoodPointWorth();

        uhealthNext = Math.max(0, uhealth - (20 - minimumFood));
        int foodBar = minimumFood + (int) ((uhealth - uhealthNext) / foodPointWorth);
        p.setFoodLevel(foodBar);
    }

    /**
     * Apply a EntityDamageEvent to the player taking unified health into account.
     * @param e
     */
    public static void applyDamageEventPlayerUnifiedHealth(EntityDamageEvent e) {
        final RegenConfig config = HealthHardcorizerSubplugin.getSingleton().getRegenConfig();

        final Player p = (Player)e.getEntity();

        final double maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        // final double cappedMaxHealth = maxHealth - HEALTH_OFFSET;
        final int minimumFood = config.getMinimumFood();
        final double foodPointWorth = config.getFoodPointWorth();

        final double damages = e.getFinalDamage();

        final double oldUHealth = getUnifiedHealth(p);
        final double oldUFoodPart = Math.max(0, oldUHealth - maxHealth) / foodPointWorth;

        final double newUHealth = oldUHealth - damages;
        final double newUFoodPart = Math.max(0, newUHealth - maxHealth) / foodPointWorth;

        // We have a food part in the unified health => custom behaviour
        if (oldUFoodPart > 0) {
            double healed = (oldUFoodPart - newUFoodPart);
            // Some armor damage isn't applied due to the event damage being reduced/negated.
            // Track them & apply them manually.
            double manualArmorDamage = 0.0;

            if (damages > p.getHealth() && newUHealth > 0) {
                e.setDamage(p.getHealth() - HEALTH_OFFSET);
                manualArmorDamage = damages;
            } else {
                e.setDamage(damages - healed);
                manualArmorDamage = healed;
            }
            p.setFoodLevel(minimumFood + (int)(newUFoodPart / foodPointWorth));

            // Manual damages/durability on armor
            final EntityEquipment pEquipment = p.getEquipment();
            ItemStack[] newDamagedArmor = ArmorDamageFormulas.getDamagedArmorStack(
                pEquipment.getArmorContents(),
                manualArmorDamage,
                e.getCause());
            pEquipment.setArmorContents(newDamagedArmor);
        }
    }

    /**
     * Adds a given amount of life to the player's unified health.
     * @param p
     * @param amount
     * @return
     */
    public static double increaseUnifiedHealth(Player p, double amount) {
        double newValue = Math.max(0, getUnifiedHealth(p) + amount);
        setUnifiedHealth(p, newValue);
        return newValue;
    }

    /**
     * Force redistribute food points to health if needed.
     * @param p
     */
    public static void updateUnifiedHealth(Player p) {
        setUnifiedHealth(p, getUnifiedHealth(p));
    }
}
