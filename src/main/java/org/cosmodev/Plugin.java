package org.cosmodev;

import org.bukkit.plugin.java.JavaPlugin;
import org.cosmodev.Commands.StatCMD;
import org.cosmodev.Events.CaptchaEvents;

public class Plugin extends JavaPlugin {

    private static Plugin instance;

    public static Plugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new CaptchaEvents(), this);
        getCommand("stat").setExecutor(new StatCMD());
    }


}
