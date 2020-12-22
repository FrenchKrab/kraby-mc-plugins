package com.kraby.mcarcinizer;

import java.util.ArrayList;
import java.util.List;
import com.kraby.mcarcinizer.deathkeeper.DeathKeeperSubplugin;
import com.kraby.mcarcinizer.healthhardcorizer.HealthHardcorizerSubplugin;
import org.bukkit.plugin.java.JavaPlugin;


public class CarcinizerMain extends JavaPlugin {
    private static CarcinizerMain singleton;

    private List<Subplugin> subplugins = new ArrayList<>();

    @Override
    public void onEnable() {
        super.onEnable();
        setSingleton(this);
        initializeSubPlugins();
    }


    @Override
    public void onDisable() {
        //
    }



    private void initializeSubPlugins() {
        subplugins.add(new DeathKeeperSubplugin(this));
        subplugins.add(new HealthHardcorizerSubplugin(this));
    }


    private static void setSingleton(CarcinizerMain singleton) {
        CarcinizerMain.singleton = singleton;
    }

    public static CarcinizerMain getSingleton() {
        return singleton;
    }
}
