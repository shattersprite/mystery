package com.shattersprite.mystery.mystery;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a mystery
 */
public class Mystery {

    private String id;
    private String name;
    private String description;
    private String mode; // individual, first-completion, team, server-wide
    private Map<Integer, Stage> stages;
    private Map<Integer, Hint> hints;
    private boolean enabled;

    public Mystery() {
        this.stages = new HashMap<>();
        this.hints = new HashMap<>();
        this.enabled = true;
        this.mode = "individual";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Map<Integer, Stage> getStages() {
        return stages;
    }

    public void setStages(Map<Integer, Stage> stages) {
        this.stages = stages;
    }

    public Stage getStage(int stageNumber) {
        return stages.get(stageNumber);
    }

    public void addStage(int stageNumber, Stage stage) {
        stages.put(stageNumber, stage);
    }

    public Map<Integer, Hint> getHints() {
        return hints;
    }

    public void setHints(Map<Integer, Hint> hints) {
        this.hints = hints;
    }

    public Hint getHint(int hintNumber) {
        return hints.get(hintNumber);
    }

    public void addHint(int hintNumber, Hint hint) {
        hints.put(hintNumber, hint);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getTotalStages() {
        return stages.size();
    }
}
