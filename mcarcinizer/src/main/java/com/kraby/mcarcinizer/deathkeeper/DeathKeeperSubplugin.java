package com.kraby.mcarcinizer.deathkeeper;

import java.util.List;
import com.kraby.mcarcinizer.Subplugin;
import com.kraby.mcarcinizer.deathkeeper.config.DkConfig;
import com.kraby.mcarcinizer.deathkeeper.listeners.ClassicDkListener;
import com.kraby.mcarcinizer.utils.config.ConfigAccessor;
import org.bukkit.plugin.Plugin;

public class DeathKeeperSubplugin extends Subplugin {
    private static final String CONFIG_FILE_NAME = "deathkeeper.yml";

    private static DeathKeeperSubplugin singleton;

    private DkConfig dkConfig;

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

    /**
     * Reload this subplugin's config file.
     */
    public void reload() {
        unregisterAllListeners();

        dkConfig = new DkConfig(getOwner(), CONFIG_FILE_NAME);
        if (dkConfig.isPluginEnabled()) {
            registerAllListeners();
            getOwner().getLogger().info("DeathKeeper subplugin enabled !");
        } else {
            getOwner().getLogger().info("DeathKeeper subplugin disabled...");
        }
    }

    @Override
    protected void createListeners() {
        listeners.add(new ClassicDkListener());
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
        return dkConfig;
    }

    @Override
    public List<ConfigAccessor> getConfigAccessors() {
        return List.of(dkConfig);
    }

    @Override
    public String getName() {
        return "DeathKeeper";
    }
}
