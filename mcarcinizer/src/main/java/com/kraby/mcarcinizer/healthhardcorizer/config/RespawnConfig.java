package com.kraby.mcarcinizer.healthhardcorizer.config;

import com.kraby.mcarcinizer.utils.config.ConfigAccessor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class RespawnConfig extends ConfigAccessor {
    private static final String CFG_MODULE_ENABLED = "hardcore_respawn.enabled";
    private static final String CFG_LIFE = "hardcore_respawn.life";
    private static final String CFG_FOOD = "hardcore_respawn.food";
    private static final String CFG_RESPAWN_MESSAGE = "hardcore_respawn.respawn_message";

    public RespawnConfig(FileConfiguration config) {
        super(config);
    }


    public RespawnConfig(Plugin plugin, String configFileName) {
        super(plugin, configFileName);
    }


    public boolean isEnabled() {
        return config.getBoolean(CFG_MODULE_ENABLED, false);
    }

    public double getLife() {
        return config.getDouble(CFG_LIFE, 20.0);
    }

    public int getFood() {
        return config.getInt(CFG_FOOD, 20);
    }

    public String getRespawnMessage() {
        return config.getString(CFG_RESPAWN_MESSAGE, "");
    }
}
