package com.shattersprite.mystery.command.subcommands;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.command.SubCommand;
import com.shattersprite.mystery.mystery.Mystery;
import org.bukkit.command.CommandSender;

/**
 * List command - shows available mysteries
 */
public class ListCommand implements SubCommand {

    private final MysteryPlugin plugin;

    public ListCommand(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "List available mysteries";
    }

    @Override
    public String getUsage() {
        return "/mystery list";
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
        sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§eAvailable Mysteries:");

        var mysteries = plugin.getMysteryManager().getMysteries();
        if (mysteries.isEmpty()) {
            sender.sendMessage("§7No mysteries available yet. Use /mystery create to create one.");
            return;
        }

        for (Mystery mystery : mysteries.values()) {
            String name = mystery.getName().replace("&", "§");
            String description = mystery.getDescription().replace("&", "§");
            sender.sendMessage("§e- §f" + name + " §7(" + mystery.getId() + ")");
            if (!description.isEmpty()) {
                sender.sendMessage("  §7" + description);
            }
        }
    }
}
