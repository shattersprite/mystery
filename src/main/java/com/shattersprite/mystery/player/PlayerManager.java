package com.shattersprite.mystery.player;

import com.shattersprite.mystery.MysteryPlugin;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Manages player progress
 */
public class PlayerManager {

    private final MysteryPlugin plugin;
    private final Map<UUID, PlayerProgress> progressCache;

    public PlayerManager(MysteryPlugin plugin) {
        this.plugin = plugin;
        this.progressCache = new HashMap<>();
    }

    /**
     * Load a player's progress
     */
    public CompletableFuture<PlayerProgress> loadProgress(UUID uuid) {
        if (progressCache.containsKey(uuid)) {
            return CompletableFuture.completedFuture(progressCache.get(uuid));
        }

        return plugin.getStorageManager().getStorage().loadPlayerProgress(uuid)
                .thenApply(progress -> {
                    if (progress != null) {
                        progressCache.put(uuid, progress);
                    }
                    return progress;
                });
    }

    /**
     * Save a player's progress
     */
    public CompletableFuture<Void> saveProgress(UUID uuid) {
        PlayerProgress progress = progressCache.get(uuid);
        if (progress == null) {
            return CompletableFuture.completedFuture(null);
        }

        return plugin.getStorageManager().getStorage().savePlayerProgress(uuid, progress);
    }

    /**
     * Save all player progress
     */
    public CompletableFuture<Void> saveAllProgress() {
        CompletableFuture<?>[] futures = progressCache.keySet().stream()
                .map(this::saveProgress)
                .toArray(CompletableFuture[]::new);

        return CompletableFuture.allOf(futures);
    }

    /**
     * Create new progress for a player
     */
    public PlayerProgress createProgress(UUID uuid, String mysteryId) {
        PlayerProgress progress = new PlayerProgress(uuid);
        progress.setMysteryId(mysteryId);
        progress.setStartTime(System.currentTimeMillis());
        progressCache.put(uuid, progress);
        return progress;
    }

    /**
     * Get a player's progress from cache
     */
    public PlayerProgress getProgress(UUID uuid) {
        return progressCache.get(uuid);
    }

    /**
     * Add a player's progress to cache
     */
    public void addProgress(UUID uuid, PlayerProgress progress) {
        progressCache.put(uuid, progress);
    }

    /**
     * Remove a player's progress from cache
     */
    public void removeProgress(UUID uuid) {
        progressCache.remove(uuid);
    }

    /**
     * Reset a player's progress
     */
    public CompletableFuture<Void> resetProgress(UUID uuid) {
        PlayerProgress progress = progressCache.get(uuid);
        if (progress == null) {
            return CompletableFuture.completedFuture(null);
        }

        progressCache.remove(uuid);
        return plugin.getStorageManager().getStorage().deletePlayerProgress(uuid);
    }

    /**
     * Handle player join
     */
    public void handlePlayerJoin(Player player) {
        loadProgress(player.getUniqueId());
    }

    /**
     * Handle player quit
     */
    public void handlePlayerQuit(Player player) {
        saveProgress(player.getUniqueId()).thenRun(() -> removeProgress(player.getUniqueId()));
    }
}
