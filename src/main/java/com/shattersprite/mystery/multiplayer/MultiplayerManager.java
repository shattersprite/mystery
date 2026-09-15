package com.shattersprite.mystery.multiplayer;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.mystery.Mystery;
import com.shattersprite.mystery.player.PlayerProgress;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

import java.util.*;

/**
 * Manages multiplayer mystery modes
 */
public class MultiplayerManager {

    private final MysteryPlugin plugin;
    private final Map<String, ServerWideMystery> serverWideMysteries;
    private final Map<UUID, Set<UUID>> teams; // Maps team leader to team members

    public MultiplayerManager(MysteryPlugin plugin) {
        this.plugin = plugin;
        this.serverWideMysteries = new HashMap<>();
        this.teams = new HashMap<>();
    }

    /**
     * Start a server-wide mystery
     */
    public boolean startServerWideMystery(String mysteryId) {
        Mystery mystery = plugin.getMysteryManager().getMystery(mysteryId);
        if (mystery == null) {
            return false;
        }

        if (!"server-wide".equals(mystery.getMode())) {
            return false;
        }

        ServerWideMystery serverMystery = new ServerWideMystery(mysteryId, System.currentTimeMillis());
        serverWideMysteries.put(mysteryId, serverMystery);

        // Notify all online players
        plugin.getServer().broadcast(Component.text(
            plugin.getMessagesConfig().getMessage("mystery-started-server", "id", mysteryId)));

        return true;
    }

    /**
     * Stop a server-wide mystery
     */
    public boolean stopServerWideMystery(String mysteryId) {
        ServerWideMystery serverMystery = serverWideMysteries.remove(mysteryId);
        if (serverMystery == null) {
            return false;
        }

        // Clean up all player progress for this mystery
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            PlayerProgress progress = plugin.getPlayerManager().getProgress(player.getUniqueId());
            if (progress != null && mysteryId.equals(progress.getMysteryId())) {
                plugin.getPlayerManager().resetProgress(player.getUniqueId());
            }
        }

        plugin.getServer().broadcast(Component.text(
            plugin.getMessagesConfig().getMessage("mystery-stopped-server", "id", mysteryId)));

        return true;
    }

    /**
     * Create a team
     */
    public boolean createTeam(UUID leader, String teamName) {
        if (teams.containsKey(leader)) {
            return false;
        }

        teams.put(leader, new HashSet<>());
        teams.get(leader).add(leader);

        return true;
    }

    /**
     * Add a member to a team
     */
    public boolean addTeamMember(UUID leader, UUID member) {
        Set<UUID> team = teams.get(leader);
        if (team == null) {
            return false;
        }

        team.add(member);
        return true;
    }

    /**
     * Remove a member from a team
     */
    public boolean removeTeamMember(UUID leader, UUID member) {
        Set<UUID> team = teams.get(leader);
        if (team == null) {
            return false;
        }

        team.remove(member);
        return true;
    }

    /**
     * Get team members
     */
    public Set<UUID> getTeamMembers(UUID leader) {
        return teams.getOrDefault(leader, Collections.emptySet());
    }

    /**
     * Check if a player is in a team
     */
    public boolean isInTeam(UUID player) {
        for (Set<UUID> team : teams.values()) {
            if (team.contains(player)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the team leader for a player
     */
    public UUID getTeamLeader(UUID player) {
        for (Map.Entry<UUID, Set<UUID>> entry : teams.entrySet()) {
            if (entry.getValue().contains(player)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Get server-wide mystery status
     */
    public ServerWideMystery getServerWideMystery(String mysteryId) {
        return serverWideMysteries.get(mysteryId);
    }

    /**
     * Check if a mystery is running server-wide
     */
    public boolean isServerWideRunning(String mysteryId) {
        return serverWideMysteries.containsKey(mysteryId);
    }

    /**
     * Represents a server-wide mystery
     */
    public static class ServerWideMystery {
        private final String mysteryId;
        private final long startTime;
        private UUID firstCompleter;
        private long completionTime;

        public ServerWideMystery(String mysteryId, long startTime) {
            this.mysteryId = mysteryId;
            this.startTime = startTime;
        }

        public String getMysteryId() {
            return mysteryId;
        }

        public long getStartTime() {
            return startTime;
        }

        public UUID getFirstCompleter() {
            return firstCompleter;
        }

        public void setFirstCompleter(UUID firstCompleter) {
            this.firstCompleter = firstCompleter;
        }

        public long getCompletionTime() {
            return completionTime;
        }

        public void setCompletionTime(long completionTime) {
            this.completionTime = completionTime;
        }
    }
}
