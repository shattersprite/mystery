package com.shattersprite.mystery.command.subcommands;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.command.SubCommand;
import org.bukkit.command.CommandSender;
import java.io.File;
import java.io.IOException;

/**
 * Create command - creates a new mystery
 */
public class CreateCommand implements SubCommand {

    private final MysteryPlugin plugin;

    public CreateCommand(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Create a new mystery";
    }

    @Override
    public String getUsage() {
        return "/mystery create";
    }

    @Override
    public String getPermission() {
        return "mystery.create";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0 || !args[0].matches("[a-zA-Z0-9_-]+")) {
            sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cUsage: /mystery create <id>");
            return;
        }

        String id = args[0].toLowerCase();
        if (plugin.getMysteryManager().getMystery(id) != null) {
            sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cA mystery with that ID already exists.");
            return;
        }

        File folder = new File(plugin.getDataFolder(), "mysteries");
        folder.mkdirs();
        File file = new File(folder, id + ".yml");
        org.bukkit.configuration.file.YamlConfiguration config = new org.bukkit.configuration.file.YamlConfiguration();
        config.set("mystery.id", id);
        config.set("mystery.name", id);
        config.set("mystery.description", "");
        config.set("mystery.mode", "individual");
        config.set("mystery.stages.1.type", "clue");
        config.set("mystery.stages.1.clue", "&eReplace this clue");
        config.set("mystery.stages.1.trigger.type", "manual");
        try {
            config.save(file);
            plugin.getMysteryManager().reloadMysteries();
            sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§aCreated " + id + ".yml. Edit it in the mysteries folder, then use /mystery reload.");
        } catch (IOException e) {
            plugin.getLogger().log(java.util.logging.Level.SEVERE, "Failed to create mystery " + id, e);
            sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cCould not create the mystery file.");
        }
    }
}
