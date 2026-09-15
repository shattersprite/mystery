package com.shattersprite.mystery.command.subcommands;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.command.SubCommand;
import com.shattersprite.mystery.mystery.Mystery;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Info command - shows mystery details
 */
public class InfoCommand implements SubCommand {

    private final MysteryPlugin plugin;

    public InfoCommand(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "View mystery details";
    }

    @Override
    public String getUsage() {
        return "/mystery info <id>";
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
        if (args.length == 0) {
            sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cUsage: /mystery info <id>");
            return;
        }

        Mystery mystery = plugin.getMysteryManager().getMystery(args[0]);
        if (mystery == null) {
            sender.sendMessage(plugin.getMessagesConfig().getMysteryNotFound(args[0]));
            return;
        }

        String name = mystery.getName().replace("&", "§");
        String description = mystery.getDescription().replace("&", "§");

        sender.sendMessage("§6=== " + name + " ===");
        sender.sendMessage("§eID: §f" + mystery.getId());
        sender.sendMessage("§eDescription: §f" + description);
        sender.sendMessage("§eMode: §f" + mystery.getMode());
        sender.sendMessage("§eStages: §f" + mystery.getTotalStages());
        sender.sendMessage("§eEnabled: §f" + (mystery.isEnabled() ? "Yes" : "No"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(plugin.getMysteryManager().getMysteries().keySet());
        }
        return List.of();
    }
}
