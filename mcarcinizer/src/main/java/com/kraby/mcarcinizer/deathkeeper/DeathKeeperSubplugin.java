package com.kraby.mcarcinizer.deathkeeper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kraby.mcarcinizer.carcinizer.plugins.ModuleBasedSubplugin;
import com.kraby.mcarcinizer.deathkeeper.config.DkConfig;
import com.kraby.mcarcinizer.deathkeeper.listeners.ClassicDkListener;
import com.kraby.mcarcinizer.utils.config.Config;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class DeathKeeperSubplugin extends ModuleBasedSubplugin {
    private static final String CONFIG_FILE_NAME = "deathkeeper.yml";
    private static final String MODULE_ID_DK = "dk";

    private static DeathKeeperSubplugin singleton;

    /**
     * Create the subplugin. Should only be created once (singleton) (... not really clean).
     * @param owner
     */
    public DeathKeeperSubplugin(Plugin owner) {
        super(owner);
        if (setSingleton(this)) {
            reload();
        }
    }

    private static boolean setSingleton(DeathKeeperSubplugin singleton) {
        if (DeathKeeperSubplugin.singleton == null) {
            DeathKeeperSubplugin.singleton = singleton;
            return true;
        } else {
            return false;
        }
    }

    public static DeathKeeperSubplugin getSingleton() {
        return singleton;
    }

    public DkConfig getDkConfig() {
        return (DkConfig)getMainConfig();
    }

    @Override
    public String getName() {
        return "DeathKeeper";
    }

    @Override
    public String getDisplayName() {
        return "DeathKeeper";
    }

    @Override
    protected Map<String, List<Listener>> createModulesListeners() {
        Map<String, List<Listener>> map = new HashMap<>();
        map.put(MODULE_ID_DK, List.of(new ClassicDkListener()));
        return map;
    }

    @Override
    protected Map<String, Config> createModulesConfigs() {
        return new HashMap<>();
    }

    @Override
    protected Config createMainConfig() {
        return new DkConfig(getOwner(), CONFIG_FILE_NAME);
    }
}
