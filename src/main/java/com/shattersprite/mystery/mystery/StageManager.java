package com.shattersprite.mystery.mystery;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.api.MysteryCompleteEvent;
import com.shattersprite.mystery.api.MysteryStageCompleteEvent;
import com.shattersprite.mystery.api.MysteryStartEvent;
import com.shattersprite.mystery.player.PlayerProgress;
import com.shattersprite.mystery.reward.Reward;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Manages stage progression
 */
public class StageManager {

    private final MysteryPlugin plugin;

    public StageManager(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Start a mystery for a player
     */
    public CompletableFuture<Void> startMystery(Player player, String mysteryId) {
        Mystery mystery = plugin.getMysteryManager().getMystery(mysteryId);
        if (mystery == null) {
            return CompletableFuture.completedFuture(null);
        }

        PlayerProgress progress = plugin.getPlayerManager().createProgress(player.getUniqueId(), mysteryId);
        progress.setCurrentStage(1);
        plugin.getServer().getPluginManager().callEvent(new MysteryStartEvent(player, mystery));

        activateStage(player, mystery.getStage(1));

        return plugin.getPlayerManager().saveProgress(player.getUniqueId());
    }

    /**
     * Complete a stage
     */
    public CompletableFuture<Void> completeStage(Player player, int stageNumber) {
        UUID uuid = player.getUniqueId();
        PlayerProgress progress = plugin.getPlayerManager().getProgress(uuid);

        if (progress == null) {
            return CompletableFuture.completedFuture(null);
        }

        Mystery mystery = plugin.getMysteryManager().getMystery(progress.getMysteryId());
        if (mystery == null) {
            return CompletableFuture.completedFuture(null);
        }

        Stage stage = mystery.getStage(stageNumber);
        if (stage == null || progress.getCompletedStages().contains(String.valueOf(stageNumber))) {
            return CompletableFuture.completedFuture(null);
        }

        // Mark stage as completed
        progress.addCompletedStage(String.valueOf(stageNumber));
        plugin.getServer().getPluginManager().callEvent(new MysteryStageCompleteEvent(player, mystery, stage));

        for (Reward reward : stage.getRewards()) {
            plugin.getRewardManager().giveReward(player, reward);
        }

        // Play completion sound
        plugin.getClueManager().playStageSound(player);

        // Check if this was the final stage
        if (stageNumber >= mystery.getTotalStages()) {
            return completeMystery(player);
        }

        // Move to next stage
        int nextStage = stageNumber + 1;
        progress.setCurrentStage(nextStage);

        activateStage(player, mystery.getStage(nextStage));

        return plugin.getPlayerManager().saveProgress(uuid);
    }

    /**
     * Complete a mystery
     */
    private CompletableFuture<Void> completeMystery(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerProgress progress = plugin.getPlayerManager().getProgress(uuid);

        if (progress == null) {
            return CompletableFuture.completedFuture(null);
        }

        progress.setCompleted(true);
        progress.setCompletionTime(System.currentTimeMillis());

        // Play completion sound
        plugin.getClueManager().playCompleteSound(player);

        Mystery mystery = plugin.getMysteryManager().getMystery(progress.getMysteryId());
        if (mystery != null) {
            plugin.getServer().getPluginManager().callEvent(new MysteryCompleteEvent(
                    player, mystery, progress.getCompletionTime()));
        }

        return plugin.getPlayerManager().saveProgress(uuid)
                .thenRun(() -> {
                    if (mystery != null) {
                        player.sendMessage(plugin.getMessagesConfig().getMysteryCompleted(
                                mystery.getName().replace("&", "§")
                        ));
                    }
                });
    }

    /**
     * Get the current stage for a player
     */
    public Stage getCurrentStage(Player player) {
        PlayerProgress progress = plugin.getPlayerManager().getProgress(player.getUniqueId());
        if (progress == null) {
            return null;
        }

        Mystery mystery = plugin.getMysteryManager().getMystery(progress.getMysteryId());
        if (mystery == null) {
            return null;
        }

        return mystery.getStage(progress.getCurrentStage());
    }

    private void activateStage(Player player, Stage stage) {
        if (stage == null) {
            return;
        }

        plugin.getClueManager().displayClue(player, stage);
        if (stage.getPuzzle() != null) {
            plugin.getPuzzleManager().startPuzzle(player, stage);
            return;
        }

        Trigger trigger = stage.getTrigger();
        if (trigger != null && "location".equalsIgnoreCase(trigger.getType())) {
            plugin.getTriggerManager().startLocationChecking(player, stage);
        }
    }
}
