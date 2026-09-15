package com.shattersprite.mystery.mystery;

/**
 * Represents a puzzle in a stage
 */
public class Puzzle {

    private String type;
    private String answer;
    private int attempts;
    private boolean caseSensitive;
    private int timeout;

    public Puzzle() {
        this.attempts = -1; // -1 means unlimited
        this.caseSensitive = false;
        this.timeout = 0; // 0 means no timeout
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
