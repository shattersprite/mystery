package com.shattersprite.mystery.reward;

/**
 * Data for command rewards
 */
public class CommandRewardData {

    private String command;
    private boolean console;

    public CommandRewardData() {
        this.console = false;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean isConsole() {
        return console;
    }

    public void setConsole(boolean console) {
        this.console = console;
    }
}
