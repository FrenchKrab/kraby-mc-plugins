package com.kraby.mcarcinizer.endfarms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kraby.mcarcinizer.carcinizer.plugins.ModuleBasedSubplugin;
import com.kraby.mcarcinizer.enchante.config.GeneralConfig;
import com.kraby.mcarcinizer.endfarms.config.MobFarmsConfig;
import com.kraby.mcarcinizer.utils.config.Config;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class EndFarmsSubplugin extends ModuleBasedSubplugin  {
    private static final String MODULE_ID_MOBFARMS = "mobfarms";
    private static final String CONFIG_FILE_NAME = "endfarms.yml";

    private static EndFarmsSubplugin singleton;

    /**
     * Create the subplugin. Should only be created once (singleton) (... not really clean).
     * @param owner
     */
    public EndFarmsSubplugin(Plugin owner) {
        super(owner);
        if (setSingleton(this)) {
            reload();
        }
    }

    private static boolean setSingleton(EndFarmsSubplugin singleton) {
        if (EndFarmsSubplugin.singleton == null) {
            EndFarmsSubplugin.singleton = singleton;
            return true;
        } else {
            return false;
        }
    }

    public static EndFarmsSubplugin getSingleton() {
        return singleton;
    }


    public MobFarmsConfig getMobFarmsConfig() {
        return (MobFarmsConfig) getModuleConfig(MODULE_ID_MOBFARMS);
    }



    @Override
    public String getName() {
        return "endfarms";
    }

    @Override
    public String getDisplayName() {
        return "End Farms";
    }

    @Override
    protected Config createMainConfig() {
        return new GeneralConfig(getOwner(), CONFIG_FILE_NAME);
    }

    @Override
    protected Map<String, List<Listener>> createModulesListeners() {
        Map<String, List<Listener>> map = new HashMap<>();
        // map.put(MODULE_ID_BETTERANVIL, List.of(new BetterAnvilListener()));
        // map.put(MODULE_ID_TWEAKS, List.of(new TweaksListener()));
        return map;
    }

    @Override
    protected Map<String, Config> createModulesConfigs() {
        Map<String, Config> map = new HashMap<>();
        map.put(MODULE_ID_MOBFARMS, new MobFarmsConfig(getMainConfig().getConfig()));
        return map;
    }

}