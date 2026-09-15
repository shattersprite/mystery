package com.shattersprite.mystery.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Interface for subcommands
 */
public interface SubCommand {

    /**
     * Get the name of the subcommand
     */
    String getName();

    /**
     * Get the description of the subcommand
     */
    String getDescription();

    /**
     * Get the usage message
     */
    String getUsage();

    /**
     * Get the permission required to execute this command
     */
    String getPermission();

    /**
     * Check if this command can only be executed by players
     */
    boolean isPlayerOnly();

    /**
     * Check if the sender has permission to execute this command
     */
    default boolean canExecute(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return !isPlayerOnly();
        }
        return sender.hasPermission(getPermission()) || sender.isOp();
    }

    /**
     * Execute the subcommand
     */
    void execute(CommandSender sender, String[] args);

    /**
     * Get tab completions for this subcommand
     */
    default List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
