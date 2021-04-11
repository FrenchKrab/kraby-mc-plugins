package com.kraby.mcarcinizer;

import java.util.ArrayList;
import java.util.List;
import com.kraby.mcarcinizer.utils.config.ConfigAccessor;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public abstract class Subplugin {

    private final Plugin owner;

    protected List<Listener> listeners = new ArrayList<>();



    protected Subplugin(Plugin owner) {
        this.owner = owner;
        createListeners();
    }


    public Plugin getOwner() {
        return owner;
    }


    protected void unregisterAllListeners() {
        for (Listener l : listeners) {
            HandlerList.unregisterAll(l);
        }
    }

    /**
     * Registers all listeners contained in the listeners variable.
     */
    protected void registerAllListeners() {
        for (Listener l : listeners) {
            Bukkit.getPluginManager().registerEvents(l, getOwner());
        }
    }

    protected void registerListener(Listener l) {
        Bukkit.getPluginManager().registerEvents(l, getOwner());
    }

    /**
     * Get this subplugin's name (should be unique).
     * @return
     */
    public abstract String getName();

    /**
     * Reload this subplugin's config, listeners, etc.
     */
    public abstract void reload();

    public abstract List<ConfigAccessor> getConfigAccessors();

    protected abstract void createListeners();
}
