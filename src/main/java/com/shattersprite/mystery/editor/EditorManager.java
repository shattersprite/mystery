package com.shattersprite.mystery.editor;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.mystery.Mystery;
import com.shattersprite.mystery.mystery.Stage;
import com.shattersprite.mystery.mystery.trigger.LocationTriggerData;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Manages in-game editing operations
 */
public class EditorManager {

    private final MysteryPlugin plugin;

    public EditorManager(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Set the location trigger for a stage to the player's current location
     */
    public boolean setStageLocation(Player player, String mysteryId, int stageNumber) {
        Mystery mystery = plugin.getMysteryManager().getMystery(mysteryId);
        if (mystery == null) {
            player.sendMessage(plugin.getMessagesConfig().getMysteryNotFound(mysteryId));
            return false;
        }

        Stage stage = mystery.getStage(stageNumber);
        if (stage == null) {
            player.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cStage " + stageNumber + " not found.");
            return false;
        }

        if (stage.getTrigger() == null || !"location".equals(stage.getTrigger().getType())) {
            player.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cStage " + stageNumber + " does not have a location trigger.");
            return false;
        }

        Location playerLoc = player.getLocation();
        LocationTriggerData data = (LocationTriggerData) stage.getTrigger().getData();

        data.setWorld(playerLoc.getWorld().getName());
        data.setX(playerLoc.getX());
        data.setY(playerLoc.getY());
        data.setZ(playerLoc.getZ());

        // Save the mystery
        plugin.getMysteryManager().saveMystery(mystery);

        player.sendMessage(plugin.getMessagesConfig().getPrefix() + "§aStage " + stageNumber + " location set to your current position.");
        player.sendMessage("§7World: " + data.getWorld());
        player.sendMessage("§7X: " + String.format("%.2f", data.getX()));
        player.sendMessage("§7Y: " + String.format("%.2f", data.getY()));
        player.sendMessage("§7Z: " + String.format("%.2f", data.getZ()));

        return true;
    }

    /**
     * Get the location of a stage trigger
     */
    public void getStageLocation(Player player, String mysteryId, int stageNumber) {
        Mystery mystery = plugin.getMysteryManager().getMystery(mysteryId);
        if (mystery == null) {
            player.sendMessage(plugin.getMessagesConfig().getMysteryNotFound(mysteryId));
            return;
        }

        Stage stage = mystery.getStage(stageNumber);
        if (stage == null) {
            player.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cStage " + stageNumber + " not found.");
            return;
        }

        if (stage.getTrigger() == null || !"location".equals(stage.getTrigger().getType())) {
            player.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cStage " + stageNumber + " does not have a location trigger.");
            return;
        }

        LocationTriggerData data = (LocationTriggerData) stage.getTrigger().getData();

        player.sendMessage(plugin.getMessagesConfig().getPrefix() + "§eStage " + stageNumber + " location:");
        player.sendMessage("§7World: " + data.getWorld());
        player.sendMessage("§7X: " + String.format("%.2f", data.getX()));
        player.sendMessage("§7Y: " + String.format("%.2f", data.getY()));
        player.sendMessage("§7Z: " + String.format("%.2f", data.getZ()));
        player.sendMessage("§7Radius: " + String.format("%.2f", data.getRadius()));
    }

    /**
     * Teleport to a stage location
     */
    public boolean teleportToStage(Player player, String mysteryId, int stageNumber) {
        Mystery mystery = plugin.getMysteryManager().getMystery(mysteryId);
        if (mystery == null) {
            player.sendMessage(plugin.getMessagesConfig().getMysteryNotFound(mysteryId));
            return false;
        }

        Stage stage = mystery.getStage(stageNumber);
        if (stage == null) {
            player.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cStage " + stageNumber + " not found.");
            return false;
        }

        if (stage.getTrigger() == null || !"location".equals(stage.getTrigger().getType())) {
            player.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cStage " + stageNumber + " does not have a location trigger.");
            return false;
        }

        LocationTriggerData data = (LocationTriggerData) stage.getTrigger().getData();

        if (!player.getWorld().getName().equals(data.getWorld())) {
            player.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cYou must be in the same world as the stage location.");
            return false;
        }

        Location loc = new Location(
                player.getWorld(),
                data.getX(),
                data.getY(),
                data.getZ()
        );

        player.teleport(loc);
        player.sendMessage(plugin.getMessagesConfig().getPrefix() + "§aTeleported to stage " + stageNumber + " location.");

        return true;
    }
}
