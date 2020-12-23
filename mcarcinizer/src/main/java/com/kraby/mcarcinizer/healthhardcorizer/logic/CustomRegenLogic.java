package com.kraby.mcarcinizer.healthhardcorizer.logic;

import com.kraby.mcarcinizer.healthhardcorizer.HealthHardcorizerSubplugin;
import com.kraby.mcarcinizer.healthhardcorizer.config.RegenConfig;
import org.bukkit.entity.Player;

public class CustomRegenLogic {
    private static final double MAX_HEALTH = 20.0d;

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

        int minimumFood = config.getMinimumFood();
        double foodPointWorth = config.getFoodPointWorth();
        int useableFood = Math.max(0, p.getFoodLevel() - minimumFood);

        double uhealth = Math.min(p.getHealth(), MAX_HEALTH - HEALTH_OFFSET);
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
        final double maxHealth = MAX_HEALTH - HEALTH_OFFSET;

        double uhealthNext = Math.max(0, uhealth - maxHealth);
        double heartsBar = uhealth - uhealthNext;
        p.setHealth(Math.max(0.001d, heartsBar));

        uhealth = uhealthNext;

        int minimumFood = config.getMinimumFood();
        double foodPointWorth = config.getFoodPointWorth();

        uhealthNext = Math.max(0, uhealth - (20 - minimumFood));
        int foodBar = minimumFood + (int) ((uhealth - uhealthNext) / foodPointWorth);
        p.setFoodLevel(foodBar);
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
