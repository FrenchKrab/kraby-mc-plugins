package com.kraby.mcarcinizer.healthhardcorizer;

import com.kraby.mcarcinizer.Subplugin;
import com.kraby.mcarcinizer.healthhardcorizer.config.GeneralConfig;
import com.kraby.mcarcinizer.healthhardcorizer.config.RegenConfig;
import com.kraby.mcarcinizer.healthhardcorizer.config.RespawnConfig;
import com.kraby.mcarcinizer.healthhardcorizer.listeners.RegenListener;
import com.kraby.mcarcinizer.healthhardcorizer.listeners.RespawnListener;
import org.bukkit.plugin.Plugin;

public class HealthHardcorizerSubplugin extends Subplugin  {
    private static final String CONFIG_FILE_NAME = "healthhardcorizer.yml";

    private static HealthHardcorizerSubplugin singleton;

    private RegenListener regenListener;
    private RespawnListener respawnListener;

    private GeneralConfig generalConfig;
    private RegenConfig regenConfig;
    private RespawnConfig respawnConfig;

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

    /**
     * Reload this subplugin's config files.
     */
    public void reload() {
        unregisterAllListeners();

        generalConfig = new GeneralConfig(getOwner(), CONFIG_FILE_NAME);
        regenConfig = new RegenConfig(getOwner(), CONFIG_FILE_NAME);
        respawnConfig = new RespawnConfig(getOwner(), CONFIG_FILE_NAME);

        if (generalConfig.isPluginEnabled()) {
            if (regenConfig.isEnabled()) {
                registerListener(regenListener);
            }
            if (respawnConfig.isEnabled()) {
                registerListener(respawnListener);
            }

            getOwner().getLogger().info("HealthHardcorizer subplugin enabled !");
        } else {
            getOwner().getLogger().info("HealthHardcorizer subplugin disabled...");
        }
    }

    @Override
    protected void createListeners() {
        regenListener = new RegenListener();
        respawnListener = new RespawnListener();

        listeners.add(regenListener);
        listeners.add(respawnListener);
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
        return regenConfig;
    }

    public RespawnConfig getRespawnConfig() {
        return respawnConfig;
    }
}