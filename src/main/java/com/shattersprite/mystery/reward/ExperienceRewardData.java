package com.shattersprite.mystery.reward;

/**
 * Data for experience rewards
 */
public class ExperienceRewardData {

    private int amount;
    private boolean levels;

    public ExperienceRewardData() {
        this.levels = false;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isLevels() {
        return levels;
    }

    public void setLevels(boolean levels) {
        this.levels = levels;
    }
}
