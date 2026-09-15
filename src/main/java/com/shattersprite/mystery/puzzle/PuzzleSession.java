package com.shattersprite.mystery.puzzle;

import com.shattersprite.mystery.mystery.Puzzle;

import java.util.UUID;

/**
 * Represents an active puzzle session
 */
public class PuzzleSession {

    private final UUID playerUuid;
    private final int stageNumber;
    private final Puzzle puzzle;
    private int attempts;
    private long startTime;

    public PuzzleSession(UUID playerUuid, int stageNumber, Puzzle puzzle) {
        this.playerUuid = playerUuid;
        this.stageNumber = stageNumber;
        this.puzzle = puzzle;
        this.attempts = 0;
        this.startTime = System.currentTimeMillis();
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public int getStageNumber() {
        return stageNumber;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public int getAttempts() {
        return attempts;
    }

    public void incrementAttempts() {
        this.attempts++;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }
}
