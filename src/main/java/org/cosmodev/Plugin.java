package org.cosmodev;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.cosmodev.Commands.StatCMD;
import org.cosmodev.Events.CaptchaEvents;

public class Plugin extends JavaPlugin {

    private static Plugin instance;
    private static final long CLEANUP_INTERVAL = 15 * 60 * 20;

    public static Plugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new CaptchaEvents(), this);
        getCommand("stat").setExecutor(new StatCMD());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::cleanupTask, CLEANUP_INTERVAL, CLEANUP_INTERVAL);
    }

    private void cleanupTask() {
        CaptchaEvents.cleanupExpiredIpCooldowns();
    }
}
