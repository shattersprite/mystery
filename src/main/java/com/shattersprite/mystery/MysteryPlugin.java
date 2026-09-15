package com.shattersprite.mystery;

import com.shattersprite.mystery.clue.ClueManager;
import com.shattersprite.mystery.command.MysteryCommand;
import com.shattersprite.mystery.config.ConfigManager;
import com.shattersprite.mystery.config.MessagesConfig;
import com.shattersprite.mystery.editor.EditorManager;
import com.shattersprite.mystery.integration.IntegrationManager;
import com.shattersprite.mystery.listener.ChatListener;
import com.shattersprite.mystery.listener.PlayerListener;
import com.shattersprite.mystery.multiplayer.MultiplayerManager;
import com.shattersprite.mystery.mystery.MysteryManager;
import com.shattersprite.mystery.mystery.StageManager;
import com.shattersprite.mystery.player.PlayerManager;
import com.shattersprite.mystery.puzzle.PuzzleManager;
import com.shattersprite.mystery.reward.RewardManager;
import com.shattersprite.mystery.storage.StorageManager;
import com.shattersprite.mystery.testing.TestingManager;
import com.shattersprite.mystery.trigger.TriggerManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class for Mystery
 */
public final class MysteryPlugin extends JavaPlugin {

    private static MysteryPlugin instance;

    private ConfigManager configManager;
    private MessagesConfig messagesConfig;
    private StorageManager storageManager;
    private MysteryManager mysteryManager;
    private StageManager stageManager;
    private PlayerManager playerManager;
    private IntegrationManager integrationManager;
    private ClueManager clueManager;
    private TriggerManager triggerManager;
    private RewardManager rewardManager;
    private PuzzleManager puzzleManager;
    private EditorManager editorManager;
    private TestingManager testingManager;
    private MultiplayerManager multiplayerManager;

    private Economy economy = null;

    @Override
    public void onEnable() {
        instance = this;

        // Save default configs
        saveDefaultConfig();
        messagesConfig = new MessagesConfig(this);
        messagesConfig.saveDefaultConfig();

        // Initialize managers
        configManager = new ConfigManager(this);
        storageManager = new StorageManager(this);
        mysteryManager = new MysteryManager(this);
        stageManager = new StageManager(this);
        playerManager = new PlayerManager(this);
        integrationManager = new IntegrationManager(this);
        clueManager = new ClueManager(this);
        triggerManager = new TriggerManager(this);
        rewardManager = new RewardManager(this);
        puzzleManager = new PuzzleManager(this);
        editorManager = new EditorManager(this);
        testingManager = new TestingManager(this);
        multiplayerManager = new MultiplayerManager(this);

        // Initialize storage
        storageManager.initialize();

        // Load mysteries
        mysteryManager.loadMysteries();

        // Setup integrations
        integrationManager.setupIntegrations();

        // Register events
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        // Register commands
        getCommand("mystery").setExecutor(new MysteryCommand(this));
        getCommand("mystery").setTabCompleter(new MysteryCommand(this));

        getLogger().info("Mystery has been enabled!");
    }

    @Override
    public void onDisable() {
        // Stop location checking
        if (triggerManager != null) {
            triggerManager.stopAllLocationChecking();
        }

        // Disable integrations
        if (integrationManager != null) {
            integrationManager.disable();
        }

        // Save data
        if (playerManager != null) {
            playerManager.saveAllProgress().join();
        }

        // Close storage
        if (storageManager != null) {
            storageManager.close();
        }

        getLogger().info("Mystery has been disabled!");
    }

    /**
     * Reload the plugin
     */
    public void reload() {
        // Reload configs
        reloadConfig();
        messagesConfig.reloadConfig();

        // Reload mysteries
        mysteryManager.loadMysteries();

        getLogger().info("Mystery has been reloaded!");
    }

    public static MysteryPlugin getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public MysteryManager getMysteryManager() {
        return mysteryManager;
    }

    public StageManager getStageManager() {
        return stageManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public IntegrationManager getIntegrationManager() {
        return integrationManager;
    }

    public ClueManager getClueManager() {
        return clueManager;
    }

    public TriggerManager getTriggerManager() {
        return triggerManager;
    }

    public RewardManager getRewardManager() {
        return rewardManager;
    }

    public PuzzleManager getPuzzleManager() {
        return puzzleManager;
    }

    public EditorManager getEditorManager() {
        return editorManager;
    }

    public TestingManager getTestingManager() {
        return testingManager;
    }

    public MultiplayerManager getMultiplayerManager() {
        return multiplayerManager;
    }

    public Economy getEconomy() {
        return economy;
    }

    public void setEconomy(Economy economy) {
        this.economy = economy;
    }
}
