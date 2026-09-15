package com.shattersprite.mystery.listener;

import com.shattersprite.mystery.MysteryPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Handles chat events for puzzle answers
 */
public class ChatListener implements Listener {

    private final MysteryPlugin plugin;

    public ChatListener(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        // Check if player has an active puzzle
        if (plugin.getPuzzleManager().hasActivePuzzle(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            plugin.getServer().getScheduler().runTask(plugin,
                    () -> plugin.getPuzzleManager().handleAnswer(event.getPlayer(), event.getMessage()));
        } else {
            plugin.getServer().getScheduler().runTask(plugin,
                    () -> plugin.getTriggerManager().handleChat(event.getPlayer(), event.getMessage()));
        }
    }
}
