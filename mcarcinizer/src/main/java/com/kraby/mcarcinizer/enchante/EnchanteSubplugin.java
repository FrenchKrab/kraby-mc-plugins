package com.kraby.mcarcinizer.enchante;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kraby.mcarcinizer.carcinizer.plugins.ModuleBasedSubplugin;
import com.kraby.mcarcinizer.enchante.config.BetterAnvilConfig;
import com.kraby.mcarcinizer.enchante.config.GeneralConfig;
import com.kraby.mcarcinizer.enchante.listeners.BetterAnvilListener;
import com.kraby.mcarcinizer.utils.config.Config;
import com.kraby.mcarcinizer.utils.config.ConfigAccessor;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class EnchanteSubplugin extends ModuleBasedSubplugin  {
    private static final String MODULE_ID_BETTERANVIL = "betteranvil";
    private static final String CONFIG_FILE_NAME = "enchante.yml";

    private static EnchanteSubplugin singleton;

    /**
     * Create the subplugin. Should only be created once (singleton) (... not really clean).
     * @param owner
     */
    public EnchanteSubplugin(Plugin owner) {
        super(owner);
        if (setSingleton(this)) {
            reload();
        }
    }

    private static boolean setSingleton(EnchanteSubplugin singleton) {
        if (EnchanteSubplugin.singleton == null) {
            EnchanteSubplugin.singleton = singleton;
            return true;
        } else {
            return false;
        }
    }

    public static EnchanteSubplugin getSingleton() {
        return singleton;
    }


    @Override
    public String getName() {
        return "enchante";
    }

    @Override
    public String getDisplayName() {
        return "Enchanté!";
    }

    @Override
    protected Config createMainConfig() {
        return new GeneralConfig(getOwner(), CONFIG_FILE_NAME);
    }

    @Override
    protected Map<String, List<Listener>> createModulesListeners() {
        Map<String, List<Listener>> map = new HashMap<>();
        map.put(MODULE_ID_BETTERANVIL, List.of(new BetterAnvilListener()));
        return map;
    }

    @Override
    protected Map<String, Config> createModulesConfigs() {
        Map<String, Config> map = new HashMap<>();
        map.put(MODULE_ID_BETTERANVIL, new BetterAnvilConfig(getMainConfig().getConfig()));
        return map;
    }

}