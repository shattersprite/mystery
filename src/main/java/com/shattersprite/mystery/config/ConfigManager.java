package com.shattersprite.mystery.config;

import com.shattersprite.mystery.MysteryPlugin;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Manages plugin configuration
 */
public class ConfigManager {

    private final MysteryPlugin plugin;

    public ConfigManager(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Get the plugin configuration
     */
    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    /**
     * Reload the plugin configuration
     */
    public void reloadConfig() {
        plugin.reloadConfig();
    }

    /**
     * Get the storage type
     */
    public String getStorageType() {
        return getConfig().getString("storage.type", "sqlite");
    }

    /**
     * Get the MySQL host
     */
    public String getMySQLHost() {
        return getConfig().getString("storage.mysql.host", "localhost");
    }

    /**
     * Get the MySQL port
     */
    public int getMySQLPort() {
        return getConfig().getInt("storage.mysql.port", 3306);
    }

    /**
     * Get the MySQL database
     */
    public String getMySQLDatabase() {
        return getConfig().getString("storage.mysql.database", "mystery");
    }

    /**
     * Get the MySQL username
     */
    public String getMySQLUsername() {
        return getConfig().getString("storage.mysql.username", "root");
    }

    /**
     * Get the MySQL password
     */
    public String getMySQLPassword() {
        return getConfig().getString("storage.mysql.password", "");
    }

    /**
     * Check if hints are enabled
     */
    public boolean areHintsEnabled() {
        return getConfig().getBoolean("hints.enabled", true);
    }

    /**
     * Get the default hint cost type
     */
    public String getHintCostType() {
        return getConfig().getString("hints.cost-type", "experience");
    }

    /**
     * Check if testing mode is enabled
     */
    public boolean isTestingEnabled() {
        return getConfig().getBoolean("testing.enabled", true);
    }

    /**
     * Check if debug mode is enabled
     */
    public boolean isDebugEnabled() {
        return getConfig().getBoolean("debug", false);
    }

    /**
     * Get the check interval for location triggers (in ticks)
     */
    public int getLocationCheckInterval() {
        return getConfig().getInt("triggers.location-check-interval", 20);
    }

    /**
     * Get the maximum distance for location triggers
     */
    public double getLocationTriggerDistance() {
        return getConfig().getDouble("triggers.location-distance", 3.0);
    }
}
