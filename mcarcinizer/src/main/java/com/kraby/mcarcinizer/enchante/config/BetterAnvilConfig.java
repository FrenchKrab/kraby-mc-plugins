package com.kraby.mcarcinizer.enchante.config;

import java.util.HashMap;
import java.util.Map;

import com.kraby.mcarcinizer.CarcinizerMain;
import com.kraby.mcarcinizer.utils.config.ConfigAccessor;
import com.kraby.mcarcinizer.utils.config.EnablableConfig;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;

public class BetterAnvilConfig extends ConfigAccessor implements EnablableConfig {
    private static final String CFG_ENABLED = "better_anvil.enabled";
    private static final String CFG_VALUES = "better_anvil.values";


    public BetterAnvilConfig(Configuration config) {
        super(config);
    }

    public BetterAnvilConfig(Plugin plugin, String configFileName) {
        super(plugin, configFileName);
    }


    public boolean isEnabled() {
        return config.getBoolean(CFG_ENABLED, false);
    }

    /**
     * Get allowed items and their value.
     * @return
     */
    public Map<Material,Float> getItemsValue() {
        Map<Material,Float> m = new HashMap<>();
        Map<String, Object> rawValues = config.getConfigurationSection(CFG_VALUES)
            .getValues(false);
        for (String itemName : rawValues.keySet()) {
            CarcinizerMain.getSingleton().getLogger().info(itemName);
            if (rawValues.get(itemName) instanceof Integer) {
                CarcinizerMain.getSingleton().getLogger().info("=" + (int) rawValues.get(itemName));
            }
        }
        return m;
    }
}
