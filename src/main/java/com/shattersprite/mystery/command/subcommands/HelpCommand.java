package com.shattersprite.mystery.command.subcommands;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Help command
 */
public class HelpCommand implements SubCommand {

    private final MysteryPlugin plugin;

    public HelpCommand(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Show help for Mystery commands";
    }

    @Override
    public String getUsage() {
        return "/mystery help [page]";
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
        sender.sendMessage("§6=== Mystery Commands ===");
        sender.sendMessage("§e/mystery list §7- List available mysteries");
        sender.sendMessage("§e/mystery info <id> §7- View mystery details");
        sender.sendMessage("§e/mystery start <id> §7- Start a mystery");
        sender.sendMessage("§e/mystery stop §7- Stop current mystery");
        sender.sendMessage("§e/mystery progress §7- View your progress");
        sender.sendMessage("§e/mystery hint §7- Request a hint");
        sender.sendMessage("§e/mystery reset §7- Reset your progress");

        if (sender.hasPermission("mystery.admin")) {
            sender.sendMessage("");
            sender.sendMessage("§6=== Admin Commands ===");
            sender.sendMessage("§e/mystery create §7- Create a new mystery");
            sender.sendMessage("§e/mystery edit <id> §7- Edit a mystery");
            sender.sendMessage("§e/mystery delete <id> §7- Delete a mystery");
            sender.sendMessage("§e/mystery reload §7- Reload the plugin");
            sender.sendMessage("§e/mystery test <id> §7- Test a mystery");
            sender.sendMessage("§e/mystery setlocation <id> <stage> §7- Set stage location");
        }
    }
}
