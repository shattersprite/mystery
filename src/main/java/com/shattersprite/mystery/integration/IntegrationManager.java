package com.shattersprite.mystery.integration;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.integration.placeholderapi.PlaceholderAPIHook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Manages external plugin integrations
 */
public class IntegrationManager {

    private final MysteryPlugin plugin;
    private PlaceholderAPIHook placeholderAPIHook;
    private boolean vaultEnabled;
    private boolean placeholderAPIEnabled;

    public IntegrationManager(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Setup integrations with other plugins
     */
    public void setupIntegrations() {
        // Setup Vault (Economy)
        setupVault();

        // Setup PlaceholderAPI
        setupPlaceholderAPI();

        plugin.getLogger().info("Integrations setup complete!");
        plugin.getLogger().info("Vault: " + (vaultEnabled ? "Enabled" : "Disabled"));
        plugin.getLogger().info("PlaceholderAPI: " + (placeholderAPIEnabled ? "Enabled" : "Disabled"));
    }

    /**
     * Setup Vault integration
     */
    private void setupVault() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            vaultEnabled = false;
            return;
        }

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            vaultEnabled = false;
            return;
        }

        plugin.setEconomy(rsp.getProvider());
        vaultEnabled = true;
        plugin.getLogger().info("Vault integration enabled!");
    }

    /**
     * Setup PlaceholderAPI integration
     */
    private void setupPlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            placeholderAPIEnabled = false;
            return;
        }

        placeholderAPIHook = new PlaceholderAPIHook(plugin);
        placeholderAPIHook.register();
        placeholderAPIEnabled = true;
        plugin.getLogger().info("PlaceholderAPI integration enabled!");
    }

    /**
     * Check if Vault is enabled
     */
    public boolean isVaultEnabled() {
        return vaultEnabled;
    }

    /**
     * Check if PlaceholderAPI is enabled
     */
    public boolean isPlaceholderAPIEnabled() {
        return placeholderAPIEnabled;
    }

    /**
     * Get the PlaceholderAPI hook
     */
    public PlaceholderAPIHook getPlaceholderAPIHook() {
        return placeholderAPIHook;
    }

    /**
     * Disable all integrations
     */
    public void disable() {
        if (placeholderAPIHook != null) {
            placeholderAPIHook.unregister();
        }
    }
}
