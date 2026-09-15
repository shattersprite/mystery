package com.shattersprite.mystery.trigger;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.mystery.Stage;
import com.shattersprite.mystery.mystery.Trigger;
import com.shattersprite.mystery.mystery.trigger.AdvancementTriggerData;
import com.shattersprite.mystery.mystery.trigger.ChatTriggerData;
import com.shattersprite.mystery.mystery.trigger.CommandTriggerData;
import com.shattersprite.mystery.mystery.trigger.InteractionTriggerData;
import com.shattersprite.mystery.mystery.trigger.ItemTriggerData;
import com.shattersprite.mystery.mystery.trigger.LocationTriggerData;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages trigger checking
 */
public class TriggerManager {

    private final MysteryPlugin plugin;
    private final Map<UUID, BukkitTask> locationCheckTasks;

    public TriggerManager(MysteryPlugin plugin) {
        this.plugin = plugin;
        this.locationCheckTasks = new HashMap<>();
    }

    /**
     * Check if a trigger is satisfied
     */
    public boolean checkTrigger(Player player, Trigger trigger) {
        if (trigger == null) {
            return false;
        }

        String type = trigger.getType();
        Object data = trigger.getData();

        switch (type.toLowerCase()) {
            case "location":
                return checkLocationTrigger(player, data);
            case "interaction":
                return checkInteractionTrigger(player, data);
            case "item":
                return checkItemTrigger(player, data);
            case "command":
                return checkCommandTrigger(player, data);
            case "chat":
                return checkChatTrigger(player, data);
            case "advancement":
                return checkAdvancementTrigger(player, data);
            case "manual":
                return true; // Manual triggers are always satisfied when called
            default:
                plugin.getLogger().warning("Unknown trigger type: " + type);
                return false;
        }
    }

    /**
     * Check location trigger
     */
    private boolean checkLocationTrigger(Player player, Object data) {
        if (!(data instanceof LocationTriggerData)) {
            return false;
        }

        LocationTriggerData locationData = (LocationTriggerData) data;

        if (!player.getWorld().getName().equals(locationData.getWorld())) {
            return false;
        }

        double distance = player.getLocation().distance(
                new org.bukkit.Location(
                        player.getWorld(),
                        locationData.getX(),
                        locationData.getY(),
                        locationData.getZ()
                )
        );

        return distance <= locationData.getRadius();
    }

    /**
     * Check interaction trigger
     */
    private boolean checkInteractionTrigger(Player player, Object data) {
        if (!(data instanceof InteractionTriggerData)) {
            return false;
        }

        // This will be called from event listeners
        return false;
    }

    /**
     * Check item trigger
     */
    private boolean checkItemTrigger(Player player, Object data) {
        if (!(data instanceof ItemTriggerData)) {
            return false;
        }

        ItemTriggerData itemData = (ItemTriggerData) data;

        try {
            org.bukkit.Material material = org.bukkit.Material.valueOf(itemData.getMaterial().toUpperCase());
            int amount = itemData.getAmount();

            int count = 0;
            for (org.bukkit.inventory.ItemStack item : player.getInventory().getContents()) {
                if (item != null && item.getType() == material && matchesItem(item, itemData)) {
                    count += item.getAmount();
                }
            }

            return count >= amount;
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid material in trigger: " + itemData.getMaterial());
            return false;
        }
    }

    private boolean matchesItem(org.bukkit.inventory.ItemStack item, ItemTriggerData data) {
        if (data.getName() == null || !data.isExactMatch()) {
            return true;
        }
        return item.hasItemMeta() && item.getItemMeta().hasDisplayName() &&
                data.getName().equals(item.getItemMeta().getDisplayName()) ||
                item.hasItemMeta() && item.getItemMeta().hasDisplayName() &&
                        data.getName().replace("&", "§").equals(item.getItemMeta().getDisplayName());
    }

    /**
     * Check command trigger
     */
    private boolean checkCommandTrigger(Player player, Object data) {
        if (!(data instanceof CommandTriggerData)) {
            return false;
        }

        // This will be called from event listeners
        return false;
    }

    /**
     * Check chat trigger
     */
    private boolean checkChatTrigger(Player player, Object data) {
        if (!(data instanceof ChatTriggerData)) {
            return false;
        }

        // This will be called from event listeners
        return false;
    }

    /**
     * Check advancement trigger
     */
    private boolean checkAdvancementTrigger(Player player, Object data) {
        if (!(data instanceof AdvancementTriggerData)) {
            return false;
        }

        AdvancementTriggerData advancementData = (AdvancementTriggerData) data;

        org.bukkit.advancement.Advancement advancement = plugin.getServer().getAdvancement(
                org.bukkit.NamespacedKey.minecraft(advancementData.getAdvancement())
        );

        if (advancement == null) {
            plugin.getLogger().warning("Invalid advancement in trigger: " + advancementData.getAdvancement());
            return false;
        }

        return player.getAdvancementProgress(advancement).isDone();
    }

