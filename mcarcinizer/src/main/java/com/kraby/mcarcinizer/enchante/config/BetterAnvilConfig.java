package com.kraby.mcarcinizer.enchante.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.kraby.mcarcinizer.enchante.config.betteranvil.ItemAttributeEnchanterData;
import com.kraby.mcarcinizer.enchante.config.betteranvil.ItemRepairCostReducerData;
import com.kraby.mcarcinizer.utils.config.ConfigAccessor;
import com.kraby.mcarcinizer.utils.config.EnablableConfig;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class BetterAnvilConfig extends ConfigAccessor implements EnablableConfig {
    private static final String CFG_ENABLED = "better_anvil.enabled";
    private static final String CFG_REPAIR_VALUES = "better_anvil.cost_reduction_items";
    private static final String CFG_ATT_ENCHANT_VALUES = "better_anvil.attribute_enchants";


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
     * Get data about how repair cost reduction associated with this item.
     * Values default to 0 if no data available.
     * @param itemName
     * @return
     */
    public ItemRepairCostReducerData getItemRepairCostReduceData(String itemName) {
        ConfigurationSection section = getItemSection(CFG_REPAIR_VALUES, itemName);
        if (section == null) {
            return null;
        }

        return new ItemRepairCostReducerData(section);
    }

    /**
     * Get data about the attribute enchantment data linked to this item.
     * @param itemName
     * @return
     */
    public List<ItemAttributeEnchanterData> getItemAttributeEnchantData(String itemName) {
        ConfigurationSection section = config.getConfigurationSection(
            CFG_ATT_ENCHANT_VALUES + "." + itemName);
        Set<String> keys = section.getKeys(false);

        List<ItemAttributeEnchanterData> result = new ArrayList<>();
        for (String key : keys) {
            ConfigurationSection section2 = section.getConfigurationSection(key);
            result.add(new ItemAttributeEnchanterData(section2));
        }

        return result;
    }


    private ConfigurationSection getItemSection(String root, String itemName) {
        String path = root + "." + itemName;
        return config.getConfigurationSection(path);
    }
}
