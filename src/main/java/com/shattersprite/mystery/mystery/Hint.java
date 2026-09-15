package com.shattersprite.mystery.mystery;

/**
 * Represents a hint for a stage
 */
public class Hint {

    private String text;
    private int cost;

    public Hint() {
        this.cost = 0;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
