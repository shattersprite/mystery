package com.shattersprite.mystery.api;

import com.shattersprite.mystery.mystery.Mystery;
import com.shattersprite.mystery.mystery.Puzzle;
import com.shattersprite.mystery.mystery.Stage;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player solves a puzzle
 */
public class MysteryPuzzleSolveEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Mystery mystery;
    private final Stage stage;
    private final Puzzle puzzle;
    private final String answer;

    public MysteryPuzzleSolveEvent(Player player, Mystery mystery, Stage stage, Puzzle puzzle, String answer) {
        this.player = player;
        this.mystery = mystery;
        this.stage = stage;
        this.puzzle = puzzle;
        this.answer = answer;
    }

    public Player getPlayer() {
        return player;
    }

    public Mystery getMystery() {
        return mystery;
    }

    public Stage getStage() {
        return stage;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
