package com.kraby.mcarcinizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.kraby.mcarcinizer.carcinizer.plugins.Subplugin;
import com.kraby.mcarcinizer.deathkeeper.DeathKeeperSubplugin;
import com.kraby.mcarcinizer.healthhardcorizer.HealthHardcorizerSubplugin;
import com.kraby.mcarcinizer.mcarcinizer.commands.InvSizeCommand;
import com.kraby.mcarcinizer.mcarcinizer.InfoLogs;
import com.kraby.mcarcinizer.mcarcinizer.commands.CheckConfigCommand;
import com.kraby.mcarcinizer.mcarcinizer.commands.ReloadCommand;
import com.kraby.mcarcinizer.utils.config.Config;
import com.kraby.mcarcinizer.utils.config.ErrorCheckableConfig;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


public class CarcinizerMain extends JavaPlugin {
    private static CarcinizerMain singleton;

    private List<Subplugin> subplugins = new ArrayList<>();

    @Override
    public void onEnable() {
        super.onEnable();
        setSingleton(this);
        initializeSubPlugins();
        registerCommands();
        checkForErrors();
    }


    @Override
    public void onDisable() {
        //
    }

    public List<Subplugin> getSubplugins() {
        return Collections.unmodifiableList(subplugins);
    }

    /**
     * Calls the reload method on all subplugins.
     */
    public void reloadSubplugin() {
        for (Subplugin subplugin : subplugins) {
            subplugin.reload();
        }
    }

    private void initializeSubPlugins() {
        subplugins.clear();
        subplugins.add(new DeathKeeperSubplugin(this));
        subplugins.add(new HealthHardcorizerSubplugin(this));
    }

    private void registerCommands() {
        this.getCommand("carcreload").setExecutor(new ReloadCommand());
        this.getCommand("invsize").setExecutor(new InvSizeCommand());
        this.getCommand("carcchkcfg").setExecutor(new CheckConfigCommand());
    }


    /**
     * Returns all errors found in subplugins configurations.
     * @return a non null list.
     */
    public List<String> getConfigErrors() {
        List<String> errors = new ArrayList<>();
        for (Subplugin sp : this.getSubplugins()) {
            String pluginPrefix = ChatColor.YELLOW + "(" + sp.getName() + ") ";
            for (Config conf : sp.getConfigs()) {
                if (!(conf instanceof ErrorCheckableConfig)) {
                    continue;
                }

                for (String error : ((ErrorCheckableConfig)conf).getErrors()) {
                    errors.add(pluginPrefix + ChatColor.RED + error);
                }
            }
        }
        return errors;
    }

    private void checkForErrors() {
        List<String> configErrors = getConfigErrors();
        InfoLogs.warnIfConfigsContainErrors(Bukkit.getConsoleSender(), configErrors.size());
    }


    private static void setSingleton(CarcinizerMain singleton) {
        CarcinizerMain.singleton = singleton;
    }

    public static CarcinizerMain getSingleton() {
        return singleton;
    }
}
