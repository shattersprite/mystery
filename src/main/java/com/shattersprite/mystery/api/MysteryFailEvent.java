package com.shattersprite.mystery.api;

import com.shattersprite.mystery.mystery.Mystery;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player fails a mystery (e.g., runs out of puzzle attempts)
 */
public class MysteryFailEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Mystery mystery;
    private final String reason;

    public MysteryFailEvent(Player player, Mystery mystery, String reason) {
        this.player = player;
        this.mystery = mystery;
        this.reason = reason;
    }

    public Player getPlayer() {
        return player;
    }

    public Mystery getMystery() {
        return mystery;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