    /**
     * Start location checking for a player
     */
    public void startLocationChecking(Player player, Stage stage) {
        if (stage.getTrigger() == null || !"location".equals(stage.getTrigger().getType())) {
            return;
        }

        UUID uuid = player.getUniqueId();

        // Stop existing task if any
        stopLocationChecking(uuid);

        int interval = plugin.getConfigManager().getLocationCheckInterval();

        BukkitTask task = plugin.getServer().getScheduler().runTaskTimer(
                plugin,
                () -> {
                    if (checkTrigger(player, stage.getTrigger())) {
                        stopLocationChecking(uuid);
                        plugin.getStageManager().completeStage(player, stage.getNumber());
                    }
                },
                interval,
                interval
        );

        locationCheckTasks.put(uuid, task);
    }

    /**
     * Stop location checking for a player
     */
    public void stopLocationChecking(UUID uuid) {
        BukkitTask task = locationCheckTasks.remove(uuid);
        if (task != null) {
            task.cancel();
        }
    }

    /**
     * Stop all location checking
     */
    public void stopAllLocationChecking() {
        locationCheckTasks.values().forEach(BukkitTask::cancel);
        locationCheckTasks.clear();
    }

    public void handleInteraction(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Stage stage = plugin.getStageManager().getCurrentStage(player);
        if (stage == null || stage.getTrigger() == null ||
                !"interaction".equalsIgnoreCase(stage.getTrigger().getType()) || event.getClickedBlock() == null ||
                !(stage.getTrigger().getData() instanceof InteractionTriggerData)) {
            return;
        }

        InteractionTriggerData data = (InteractionTriggerData) stage.getTrigger().getData();
        boolean blockMatches = data.getBlockType() == null ||
                data.getBlockType().equalsIgnoreCase(event.getClickedBlock().getType().name());
        boolean actionMatches = data.getInteractionType() == null ||
                ("block".equalsIgnoreCase(data.getInteractionType()) &&
                        (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK));
        if (blockMatches && actionMatches) {
            plugin.getStageManager().completeStage(player, stage.getNumber());
        }
    }

    public void handleItemCheck(Player player) {
        Stage stage = plugin.getStageManager().getCurrentStage(player);
        if (stage != null && stage.getTrigger() != null &&
                "item".equalsIgnoreCase(stage.getTrigger().getType()) &&
                checkTrigger(player, stage.getTrigger())) {
            plugin.getStageManager().completeStage(player, stage.getNumber());
        }
    }

    public void handleCommand(Player player, String command) {
        Stage stage = plugin.getStageManager().getCurrentStage(player);
        if (stage == null || stage.getTrigger() == null ||
                !"command".equalsIgnoreCase(stage.getTrigger().getType()) ||
                !(stage.getTrigger().getData() instanceof CommandTriggerData)) {
            return;
        }
        CommandTriggerData data = (CommandTriggerData) stage.getTrigger().getData();
        String expected = data.getCommand().toLowerCase();
        String actual = command.toLowerCase();
        boolean matches = data.isExactMatch() ? actual.equals(expected) : actual.startsWith(expected);
        if (matches) {
            plugin.getStageManager().completeStage(player, stage.getNumber());
        }
    }

    public void handleChat(Player player, String message) {
        Stage stage = plugin.getStageManager().getCurrentStage(player);
        if (stage == null || stage.getTrigger() == null ||
                !"chat".equalsIgnoreCase(stage.getTrigger().getType()) ||
                !(stage.getTrigger().getData() instanceof ChatTriggerData)) {
            return;
        }
        ChatTriggerData data = (ChatTriggerData) stage.getTrigger().getData();
        String phrase = data.getPhrase();
        String actual = data.isCaseSensitive() ? message : message.toLowerCase();
        String expected = data.isCaseSensitive() ? phrase : phrase.toLowerCase();
        boolean matches = data.isExactMatch() ? actual.equals(expected) : actual.contains(expected);
        if (matches) {
            plugin.getStageManager().completeStage(player, stage.getNumber());
        }
    }

    public void handleAdvancement(Player player, String advancement) {
        Stage stage = plugin.getStageManager().getCurrentStage(player);
        if (stage == null || stage.getTrigger() == null ||
                !"advancement".equalsIgnoreCase(stage.getTrigger().getType()) ||
                !(stage.getTrigger().getData() instanceof AdvancementTriggerData)) {
            return;
        }
        AdvancementTriggerData data = (AdvancementTriggerData) stage.getTrigger().getData();
        if (advancement.equalsIgnoreCase(data.getAdvancement())) {
            plugin.getStageManager().completeStage(player, stage.getNumber());
        }
    }
}
