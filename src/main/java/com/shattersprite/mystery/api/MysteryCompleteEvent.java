package com.shattersprite.mystery.api;

import com.shattersprite.mystery.mystery.Mystery;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player completes a mystery
 */
public class MysteryCompleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Mystery mystery;
    private final long completionTime;

    public MysteryCompleteEvent(Player player, Mystery mystery, long completionTime) {
        this.player = player;
        this.mystery = mystery;
        this.completionTime = completionTime;
    }

    public Player getPlayer() {
        return player;
    }

    public Mystery getMystery() {
        return mystery;
    }

    public long getCompletionTime() {
        return completionTime;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
