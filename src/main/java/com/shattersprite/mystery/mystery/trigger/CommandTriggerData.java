package com.shattersprite.mystery.mystery.trigger;

/**
 * Data for command-based triggers
 */
public class CommandTriggerData {

    private String command;
    private boolean exactMatch;

    public CommandTriggerData() {
        this.exactMatch = true;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean isExactMatch() {
        return exactMatch;
    }

    public void setExactMatch(boolean exactMatch) {
        this.exactMatch = exactMatch;
    }
}
