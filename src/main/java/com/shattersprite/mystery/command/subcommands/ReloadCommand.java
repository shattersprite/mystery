package com.shattersprite.mystery.command.subcommands;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.command.SubCommand;
import org.bukkit.command.CommandSender;

/**
 * Reload command - reloads the plugin
 */
public class ReloadCommand implements SubCommand {

    private final MysteryPlugin plugin;

    public ReloadCommand(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reload the plugin";
    }

    @Override
    public String getUsage() {
        return "/mystery reload";
    }

    @Override
    public String getPermission() {
        return "mystery.reload";
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        plugin.reload();
        sender.sendMessage(plugin.getMessagesConfig().getPluginReloaded());
    }
}
