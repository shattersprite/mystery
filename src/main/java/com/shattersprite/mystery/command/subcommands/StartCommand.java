package com.shattersprite.mystery.command.subcommands;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.command.SubCommand;
import com.shattersprite.mystery.mystery.Mystery;
import com.shattersprite.mystery.player.PlayerProgress;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Start command - starts a mystery
 */
public class StartCommand implements SubCommand {

    private final MysteryPlugin plugin;

    public StartCommand(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Start a mystery";
    }

    @Override
    public String getUsage() {
        return "/mystery start <id>";
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
        if (args.length == 0) {
            sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cUsage: /mystery start <id>");
            return;
        }

        Mystery mystery = plugin.getMysteryManager().getMystery(args[0]);
        if (mystery == null) {
            sender.sendMessage(plugin.getMessagesConfig().getMysteryNotFound(args[0]));
            return;
        }

        Player player = (Player) sender;

        // Check if player is already in a mystery
        PlayerProgress existingProgress = plugin.getPlayerManager().getProgress(player.getUniqueId());
        if (existingProgress != null && existingProgress.getMysteryId() != null) {
            sender.sendMessage(plugin.getMessagesConfig().getAlreadyInMystery());
            return;
        }

        // Start the mystery
        plugin.getStageManager().startMystery(player, args[0])
                .thenRun(() -> sender.sendMessage(plugin.getMessagesConfig().getMysteryStarted(mystery.getName().replace("&", "§"))));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(plugin.getMysteryManager().getMysteries().keySet());
        }
        return List.of();
    }
}
