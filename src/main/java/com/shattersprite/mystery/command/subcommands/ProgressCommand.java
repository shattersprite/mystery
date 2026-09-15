package com.shattersprite.mystery.command.subcommands;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.command.SubCommand;
import com.shattersprite.mystery.mystery.Mystery;
import com.shattersprite.mystery.player.PlayerProgress;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Progress command - shows player progress
 */
public class ProgressCommand implements SubCommand {

    private final MysteryPlugin plugin;

    public ProgressCommand(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "progress";
    }

    @Override
    public String getDescription() {
        return "View your progress";
    }

    @Override
    public String getUsage() {
        return "/mystery progress [player]";
    }

    @Override
    public String getPermission() {
        return "mystery.progress";
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UUID targetUuid;
        String targetName;

        if (args.length > 0) {
            if (!sender.hasPermission("mystery.admin")) {
                sender.sendMessage(plugin.getMessagesConfig().getNoPermission());
                return;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cPlayer not found.");
                return;
            }
            targetUuid = target.getUniqueId();
            targetName = target.getName();
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getMessagesConfig().getMessage("player-only"));
                return;
            }
            targetUuid = ((Player) sender).getUniqueId();
            targetName = sender.getName();
        }

        PlayerProgress progress = plugin.getPlayerManager().getProgress(targetUuid);
        if (progress == null || progress.getMysteryId() == null) {
            sender.sendMessage(plugin.getMessagesConfig().getNotInMystery());
            return;
        }

        Mystery mystery = plugin.getMysteryManager().getMystery(progress.getMysteryId());
        if (mystery == null) {
            sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cThe mystery you were playing no longer exists.");
            return;
        }

        String header = args.length > 0 ? plugin.getMessagesConfig().getMessage("player-progress", "player", targetName) : plugin.getMessagesConfig().getMessage("progress-header");
        sender.sendMessage(header);
        sender.sendMessage(plugin.getMessagesConfig().getMessage("progress-mystery", "name", mystery.getName().replace("&", "§")));
        sender.sendMessage(plugin.getMessagesConfig().getMessage("progress-stage", "stage", String.valueOf(progress.getCurrentStage())));
        sender.sendMessage(plugin.getMessagesConfig().getMessage("progress-completed", "stages", String.join(", ", progress.getCompletedStages())));

        String status = progress.isCompleted() ? "Completed" : "In Progress";
        sender.sendMessage(plugin.getMessagesConfig().getMessage("progress-status", "status", status));

        if (progress.isCompleted()) {
            long completionTime = progress.getCompletionTime() - progress.getStartTime();
            long minutes = completionTime / 60000;
            long seconds = (completionTime % 60000) / 1000;
            sender.sendMessage("§eTime: §f" + minutes + "m " + seconds + "s");
        }
    }
}
