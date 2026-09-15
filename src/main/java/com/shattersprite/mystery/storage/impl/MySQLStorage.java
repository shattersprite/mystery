package com.shattersprite.mystery.storage.impl;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.player.PlayerProgress;
import com.shattersprite.mystery.storage.Storage;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * MySQL storage implementation
 */
public class MySQLStorage implements Storage {

    private final MysteryPlugin plugin;
    private HikariDataSource dataSource;

    public MySQLStorage(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize() {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s",
                    plugin.getConfigManager().getMySQLHost(),
                    plugin.getConfigManager().getMySQLPort(),
                    plugin.getConfigManager().getMySQLDatabase()));
            config.setUsername(plugin.getConfigManager().getMySQLUsername());
            config.setPassword(plugin.getConfigManager().getMySQLPassword());
            config.setPoolName("Mystery-HikariCP");
            config.setMaximumPoolSize(plugin.getConfig().getInt("storage.mysql.pool-size", 10));

            dataSource = new HikariDataSource(config);

            createTables();
            plugin.getLogger().info("MySQL storage initialized successfully!");
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize MySQL storage", e);
        }
    }

    private void createTables() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS player_progress (" +
                "uuid VARCHAR(36) PRIMARY KEY," +
                "mystery_id VARCHAR(255)," +
                "current_stage INT," +
                "completed_stages TEXT," +
                "hints_used TEXT," +
                "puzzle_attempts TEXT," +
                "start_time BIGINT," +
                "completion_time BIGINT," +
                "completed BOOLEAN," +
                "rewards_claimed TEXT" +
                ")";

        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(query);
        }
    }

    @Override
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    @Override
    public CompletableFuture<PlayerProgress> loadPlayerProgress(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT * FROM player_progress WHERE uuid = ?";
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, uuid.toString());
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return fromResultSet(rs);
                }
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to load player progress", e);
            }
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> savePlayerProgress(UUID uuid, PlayerProgress progress) {
        return CompletableFuture.runAsync(() -> {
            String query = "INSERT INTO player_progress (uuid, mystery_id, current_stage, completed_stages, hints_used, puzzle_attempts, start_time, completion_time, completed, rewards_claimed) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "mystery_id = VALUES(mystery_id)," +
                    "current_stage = VALUES(current_stage)," +
                    "completed_stages = VALUES(completed_stages)," +
                    "hints_used = VALUES(hints_used)," +
                    "puzzle_attempts = VALUES(puzzle_attempts)," +
                    "start_time = VALUES(start_time)," +
                    "completion_time = VALUES(completion_time)," +
                    "completed = VALUES(completed)," +
                    "rewards_claimed = VALUES(rewards_claimed)";
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, uuid.toString());
                stmt.setString(2, progress.getMysteryId());
                stmt.setInt(3, progress.getCurrentStage());
                stmt.setString(4, String.join(",", progress.getCompletedStages()));
                stmt.setString(5, String.join(",", progress.getHintsUsed()));
                stmt.setString(6, String.join(",", progress.getPuzzleAttempts()));
                stmt.setLong(7, progress.getStartTime());
                stmt.setLong(8, progress.getCompletionTime());
                stmt.setBoolean(9, progress.isCompleted());
                stmt.setString(10, String.join(",", progress.getRewardsClaimed()));
                stmt.executeUpdate();
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save player progress", e);
            }
        });
    }

    @Override
    public CompletableFuture<Void> deletePlayerProgress(UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            String query = "DELETE FROM player_progress WHERE uuid = ?";
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, uuid.toString());
                stmt.executeUpdate();
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to delete player progress", e);
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> hasPlayerProgress(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT 1 FROM player_progress WHERE uuid = ?";
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, uuid.toString());
                ResultSet rs = stmt.executeQuery();
                return rs.next();
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to check player progress", e);
                return false;
            }
        });
    }

    private PlayerProgress fromResultSet(ResultSet rs) throws SQLException {
        PlayerProgress progress = new PlayerProgress();
        progress.setUuid(UUID.fromString(rs.getString("uuid")));
        progress.setMysteryId(rs.getString("mystery_id"));
        progress.setCurrentStage(rs.getInt("current_stage"));
        progress.setCompletedStages(splitList(rs.getString("completed_stages")));
        progress.setHintsUsed(splitList(rs.getString("hints_used")));
        progress.setPuzzleAttempts(splitList(rs.getString("puzzle_attempts")));
        progress.setStartTime(rs.getLong("start_time"));
        progress.setCompletionTime(rs.getLong("completion_time"));
        progress.setCompleted(rs.getBoolean("completed"));
        progress.setRewardsClaimed(splitList(rs.getString("rewards_claimed")));
        return progress;
    }

    private String[] splitList(String value) {
        return value == null || value.isEmpty() ? new String[0] : value.split(",");
    }
}
