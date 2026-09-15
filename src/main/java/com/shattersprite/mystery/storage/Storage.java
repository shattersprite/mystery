package com.shattersprite.mystery.storage;

import com.shattersprite.mystery.player.PlayerProgress;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Interface for storage backends
 */
public interface Storage {

    /**
     * Initialize the storage
     */
    void initialize();

    /**
     * Close the storage connection
     */
    void close();

    /**
     * Load player progress
     */
    CompletableFuture<PlayerProgress> loadPlayerProgress(UUID uuid);

    /**
     * Save player progress
     */
    CompletableFuture<Void> savePlayerProgress(UUID uuid, PlayerProgress progress);

    /**
     * Delete player progress
     */
    CompletableFuture<Void> deletePlayerProgress(UUID uuid);

    /**
     * Check if player has progress
     */
    CompletableFuture<Boolean> hasPlayerProgress(UUID uuid);
}
