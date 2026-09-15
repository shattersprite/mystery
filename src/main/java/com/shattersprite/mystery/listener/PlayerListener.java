package com.shattersprite.mystery.listener;

import com.shattersprite.mystery.MysteryPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

/**
 * Handles player join and quit events
 */
public class PlayerListener implements Listener {

    private final MysteryPlugin plugin;

    public PlayerListener(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getPlayerManager().handlePlayerJoin(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getPlayerManager().handlePlayerQuit(event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        plugin.getTriggerManager().handleInteraction(event);
        plugin.getTriggerManager().handleItemCheck(event.getPlayer());
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        plugin.getTriggerManager().handleCommand(event.getPlayer(), event.getMessage().substring(1));
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        plugin.getTriggerManager().handleItemCheck(event.getPlayer());
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        plugin.getTriggerManager().handleAdvancement(event.getPlayer(), event.getAdvancement().getKey().getKey());
    }
}
