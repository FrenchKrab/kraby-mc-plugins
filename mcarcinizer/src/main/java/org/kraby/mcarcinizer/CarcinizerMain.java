package org.kraby.mcarcinizer;

import org.bukkit.plugin.java.JavaPlugin;


public class CarcinizerMain extends JavaPlugin {
    private static CarcinizerMain singleton;
    
    @Override
    public void onEnable() {
        super.onEnable();
        setSingleton(this);
    }
    
    
    @Override
    public void onDisable() {
        //
    }
    

    private static void setSingleton(CarcinizerMain singleton) {
        CarcinizerMain.singleton = singleton;
    }

    public static CarcinizerMain getSingleton() {
        return singleton;
    }
}
