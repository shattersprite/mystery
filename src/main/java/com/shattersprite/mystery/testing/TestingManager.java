package com.shattersprite.mystery.testing;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.mystery.Mystery;
import com.shattersprite.mystery.mystery.Stage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages testing mode for mysteries
 */
public class TestingManager {

    private final MysteryPlugin plugin;
    private final Map<UUID, TestSession> activeSessions;

    public TestingManager(MysteryPlugin plugin) {
        this.plugin = plugin;
        this.activeSessions = new HashMap<>();
    }

    /**
     * Start testing a mystery
     */
    public boolean startTest(Player player, String mysteryId) {
        if (!plugin.getConfigManager().isTestingEnabled()) {
            player.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cTesting mode is disabled on this server.");
            return false;
        }

        Mystery mystery = plugin.getMysteryManager().getMystery(mysteryId);
        if (mystery == null) {
            player.sendMessage(plugin.getMessagesConfig().getMysteryNotFound(mysteryId));
            return false;
        }

        UUID uuid = player.getUniqueId();

        // Stop any existing real progress temporarily
        com.shattersprite.mystery.player.PlayerProgress realProgress = plugin.getPlayerManager().getProgress(uuid);
        TestSession session = new TestSession(uuid, mysteryId, realProgress);

        activeSessions.put(uuid, session);

        // Start the mystery in test mode
        plugin.getStageManager().startMystery(player, mysteryId)
                .thenRun(() -> player.sendMessage(plugin.getMessagesConfig().getTestingModeEnabled()));

        return true;
    }

    /**
     * Stop testing a mystery
     */
    public boolean stopTest(Player player) {
        UUID uuid = player.getUniqueId();
        TestSession session = activeSessions.remove(uuid);

        if (session == null) {
            player.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cYou are not in testing mode.");
            return false;
        }

        // Clean up test progress
        plugin.getPlayerManager().removeProgress(uuid);

        // Restore real progress if it existed
        if (session.getOriginalProgress() != null) {
            plugin.getPlayerManager().addProgress(uuid, session.getOriginalProgress());
        }

        player.sendMessage(plugin.getMessagesConfig().getTestingModeDisabled());
        return true;
    }

    /**
     * Skip to a specific stage in test mode
     */
    public boolean skipToStage(Player player, int stageNumber) {
        UUID uuid = player.getUniqueId();
        TestSession session = activeSessions.get(uuid);

        if (session == null) {
            player.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cYou are not in testing mode.");
            return false;
        }

        Mystery mystery = plugin.getMysteryManager().getMystery(session.getMysteryId());
        if (mystery == null) {
            player.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cMystery not found.");
            return false;
        }

        Stage stage = mystery.getStage(stageNumber);
        if (stage == null) {
            player.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cStage " + stageNumber + " not found.");
            return false;
        }

        // Update progress
        com.shattersprite.mystery.player.PlayerProgress progress = plugin.getPlayerManager().getProgress(uuid);
        if (progress != null) {
            progress.setCurrentStage(stageNumber);

            // Display the stage clue
            plugin.getClueManager().displayClue(player, stage);

            player.sendMessage(plugin.getMessagesConfig().getPrefix() + "§eSkipped to stage " + stageNumber + ".");
        }

        return true;
    }

    /**
     * Complete the current stage in test mode
     */
    public boolean completeStage(Player player) {
        UUID uuid = player.getUniqueId();
        TestSession session = activeSessions.get(uuid);

        if (session == null) {
            player.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cYou are not in testing mode.");
            return false;
        }

        com.shattersprite.mystery.player.PlayerProgress progress = plugin.getPlayerManager().getProgress(uuid);
        if (progress != null) {
            plugin.getStageManager().completeStage(player, progress.getCurrentStage())
                    .thenRun(() -> player.sendMessage(plugin.getMessagesConfig().getPrefix() + "§eCompleted current stage."));
        }

        return true;
    }

    /**
     * Check if a player is in testing mode
     */
    public boolean isInTestingMode(UUID uuid) {
        return activeSessions.containsKey(uuid);
    }

    /**
     * Get the test session for a player
     */
    public TestSession getSession(UUID uuid) {
        return activeSessions.get(uuid);
    }
}
