package com.shattersprite.mystery.testing;

import com.shattersprite.mystery.player.PlayerProgress;

import java.util.UUID;

/**
 * Represents a testing session
 */
public class TestSession {

    private final UUID playerUuid;
    private final String mysteryId;
    private final PlayerProgress originalProgress;
    private final long startTime;

    public TestSession(UUID playerUuid, String mysteryId, PlayerProgress originalProgress) {
        this.playerUuid = playerUuid;
        this.mysteryId = mysteryId;
        this.originalProgress = originalProgress;
        this.startTime = System.currentTimeMillis();
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getMysteryId() {
        return mysteryId;
    }

    public PlayerProgress getOriginalProgress() {
        return originalProgress;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }
}
