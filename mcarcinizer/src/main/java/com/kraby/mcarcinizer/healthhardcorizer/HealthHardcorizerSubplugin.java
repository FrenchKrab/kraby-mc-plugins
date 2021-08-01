package com.kraby.mcarcinizer.healthhardcorizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kraby.mcarcinizer.carcinizer.plugins.ModuleBasedSubplugin;
import com.kraby.mcarcinizer.healthhardcorizer.config.GeneralConfig;
import com.kraby.mcarcinizer.healthhardcorizer.config.RegenConfig;
import com.kraby.mcarcinizer.healthhardcorizer.config.RespawnConfig;
import com.kraby.mcarcinizer.healthhardcorizer.listeners.RegenListener;
import com.kraby.mcarcinizer.healthhardcorizer.listeners.RespawnListener;
import com.kraby.mcarcinizer.utils.config.Config;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class HealthHardcorizerSubplugin extends ModuleBasedSubplugin  {
    private static final String ID_MODULE_REGEN = "regen";
    private static final String ID_MODULE_RESPAWN = "respawn";

    private static final String CONFIG_FILE_NAME = "healthhardcorizer.yml";

    private static HealthHardcorizerSubplugin singleton;

    /**
     * Create the subplugin. Should only be created once (singleton) (... not really clean).
     * @param owner
     */
    public HealthHardcorizerSubplugin(Plugin owner) {
        super(owner);
        if (setSingleton(this)) {
            reload();
        }
    }

    private static boolean setSingleton(HealthHardcorizerSubplugin singleton) {
        if (HealthHardcorizerSubplugin.singleton == null) {
            HealthHardcorizerSubplugin.singleton = singleton;
            return true;
        } else {
            return false;
        }
    }

    public static HealthHardcorizerSubplugin getSingleton() {
        return singleton;
    }

    public RegenConfig getRegenConfig() {
        return (RegenConfig) getModuleConfig(ID_MODULE_REGEN);
    }

    public RespawnConfig getRespawnConfig() {
        return (RespawnConfig) getModuleConfig(ID_MODULE_RESPAWN);
    }


    // ------ Overriden --------

    @Override
    public String getName() {
        return "HealthHardcorizer";
    }

    @Override
    public String getDisplayName() {
        return "HealthHardcorizer";
    }

    @Override
    protected Map<String, List<Listener>> createModulesListeners() {
        Map<String, List<Listener>> map = new HashMap<>();
        map.put(ID_MODULE_REGEN, List.of(new RegenListener()));
        map.put(ID_MODULE_RESPAWN, List.of(new RespawnListener()));
        return map;
    }

    @Override
    protected Map<String, Config> createModulesConfigs() {
        Map<String, Config> map = new HashMap<>();
        map.put(ID_MODULE_REGEN, new RegenConfig(getMainConfig().getConfig()));
        map.put(ID_MODULE_RESPAWN, new RespawnConfig(getMainConfig().getConfig()));
        return map;
    }

    @Override
    protected Config createMainConfig() {
        return new GeneralConfig(getOwner(), CONFIG_FILE_NAME);
    }
}