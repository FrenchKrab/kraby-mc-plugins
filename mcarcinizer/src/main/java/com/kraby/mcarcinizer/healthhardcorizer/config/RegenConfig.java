package com.kraby.mcarcinizer.healthhardcorizer.config;

import com.kraby.mcarcinizer.utils.config.ConfigAccessor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class RegenConfig extends ConfigAccessor {
    private static final String CFG_MODULE_ENABLED = "hardcore_regen.enabled";
    private static final String CFG_MIN_FOOD = "hardcore_regen.minimum_food";
    private static final String CFG_FOOD_POINT_WORTH = "hardcore_regen.food_point_worth";

    public RegenConfig(FileConfiguration config) {
        super(config);
    }


    public RegenConfig(Plugin plugin, String configFileName) {
        super(plugin, configFileName);
    }


    public boolean isEnabled() {
        return config.getBoolean(CFG_MODULE_ENABLED, false);
    }

    /**
     * Returns how much food will be kept in all times.
     * @return
     */
    public int getMinimumFood() {
        return config.getInt(CFG_MIN_FOOD, 20);
    }

    /**
     * Returns how much health points a food point is worth.
     * @return
     */
    public double getFoodPointWorth() {
        return config.getDouble(CFG_FOOD_POINT_WORTH, 1.0);
    }
}
