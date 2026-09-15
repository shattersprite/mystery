package com.shattersprite.mystery.command.subcommands;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.command.SubCommand;
import org.bukkit.command.CommandSender;
import java.io.File;

/**
 * Edit command - edits a mystery
 */
public class EditCommand implements SubCommand {

    private final MysteryPlugin plugin;

    public EditCommand(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public String getDescription() {
        return "Edit a mystery";
    }

    @Override
    public String getUsage() {
        return "/mystery edit <id>";
    }

    @Override
    public String getPermission() {
        return "mystery.edit";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cUsage: /mystery edit <id>");
            return;
        }
        if (plugin.getMysteryManager().getMystery(args[0]) == null) {
            sender.sendMessage(plugin.getMessagesConfig().getMysteryNotFound(args[0]));
            return;
        }

        File file = new File(plugin.getDataFolder(), "mysteries/" + args[0] + ".yml");
        sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§aEdit this file: §f" + file.getAbsolutePath());
        sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§7Use /mystery reload after saving changes.");
    }
}
