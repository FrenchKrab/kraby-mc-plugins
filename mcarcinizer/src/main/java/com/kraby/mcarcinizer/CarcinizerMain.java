package com.kraby.mcarcinizer;

import java.util.ArrayList;
import java.util.List;
import com.kraby.mcarcinizer.deathkeeper.DeathKeeperSubplugin;
import com.kraby.mcarcinizer.healthhardcorizer.HealthHardcorizerSubplugin;
import com.kraby.mcarcinizer.mcarcinizer.commands.InvSizeCommand;
import com.kraby.mcarcinizer.mcarcinizer.commands.ReloadCommand;
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
        subplugins.add(new DeathKeeperSubplugin(this));
        subplugins.add(new HealthHardcorizerSubplugin(this));
    }

    private void registerCommands() {
        this.getCommand("carcreload").setExecutor(new ReloadCommand());
        this.getCommand("invsize").setExecutor(new InvSizeCommand());
    }


    private static void setSingleton(CarcinizerMain singleton) {
        CarcinizerMain.singleton = singleton;
    }

    public static CarcinizerMain getSingleton() {
        return singleton;
    }
}
