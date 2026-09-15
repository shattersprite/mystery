package com.shattersprite.mystery.api;

import com.shattersprite.mystery.mystery.Mystery;
import com.shattersprite.mystery.mystery.Stage;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player completes a stage
 */
public class MysteryStageCompleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Mystery mystery;
    private final Stage stage;

    public MysteryStageCompleteEvent(Player player, Mystery mystery, Stage stage) {
        this.player = player;
        this.mystery = mystery;
        this.stage = stage;
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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
