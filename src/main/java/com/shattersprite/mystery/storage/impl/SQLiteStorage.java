package com.shattersprite.mystery.storage.impl;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.player.PlayerProgress;
import com.shattersprite.mystery.storage.Storage;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * SQLite storage implementation
 */
public class SQLiteStorage implements Storage {

    private final MysteryPlugin plugin;
    private Connection connection;

    public SQLiteStorage(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize() {
        try {
            File dataFolder = plugin.getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }

            File databaseFile = new File(dataFolder, "data.db");
            String url = "jdbc:sqlite:" + databaseFile.getAbsolutePath();

            connection = DriverManager.getConnection(url);

            createTables();
            plugin.getLogger().info("SQLite storage initialized successfully!");
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize SQLite storage", e);
        }
    }

    private void createTables() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS player_progress (" +
                "uuid TEXT PRIMARY KEY," +
                "mystery_id TEXT," +
                "current_stage INTEGER," +
                "completed_stages TEXT," +
                "hints_used TEXT," +
                "puzzle_attempts TEXT," +
                "start_time BIGINT," +
                "completion_time BIGINT," +
                "completed BOOLEAN," +
                "rewards_claimed TEXT" +
                ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to close SQLite connection", e);
        }
    }

    @Override
    public CompletableFuture<PlayerProgress> loadPlayerProgress(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT * FROM player_progress WHERE uuid = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
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
            String query = "INSERT OR REPLACE INTO player_progress VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
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
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
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
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
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
