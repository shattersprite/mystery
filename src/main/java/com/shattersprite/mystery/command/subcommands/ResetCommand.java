package com.shattersprite.mystery.command.subcommands;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Reset command - resets player progress
 */
public class ResetCommand implements SubCommand {

    private final MysteryPlugin plugin;

    public ResetCommand(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public String getDescription() {
        return "Reset your progress";
    }

    @Override
    public String getUsage() {
        return "/mystery reset [player]";
    }

    @Override
    public String getPermission() {
        return "mystery.play";
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UUID targetUuid;

        if (args.length > 0) {
            if (!sender.hasPermission("mystery.reset")) {
                sender.sendMessage(plugin.getMessagesConfig().getNoPermission());
                return;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cPlayer not found.");
                return;
            }
            targetUuid = target.getUniqueId();
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getMessagesConfig().getMessage("player-only"));
                return;
            }
            targetUuid = ((Player) sender).getUniqueId();
        }

        plugin.getTriggerManager().stopLocationChecking(targetUuid);
        plugin.getPuzzleManager().endPuzzle(targetUuid);
        plugin.getPlayerManager().resetProgress(targetUuid)
                .thenRun(() -> sender.sendMessage(plugin.getMessagesConfig().getProgressReset()));
    }
}
