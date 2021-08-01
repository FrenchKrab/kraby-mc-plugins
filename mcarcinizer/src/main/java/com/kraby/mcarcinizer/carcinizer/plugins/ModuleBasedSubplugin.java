package com.kraby.mcarcinizer.carcinizer.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kraby.mcarcinizer.CarcinizerMain;
import com.kraby.mcarcinizer.utils.config.Config;
import com.kraby.mcarcinizer.utils.config.EnablableConfig;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public abstract class ModuleBasedSubplugin implements Subplugin {

    private final Plugin owner;

    private Config mainConfig;
    private Map<String, List<Listener>> modulesListeners;
    private Map<String, Config> modulesConfigs;

    protected ModuleBasedSubplugin(Plugin owner) {
        this.owner = owner;
        modulesListeners = createModulesListeners();
    }

    /**
     * Create a map that associate a module ID with its listeners.
     * @return
     */
    protected abstract Map<String, List<Listener>> createModulesListeners();

    /**
     * Create a map that associate a module ID with its config. The config decides whether
     * the module is enabled or not if it implements {@link EnablableConfig}.
     * @return
     */
    protected abstract Map<String, Config> createModulesConfigs();

    /**
     * Create the main config, if it implements {@link EnablableConfig}, will decide if the plugin
     * is enabled or not.
     * @return
     */
    protected abstract Config createMainConfig();

    public Config getMainConfig() {
        return mainConfig;
    }

    public Config getModuleConfig(String moduleId) {
        return modulesConfigs.get(moduleId);
    }


    @Override
    public Plugin getOwner() {
        return owner;
    }

    @Override
    public void reload() {
        // 1) Unload
        unregisterListeners();

        // 2) Load
        int enabledModules = 0;
        mainConfig = createMainConfig();

        // doesn't load each module listener/config if the maine module is disabled
        if (mainConfig instanceof EnablableConfig
                && !((EnablableConfig)mainConfig).isEnabled()) {
            String.format("%s subplugin disabled !",
                getDisplayName()
            );
            return;
        }

        modulesConfigs = createModulesConfigs();
        for (String moduleId : modulesListeners.keySet()) {
            if (modulesConfigs.containsKey(moduleId)
                    && modulesConfigs.get(moduleId) instanceof EnablableConfig
                    && !((EnablableConfig)modulesConfigs.get(moduleId)).isEnabled()) {
                continue;
            }

            for (Listener l : modulesListeners.get(moduleId)) {
                Bukkit.getPluginManager().registerEvents(l, getOwner());
            }

            enabledModules += 1;
        }

        CarcinizerMain.getSingleton().getLogger().info(
            String.format("%s subplugin enabled ! [%d/%d modules]",
                getDisplayName(),
                enabledModules,
                modulesListeners.size())
        );
    }

    @Override
    public List<Config> getConfigs() {
        List<Config> cfgs = new ArrayList<>();
        cfgs.add(mainConfig);
        cfgs.addAll(modulesConfigs.values());
        return cfgs;
    }


    private void unregisterListeners() {
        for (String moduleId : modulesListeners.keySet()) {
            for (Listener l : modulesListeners.get(moduleId)) {
                HandlerList.unregisterAll(l);
            }
        }
    }
}
