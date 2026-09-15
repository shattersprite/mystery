package com.shattersprite.mystery.api;

import com.shattersprite.mystery.mystery.Mystery;
import com.shattersprite.mystery.mystery.Stage;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a clue is revealed to a player
 */
public class MysteryClueRevealEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Mystery mystery;
    private final Stage stage;
    private String clue;

    public MysteryClueRevealEvent(Player player, Mystery mystery, Stage stage, String clue) {
        this.player = player;
        this.mystery = mystery;
        this.stage = stage;
        this.clue = clue;
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

    public String getClue() {
        return clue;
    }

    public void setClue(String clue) {
        this.clue = clue;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
