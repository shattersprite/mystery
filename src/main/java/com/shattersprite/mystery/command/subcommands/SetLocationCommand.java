package com.shattersprite.mystery.command.subcommands;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.command.SubCommand;
import com.shattersprite.mystery.mystery.Mystery;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * SetLocation command - sets a stage location to player's current position
 */
public class SetLocationCommand implements SubCommand {

    private final MysteryPlugin plugin;

    public SetLocationCommand(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "setlocation";
    }

    @Override
    public String getDescription() {
        return "Set a stage location to your current position";
    }

    @Override
    public String getUsage() {
        return "/mystery setlocation <mystery_id> <stage>";
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
        if (args.length < 2) {
            sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cUsage: /mystery setlocation <mystery_id> <stage>");
            return;
        }

        String mysteryId = args[0];
        int stageNumber;

        try {
            stageNumber = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cInvalid stage number.");
            return;
        }

        Player player = (Player) sender;
        plugin.getEditorManager().setStageLocation(player, mysteryId, stageNumber);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(plugin.getMysteryManager().getMysteries().keySet());
        }
        return List.of();
    }
}
