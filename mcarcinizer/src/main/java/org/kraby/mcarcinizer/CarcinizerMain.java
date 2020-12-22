package org.kraby.mcarcinizer;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;
import org.kraby.mcarcinizer.deathkeeper.DeathKeeperSubplugin;


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
    }


    private static void setSingleton(CarcinizerMain singleton) {
        CarcinizerMain.singleton = singleton;
    }

    public static CarcinizerMain getSingleton() {
        return singleton;
    }
}
