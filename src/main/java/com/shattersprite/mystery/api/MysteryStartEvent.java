package com.shattersprite.mystery.api;

import com.shattersprite.mystery.mystery.Mystery;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player starts a mystery
 */
public class MysteryStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Mystery mystery;

    public MysteryStartEvent(Player player, Mystery mystery) {
        this.player = player;
        this.mystery = mystery;
    }

    public Player getPlayer() {
        return player;
    }

    public Mystery getMystery() {
        return mystery;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
