package com.shattersprite.mystery.command;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.command.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main command handler for /mystery
 */
public class MysteryCommand implements CommandExecutor, TabCompleter {

    private final MysteryPlugin plugin;
    private final List<SubCommand> subCommands;

    public MysteryCommand(MysteryPlugin plugin) {
        this.plugin = plugin;
        this.subCommands = new ArrayList<>();

        // Register subcommands
        subCommands.add(new HelpCommand(plugin));
        subCommands.add(new ListCommand(plugin));
        subCommands.add(new InfoCommand(plugin));
        subCommands.add(new StartCommand(plugin));
        subCommands.add(new StopCommand(plugin));
        subCommands.add(new ProgressCommand(plugin));
        subCommands.add(new HintCommand(plugin));
        subCommands.add(new ResetCommand(plugin));
        subCommands.add(new ReloadCommand(plugin));
        subCommands.add(new CreateCommand(plugin));
        subCommands.add(new EditCommand(plugin));
        subCommands.add(new DeleteCommand(plugin));
        subCommands.add(new TestCommand(plugin));
        subCommands.add(new SetLocationCommand(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            subCommands.get(0).execute(sender, new String[]{});
            return true;
        }

        String subCommandName = args[0].toLowerCase();
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

        for (SubCommand subCommand : subCommands) {
            if (subCommand.getName().equalsIgnoreCase(subCommandName)) {
                if (!subCommand.canExecute(sender)) {
                    sender.sendMessage(plugin.getMessagesConfig().getNoPermission());
                    return true;
                }

                if (subCommand.isPlayerOnly() && !(sender instanceof Player)) {
                    sender.sendMessage(plugin.getMessagesConfig().getMessage("player-only"));
                    return true;
                }

                subCommand.execute(sender, subArgs);
                return true;
            }
        }

        sender.sendMessage(plugin.getMessagesConfig().getUnknownCommand());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1) {
            return subCommands.stream()
                    .filter(sub -> sub.canExecute(sender))
                    .map(SubCommand::getName)
                    .collect(Collectors.toList());
        }

        if (args.length > 1) {
            String subCommandName = args[0].toLowerCase();
            for (SubCommand subCommand : subCommands) {
                if (subCommand.getName().equalsIgnoreCase(subCommandName)) {
                    return subCommand.onTabComplete(sender, args);
                }
            }
        }

        return new ArrayList<>();
    }
}
