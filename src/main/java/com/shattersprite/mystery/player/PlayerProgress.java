package com.shattersprite.mystery.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Represents a player's progress in a mystery
 */
public class PlayerProgress {

    private UUID uuid;
    private String mysteryId;
    private int currentStage;
    private List<String> completedStages;
    private List<String> hintsUsed;
    private List<String> puzzleAttempts;
    private long startTime;
    private long completionTime;
    private boolean completed;
    private List<String> rewardsClaimed;

    public PlayerProgress() {
        this.completedStages = new ArrayList<>();
        this.hintsUsed = new ArrayList<>();
        this.puzzleAttempts = new ArrayList<>();
        this.rewardsClaimed = new ArrayList<>();
        this.currentStage = 1;
        this.completed = false;
    }

    public PlayerProgress(UUID uuid) {
        this();
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getMysteryId() {
        return mysteryId;
    }

    public void setMysteryId(String mysteryId) {
        this.mysteryId = mysteryId;
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }

    public List<String> getCompletedStages() {
        return completedStages;
    }

    public void setCompletedStages(String[] completedStages) {
        this.completedStages = new ArrayList<>(Arrays.asList(completedStages));
    }

    public void addCompletedStage(String stage) {
        completedStages.add(stage);
    }

    public List<String> getHintsUsed() {
        return hintsUsed;
    }

    public void setHintsUsed(String[] hintsUsed) {
        this.hintsUsed = new ArrayList<>(Arrays.asList(hintsUsed));
    }

    public void addHintUsed(String hint) {
        hintsUsed.add(hint);
    }

    public List<String> getPuzzleAttempts() {
        return puzzleAttempts;
    }

    public void setPuzzleAttempts(String[] puzzleAttempts) {
        this.puzzleAttempts = new ArrayList<>(Arrays.asList(puzzleAttempts));
    }

    public void addPuzzleAttempt(String attempt) {
        puzzleAttempts.add(attempt);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(long completionTime) {
        this.completionTime = completionTime;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public List<String> getRewardsClaimed() {
        return rewardsClaimed;
    }

    public void setRewardsClaimed(String[] rewardsClaimed) {
        this.rewardsClaimed = new ArrayList<>(Arrays.asList(rewardsClaimed));
    }

    public void addRewardClaimed(String reward) {
        rewardsClaimed.add(reward);
    }

    public boolean hasClaimedReward(String reward) {
        return rewardsClaimed.contains(reward);
    }
}
