package com.shattersprite.mystery.integration.placeholderapi;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.mystery.Mystery;
import com.shattersprite.mystery.player.PlayerProgress;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

/**
 * PlaceholderAPI integration for Mystery
 */
public class PlaceholderAPIHook extends PlaceholderExpansion {

    private final MysteryPlugin plugin;

    public PlaceholderAPIHook(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "mystery";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Shattersprite";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null || !player.isOnline()) {
            return "";
        }

        PlayerProgress progress = plugin.getPlayerManager().getProgress(player.getUniqueId());
        if (progress == null || progress.getMysteryId() == null) {
            return "";
        }

        Mystery mystery = plugin.getMysteryManager().getMystery(progress.getMysteryId());
        if (mystery == null) {
            return "";
        }

        // %mystery_current% - Current mystery name
        if (params.equals("current")) {
            return mystery.getName().replace("&", "§");
        }

        // %mystery_current_id% - Current mystery ID
        if (params.equals("current_id")) {
            return mystery.getId();
        }

        // %mystery_stage% - Current stage
        if (params.equals("stage")) {
            return String.valueOf(progress.getCurrentStage());
        }

        // %mystery_progress% - Progress percentage
        if (params.equals("progress")) {
            int total = mystery.getTotalStages();
            if (total == 0) return "0";
            int percentage = (progress.getCurrentStage() * 100) / total;
            return String.valueOf(percentage);
        }

        // %mystery_completed% - Completed stages count
        if (params.equals("completed")) {
            return String.valueOf(progress.getCompletedStages().size());
        }

        // %mystery_total% - Total stages
        if (params.equals("total")) {
            return String.valueOf(mystery.getTotalStages());
        }

        // %mystery_status% - Status (In Progress/Completed)
        if (params.equals("status")) {
            return progress.isCompleted() ? "Completed" : "In Progress";
        }

        // %mystery_time% - Time spent in seconds
        if (params.equals("time")) {
            long endTime = progress.isCompleted() ? progress.getCompletionTime() : System.currentTimeMillis();
            long duration = (endTime - progress.getStartTime()) / 1000;
            return String.valueOf(duration);
        }

        // %mystery_time_formatted% - Time formatted (mm:ss)
        if (params.equals("time_formatted")) {
            long endTime = progress.isCompleted() ? progress.getCompletionTime() : System.currentTimeMillis();
            long duration = (endTime - progress.getStartTime()) / 1000;
            long minutes = duration / 60;
            long seconds = duration % 60;
            return String.format("%02d:%02d", minutes, seconds);
        }

        return null;
    }
}
