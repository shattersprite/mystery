package com.shattersprite.mystery.mystery;

import com.shattersprite.mystery.reward.Reward;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a stage in a mystery
 */
public class Stage {

    private int number;
    private String type;
    private String clue;
    private Trigger trigger;
    private Puzzle puzzle;
    private List<Reward> rewards;
    private Map<String, Integer> branches; // For branching mysteries - maps condition to next stage number

    public Stage() {
        this.rewards = new ArrayList<>();
        this.branches = new HashMap<>();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClue() {
        return clue;
    }

    public void setClue(String clue) {
        this.clue = clue;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
    }

    public Map<String, Integer> getBranches() {
        return branches;
    }

    public void setBranches(Map<String, Integer> branches) {
        this.branches = branches;
    }

    public void addBranch(String condition, int nextStage) {
        branches.put(condition, nextStage);
    }

    public Integer getNextStageForCondition(String condition) {
        return branches.get(condition);
    }
}
