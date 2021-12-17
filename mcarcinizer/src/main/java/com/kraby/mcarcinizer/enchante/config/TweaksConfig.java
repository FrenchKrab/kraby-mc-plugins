package com.kraby.mcarcinizer.enchante.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.kraby.mcarcinizer.CarcinizerMain;
import com.kraby.mcarcinizer.enchante.config.betteranvil.ItemAttributeEnchanterData;
import com.kraby.mcarcinizer.enchante.config.betteranvil.ItemRepairCostReducerData;
import com.kraby.mcarcinizer.utils.config.ConfigAccessor;
import com.kraby.mcarcinizer.utils.config.EnablableConfig;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.Plugin;

public class TweaksConfig extends ConfigAccessor implements EnablableConfig {
    private static final String CFG_ENABLED = "tweaks.enabled";
    private static final String CFG_MENDING_BOUND_ENCHANTS = "tweaks.mending_bound_enchants";


    public TweaksConfig(Configuration config) {
        super(config);
    }

    public TweaksConfig(Plugin plugin, String configFileName) {
        super(plugin, configFileName);
    }


    public boolean isEnabled() {
        return config.getBoolean(CFG_ENABLED, false);
    }

    /**
     * Returns the list of enchantment to bind to items getting enchanted with mending.
     * @return
     */
    public List<Enchantment> getMendingBoundEnchants() {
        List<String> bound = config.getStringList(CFG_MENDING_BOUND_ENCHANTS);
        List<Enchantment> result = new ArrayList<>();

        for (String enchantName : bound) {
            Enchantment e = Enchantment.getByKey(NamespacedKey.minecraft(enchantName));
            if (e != null) {
                result.add(e);
            }
        }

        return result;
    }

}
