package com.kraby.mcarcinizer.endfarms.config;


import com.kraby.mcarcinizer.endfarms.config.mobfarms.WitherConfig;
import com.kraby.mcarcinizer.utils.config.ConfigAccessor;
import com.kraby.mcarcinizer.utils.config.EnablableConfig;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class MobFarmsConfig extends ConfigAccessor implements EnablableConfig {
    private static final String CFG_ENABLED = "mobfarms.enabled";
    private static final String CFG_WITHER = "mobfarms.wither";


    public MobFarmsConfig(Configuration config) {
        super(config);
    }

    public MobFarmsConfig(Plugin plugin, String configFileName) {
        super(plugin, configFileName);
    }


    public boolean isEnabled() {
        return config.getBoolean(CFG_ENABLED, false);
    }

    /**
     * Retrieve the configuration regarding wither.
     * @return
     */
    public WitherConfig getWitherConfig() {
        ConfigurationSection section = config.getConfigurationSection(CFG_WITHER);
        if (section == null) {
            return null;
        }

        return new WitherConfig(section);
    }

}
