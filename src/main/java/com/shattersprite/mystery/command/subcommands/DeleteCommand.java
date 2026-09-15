package com.shattersprite.mystery.command.subcommands;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.command.SubCommand;
import com.shattersprite.mystery.mystery.Mystery;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Delete command - deletes a mystery
 */
public class DeleteCommand implements SubCommand {

    private final MysteryPlugin plugin;

    public DeleteCommand(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getDescription() {
        return "Delete a mystery";
    }

    @Override
    public String getUsage() {
        return "/mystery delete <id>";
    }

    @Override
    public String getPermission() {
        return "mystery.delete";
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cUsage: /mystery delete <id>");
            return;
        }

        Mystery mystery = plugin.getMysteryManager().getMystery(args[0]);
        if (mystery == null) {
            sender.sendMessage(plugin.getMessagesConfig().getMysteryNotFound(args[0]));
            return;
        }

        // Remove from memory
        plugin.getMysteryManager().removeMystery(args[0]);

        // Delete file
        boolean deleted = plugin.getMysteryManager().deleteMysteryFile(args[0]);

        if (deleted) {
            sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§aMystery '" + args[0] + "' deleted successfully!");
        } else {
            sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cFailed to delete mystery file. Check console for details.");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(plugin.getMysteryManager().getMysteries().keySet());
        }
        return List.of();
    }
}
