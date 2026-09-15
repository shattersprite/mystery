package com.shattersprite.mystery.command.subcommands;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.command.SubCommand;
import com.shattersprite.mystery.player.PlayerProgress;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Stop command - stops the current mystery
 */
public class StopCommand implements SubCommand {

    private final MysteryPlugin plugin;

    public StopCommand(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return "Stop the current mystery";
    }

    @Override
    public String getUsage() {
        return "/mystery stop";
    }

    @Override
    public String getPermission() {
        return "mystery.play";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        PlayerProgress progress = plugin.getPlayerManager().getProgress(uuid);
        if (progress == null || progress.getMysteryId() == null) {
            sender.sendMessage(plugin.getMessagesConfig().getNotInMystery());
            return;
        }

        // Remove progress from cache and database
        plugin.getTriggerManager().stopLocationChecking(uuid);
        plugin.getPuzzleManager().endPuzzle(uuid);
        plugin.getPlayerManager().removeProgress(uuid);
        plugin.getStorageManager().getStorage().deletePlayerProgress(uuid)
                .thenRun(() -> sender.sendMessage(plugin.getMessagesConfig().getMysteryStopped()));
    }
}
