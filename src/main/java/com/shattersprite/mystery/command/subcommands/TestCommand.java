package com.shattersprite.mystery.command.subcommands;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.command.SubCommand;
import com.shattersprite.mystery.mystery.Mystery;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Test command - tests a mystery
 */
public class TestCommand implements SubCommand {

    private final MysteryPlugin plugin;

    public TestCommand(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getDescription() {
        return "Test a mystery";
    }

    @Override
    public String getUsage() {
        return "/mystery test <id>";
    }

    @Override
    public String getPermission() {
        return "mystery.test";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cUsage: /mystery test <id>");
            return;
        }

        Mystery mystery = plugin.getMysteryManager().getMystery(args[0]);
        if (mystery == null) {
            sender.sendMessage(plugin.getMessagesConfig().getMysteryNotFound(args[0]));
            return;
        }

        Player player = (Player) sender;
        plugin.getTestingManager().startTest(player, args[0]);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(plugin.getMysteryManager().getMysteries().keySet());
        }
        return List.of();
    }
}
