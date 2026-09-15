package com.shattersprite.mystery.reward;

import com.shattersprite.mystery.MysteryPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Manages reward distribution
 */
public class RewardManager {

    private final MysteryPlugin plugin;

    public RewardManager(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Give a reward to a player
     */
    public void giveReward(Player player, Reward reward) {
        if (reward == null) {
            return;
        }

        String type = reward.getType();
        Object data = reward.getData();

        switch (type.toLowerCase()) {
            case "item":
                giveItemReward(player, data);
                break;
            case "command":
                giveCommandReward(player, data);
                break;
            case "experience":
                giveExperienceReward(player, data);
                break;
            case "money":
                giveMoneyReward(player, data);
                break;
            case "permission":
                givePermissionReward(player, data);
                break;
            case "message":
                giveMessageReward(player, data);
                break;
            case "title":
                giveTitleReward(player, data);
                break;
            case "sound":
                giveSoundReward(player, data);
                break;
            default:
                plugin.getLogger().warning("Unknown reward type: " + type);
                break;
        }
    }

    /**
     * Give item reward
     */
    private void giveItemReward(Player player, Object data) {
        if (!(data instanceof ItemRewardData)) {
            return;
        }

        ItemRewardData itemData = (ItemRewardData) data;

        try {
            org.bukkit.Material material = org.bukkit.Material.valueOf(itemData.getMaterial().toUpperCase());
            ItemStack item = new ItemStack(material, itemData.getAmount());

            org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
            if (itemData.getName() != null) {
                meta.setDisplayName(itemData.getName().replace("&", "§"));
            }
            if (itemData.getLore() != null) {
                meta.setLore(java.util.Arrays.stream(itemData.getLore())
                        .map(line -> line.replace("&", "§")).toList());
            }
            item.setItemMeta(meta);
            player.getInventory().addItem(item);
            player.sendMessage(plugin.getMessagesConfig().getMessage("reward-received", "reward",
                    itemData.getAmount() + " " + material.name()));

        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid material in reward: " + itemData.getMaterial());
        }
    }

    /**
     * Give command reward
     */
    private void giveCommandReward(Player player, Object data) {
        if (!(data instanceof CommandRewardData)) {
            return;
        }

        CommandRewardData commandData = (CommandRewardData) data;

        String command = commandData.getCommand()
                .replace("%player%", player.getName())
                .replace("%uuid%", player.getUniqueId().toString());

        if (commandData.isConsole()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        } else {
            Bukkit.dispatchCommand(player, command);
        }
    }

    /**
     * Give experience reward
     */
    private void giveExperienceReward(Player player, Object data) {
        if (!(data instanceof ExperienceRewardData)) {
            return;
        }

        ExperienceRewardData expData = (ExperienceRewardData) data;

        if (expData.isLevels()) {
            player.giveExpLevels(expData.getAmount());
        } else {
            player.giveExp(expData.getAmount());
        }

        player.sendMessage(plugin.getMessagesConfig().getMessage("reward-received", "reward",
                expData.getAmount() + (expData.isLevels() ? " levels" : " XP")));
    }

    /**
     * Give money reward (requires Vault)
     */
    private void giveMoneyReward(Player player, Object data) {
        if (plugin.getEconomy() == null) {
            plugin.getLogger().warning("Vault not installed, cannot give money reward");
            return;
        }

        if (!(data instanceof Number)) {
            return;
        }

        double amount = ((Number) data).doubleValue();
        plugin.getEconomy().depositPlayer(player, amount);

        player.sendMessage(plugin.getMessagesConfig().getMessage("reward-received", "reward",
                String.format("%.2f", amount) + " " + plugin.getEconomy().currencyNamePlural()));
    }

    /**
     * Give permission reward (requires LuckPerms)
     */
    private void givePermissionReward(Player player, Object data) {
        if (!(data instanceof String permission) || permission.isBlank()) {
            return;
        }
        player.addAttachment(plugin).setPermission(permission, true);
        player.sendMessage(plugin.getMessagesConfig().getMessage("reward-received", "reward", permission));
    }

    /**
     * Give message reward
     */
    private void giveMessageReward(Player player, Object data) {
        if (!(data instanceof String)) {
            return;
        }

        String message = ((String) data)
                .replace("%player%", player.getName())
                .replace("&", "§");

        player.sendMessage(message);
    }

    /**
     * Give title reward
     */
    private void giveTitleReward(Player player, Object data) {
        if (!(data instanceof java.util.Map<?, ?> titleData)) {
            return;
        }
        String title = String.valueOf(titleData.get("title"));
        String subtitle = String.valueOf(titleData.get("subtitle"));
        plugin.getClueManager().displayTitle(player, title.replace("&", "<yellow>"), subtitle.replace("&", "<gray>"));
    }

    /**
     * Give sound reward
     */
    private void giveSoundReward(Player player, Object data) {
        if (!(data instanceof java.util.Map<?, ?> soundData)) {
            return;
        }
        try {
            org.bukkit.Sound sound = org.bukkit.Sound.valueOf(String.valueOf(soundData.get("sound")));
            float volume = ((Number) soundData.get("volume")).floatValue();
            float pitch = ((Number) soundData.get("pitch")).floatValue();
            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid sound reward: " + soundData.get("sound"));
        }
    }
}
