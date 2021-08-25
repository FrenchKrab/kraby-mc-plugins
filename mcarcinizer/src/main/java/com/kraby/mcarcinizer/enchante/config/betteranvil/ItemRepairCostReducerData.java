package com.kraby.mcarcinizer.enchante.config.betteranvil;

import org.bukkit.configuration.ConfigurationSection;

public class ItemRepairCostReducerData {
    public final int reduction;
    public final int cost;
    public final int count;

    /**
     * Load data about an item that reduces repair cost, from a {@link ConfigurationSection}.
     * @param section
     */
    public ItemRepairCostReducerData(ConfigurationSection section) {
        this.reduction = section.getInt("reduction", 1);
        this.cost = section.getInt("cost", 1);
        this.count = section.getInt("count", 1);
    }
}