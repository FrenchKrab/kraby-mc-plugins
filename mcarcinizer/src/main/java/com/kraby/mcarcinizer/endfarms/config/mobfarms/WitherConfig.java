package com.kraby.mcarcinizer.endfarms.config.mobfarms;

import org.bukkit.configuration.ConfigurationSection;

public class WitherConfig {
    public final boolean cancelSuffocatingDamages;
    public final double suffocatingTeleportRadius;

    public WitherConfig(ConfigurationSection section) {
        cancelSuffocatingDamages = section.getBoolean("cancel_suffocate_damage", false);
        suffocatingTeleportRadius = section.getDouble("suffocate_teleport_radius", 0.0);
    }
}
