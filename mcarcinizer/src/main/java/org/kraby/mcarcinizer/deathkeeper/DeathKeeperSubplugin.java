package org.kraby.mcarcinizer.deathkeeper;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.kraby.mcarcinizer.Subplugin;
import org.kraby.mcarcinizer.deathkeeper.config.DkConfiguration;
import org.kraby.mcarcinizer.deathkeeper.listeners.ClassicDkListener;

public class DeathKeeperSubplugin extends Subplugin {
    private static final String CONFIG_FILE_NAME = "deathkeeper.yml";

    private static DeathKeeperSubplugin singleton;

    private FileConfiguration config;
    private DkConfiguration dkConfig;

    /**
     * Create the subplugin. Should only be created once (singleton) (... not really clean).
     * @param owner
     */
    public DeathKeeperSubplugin(Plugin owner) {
        super(owner);
        if (setSingleton(this)) {
            reloadConfig();
        }
    }

    /**
     * Reload this subplugin's config file.
     */
    public void reloadConfig() {
        unregisterListeners();

        File configFile = new File(getOwner().getDataFolder(), CONFIG_FILE_NAME);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            getOwner().saveResource(CONFIG_FILE_NAME, false);
        }

        config = new YamlConfiguration();

        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        dkConfig = new DkConfiguration(config);
        if (dkConfig.isPluginEnabled()) {
            registerListeners();
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

    public DkConfiguration getDkConfig() {
        return dkConfig;
    }

    public FileConfiguration getConfig() {
        return config;
    }

}
