package com.shattersprite.mystery.mystery.trigger;

/**
 * Data for chat-based triggers
 */
public class ChatTriggerData {

    private String phrase;
    private boolean caseSensitive;
    private boolean exactMatch;

    public ChatTriggerData() {
        this.caseSensitive = false;
        this.exactMatch = false;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public boolean isExactMatch() {
        return exactMatch;
    }

    public void setExactMatch(boolean exactMatch) {
        this.exactMatch = exactMatch;
    }
}
