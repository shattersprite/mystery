package com.shattersprite.mystery.config;

import com.shattersprite.mystery.MysteryPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Manages message configuration
 */
public class MessagesConfig {

    private final MysteryPlugin plugin;
    private File configFile;
    private FileConfiguration config;

    public MessagesConfig(MysteryPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "messages.yml");
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Save the default messages.yml file
     */
    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Reload the messages configuration
     */
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Get the configuration
     */
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Get a message with color codes parsed
     */
    public String getMessage(String path) {
        String message = config.getString(path, "");
        return parseColors(message);
    }

    /**
     * Get a message with placeholders replaced
     */
    public String getMessage(String path, String... placeholders) {
        String message = getMessage(path);
        for (int i = 0; i < placeholders.length; i += 2) {
            if (i + 1 < placeholders.length) {
                message = message.replace("{" + placeholders[i] + "}", placeholders[i + 1]);
            }
        }
        return message;
    }

    /**
     * Parse color codes
     */
    private String parseColors(String message) {
        return message.replace("&", "§");
    }

    // Convenience methods for common messages

    public String getPrefix() {
        return getMessage("prefix", "&6[&eMystery&6] &r");
    }

    public String getNoPermission() {
        return getPrefix() + getMessage("no-permission", "&cYou don't have permission to do that.");
    }

    public String getUnknownCommand() {
        return getPrefix() + getMessage("unknown-command", "&cUnknown command. Use /mystery help.");
    }

    public String getMysteryNotFound(String id) {
        return getPrefix() + getMessage("mystery-not-found", "&cMystery '{id}' not found.", "id", id);
    }

    public String getMysteryStarted(String name) {
        return getPrefix() + getMessage("mystery-started", "&aYou have started: &e{name}", "name", name);
    }

    public String getMysteryCompleted(String name) {
        return getPrefix() + getMessage("mystery-completed", "&aYou have completed: &e{name}", "name", name);
    }

    public String getStageCompleted(int stage) {
        return getPrefix() + getMessage("stage-completed", "&aStage {stage} completed!", "stage", String.valueOf(stage));
    }

    public String getClueReceived(String clue) {
        return getPrefix() + getMessage("clue-received", "&e✦ CLUE DISCOVERED ✦\n&f{clue}", "clue", clue);
    }

    public String getHintReceived(String hint) {
        return getPrefix() + getMessage("hint-received", "&e✦ HINT ✦\n&f{hint}", "hint", hint);
    }

    public String getNoHintsAvailable() {
        return getPrefix() + getMessage("no-hints-available", "&cNo more hints available for this stage.");
    }

    public String getHintCost(int cost) {
        return getPrefix() + getMessage("hint-cost", "&eHint costs {cost} experience.", "cost", String.valueOf(cost));
    }

    public String getNotInMystery() {
        return getPrefix() + getMessage("not-in-mystery", "&cYou are not currently in a mystery.");
    }

    public String getAlreadyInMystery() {
        return getPrefix() + getMessage("already-in-mystery", "&cYou are already in a mystery.");
    }

    public String getMysteryStopped() {
        return getPrefix() + getMessage("mystery-stopped", "&cYou have stopped the mystery.");
    }

    public String getProgressReset() {
        return getPrefix() + getMessage("progress-reset", "&aYour progress has been reset.");
    }

    public String getPluginReloaded() {
        return getPrefix() + getMessage("plugin-reloaded", "&aPlugin reloaded successfully.");
    }

    public String getTestingModeEnabled() {
        return getPrefix() + getMessage("testing-mode-enabled", "&eTesting mode enabled. Progress will not be saved.");
    }

    public String getTestingModeDisabled() {
        return getPrefix() + getMessage("testing-mode-disabled", "&eTesting mode disabled.");
    }

    public String getHintsDisabled() {
        return getPrefix() + getMessage("hints-disabled", "&cHints are disabled on this server.");
    }

    public String getCannotAffordHint() {
        return getPrefix() + getMessage("cannot-afford-hint", "&cYou cannot afford this hint.");
    }

    public String getHintCostMessage(int cost) {
        return getMessage("hint-cost", "cost", String.valueOf(cost));
    }
}
