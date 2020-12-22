package org.kraby.mcarcinizer;

import java.util.ArrayList;
import java.util.List;
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


    protected void unregisterListeners() {
        for (Listener l : listeners) {
            HandlerList.unregisterAll(l);
        }
    }

    protected void registerListeners() {
        for (Listener l : listeners) {
            Bukkit.getPluginManager().registerEvents(l, getOwner());
        }
    }


    /**
     * Reload this subplugin's config.
     */
    public abstract void reloadConfig();

    protected abstract void createListeners();
}
