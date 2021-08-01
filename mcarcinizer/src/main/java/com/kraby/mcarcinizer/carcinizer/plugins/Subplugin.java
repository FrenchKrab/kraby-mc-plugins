package com.kraby.mcarcinizer.carcinizer.plugins;

import java.util.List;

import com.kraby.mcarcinizer.utils.config.Config;

import org.bukkit.plugin.Plugin;

public interface Subplugin {

    Plugin getOwner();

    /**
     * Get this subplugin's display name.
     * @return
     */
    String getDisplayName();

    /**
     * Get this subplugin's name (should be unique, used as an unique ID).
     * @return
     */
    String getName();

    /**
     * Reload this subplugin's config, listeners, etc.
     */
    public abstract void reload();

    List<Config> getConfigs();
}
