package com.kraby.mcarcinizer.enchante.config;

import com.kraby.mcarcinizer.utils.config.ConfigAccessor;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class GeneralConfig extends ConfigAccessor {
    private static final String CFG_PLUGIN_ENABLED = "plugin_enabled";

    public GeneralConfig(FileConfiguration config) {
        super(config);
    }


    public GeneralConfig(Plugin plugin, String configFileName) {
        super(plugin, configFileName);
    }


    public boolean isPluginEnabled() {
        return config.getBoolean(CFG_PLUGIN_ENABLED, false);
    }
}
