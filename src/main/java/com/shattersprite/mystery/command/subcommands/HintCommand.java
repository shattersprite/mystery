package com.shattersprite.mystery.command.subcommands;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.command.SubCommand;
import com.shattersprite.mystery.mystery.Hint;
import com.shattersprite.mystery.mystery.Mystery;
import com.shattersprite.mystery.mystery.Stage;
import com.shattersprite.mystery.player.PlayerProgress;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Hint command - requests a hint
 */
public class HintCommand implements SubCommand {

    private final MysteryPlugin plugin;

    public HintCommand(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "hint";
    }

    @Override
    public String getDescription() {
        return "Request a hint";
    }

    @Override
    public String getUsage() {
        return "/mystery hint";
    }

    @Override
    public String getPermission() {
        return "mystery.hint";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!plugin.getConfigManager().areHintsEnabled()) {
            sender.sendMessage(plugin.getMessagesConfig().getHintsDisabled());
            return;
        }

        Player player = (Player) sender;
        PlayerProgress progress = plugin.getPlayerManager().getProgress(player.getUniqueId());

        if (progress == null || progress.getMysteryId() == null) {
            sender.sendMessage(plugin.getMessagesConfig().getNotInMystery());
            return;
        }

        Mystery mystery = plugin.getMysteryManager().getMystery(progress.getMysteryId());
        if (mystery == null) {
            sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cThe mystery you were playing no longer exists.");
            return;
        }

        // Get current stage
        Stage currentStage = mystery.getStage(progress.getCurrentStage());
        if (currentStage == null) {
            sender.sendMessage(plugin.getMessagesConfig().getNoHintsAvailable());
            return;
        }

        // Get hints for this stage (using stage number as hint group)
        int hintNumber = progress.getHintsUsed().size() + 1;
        Hint hint = mystery.getHint(hintNumber);

        if (hint == null) {
            sender.sendMessage(plugin.getMessagesConfig().getNoHintsAvailable());
            return;
        }

        // Check cost
        int cost = hint.getCost();
        String costType = plugin.getConfigManager().getHintCostType();

        if (cost > 0) {
            if ("experience".equals(costType)) {
                int playerExp = player.getTotalExperience();
                if (playerExp < cost) {
                    sender.sendMessage(plugin.getMessagesConfig().getHintCostMessage(cost));
                    sender.sendMessage(plugin.getMessagesConfig().getCannotAffordHint());
                    return;
                }
                player.setTotalExperience(playerExp - cost);
            } else if ("economy".equals(costType)) {
                if (plugin.getEconomy() == null) {
                    sender.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cEconomy not available.");
                    return;
                }
                double balance = plugin.getEconomy().getBalance(player);
                if (balance < cost) {
                    sender.sendMessage(plugin.getMessagesConfig().getHintCostMessage(cost));
                    sender.sendMessage(plugin.getMessagesConfig().getCannotAffordHint());
                    return;
                }
                plugin.getEconomy().withdrawPlayer(player, cost);
            }
        }

        // Give hint
        progress.addHintUsed(String.valueOf(hintNumber));
        plugin.getPlayerManager().saveProgress(player.getUniqueId());

        String hintText = hint.getText().replace("&", "§");
        sender.sendMessage(plugin.getMessagesConfig().getHintReceived(hintText));
    }
}
