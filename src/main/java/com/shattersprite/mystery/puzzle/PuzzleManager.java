package com.shattersprite.mystery.puzzle;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.api.MysteryFailEvent;
import com.shattersprite.mystery.api.MysteryPuzzleSolveEvent;
import com.shattersprite.mystery.mystery.Mystery;
import com.shattersprite.mystery.mystery.Puzzle;
import com.shattersprite.mystery.mystery.Stage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages puzzle solving
 */
public class PuzzleManager {

    private final MysteryPlugin plugin;
    private final Map<UUID, PuzzleSession> activeSessions;

    public PuzzleManager(MysteryPlugin plugin) {
        this.plugin = plugin;
        this.activeSessions = new HashMap<>();
    }

    /**
     * Start a puzzle for a player
     */
    public void startPuzzle(Player player, Stage stage) {
        if (stage.getPuzzle() == null) {
            return;
        }

        UUID uuid = player.getUniqueId();
        Puzzle puzzle = stage.getPuzzle();

        // Check if player already has an active puzzle
        if (activeSessions.containsKey(uuid)) {
            player.sendMessage(plugin.getMessagesConfig().getPrefix() + "§cYou already have an active puzzle.");
            return;
        }

        // Create puzzle session
        PuzzleSession session = new PuzzleSession(uuid, stage.getNumber(), puzzle);
        activeSessions.put(uuid, session);

        // Display puzzle based on type
        displayPuzzle(player, puzzle);
    }

    /**
     * Display a puzzle to a player
     */
    private void displayPuzzle(Player player, Puzzle puzzle) {
        String type = puzzle.getType();

        switch (type.toLowerCase()) {
            case "code":
                displayCodePuzzle(player, puzzle);
                break;
            case "word":
                displayWordPuzzle(player, puzzle);
                break;
            case "math":
                displayMathPuzzle(player, puzzle);
                break;
            case "sequence":
                displaySequencePuzzle(player, puzzle);
                break;
            case "multiple_choice":
                displayMultipleChoicePuzzle(player, puzzle);
                break;
            default:
                plugin.getLogger().warning("Unknown puzzle type: " + type);
                break;
        }
    }

    /**
     * Display code puzzle
     */
    private void displayCodePuzzle(Player player, Puzzle puzzle) {
        player.sendMessage("§6=== THE LOCK ===");
        player.sendMessage("§eEnter the code:");
        player.sendMessage("§7");
        player.sendMessage("§eType your answer in chat.");
    }

    /**
     * Display word puzzle
     */
    private void displayWordPuzzle(Player player, Puzzle puzzle) {
        player.sendMessage("§6=== THE RIDDLE ===");
        player.sendMessage("§eSolve the riddle:");
        player.sendMessage("§7");
        player.sendMessage("§eType your answer in chat.");
    }

    /**
     * Display math puzzle
     */
    private void displayMathPuzzle(Player player, Puzzle puzzle) {
        player.sendMessage("§6=== THE CALCULATION ===");
        player.sendMessage("§eSolve the equation:");
        player.sendMessage("§7");
        player.sendMessage("§eType your answer in chat.");
    }

    /**
     * Display sequence puzzle
     */
    private void displaySequencePuzzle(Player player, Puzzle puzzle) {
        player.sendMessage("§6=== THE PATTERN ===");
        player.sendMessage("§eComplete the sequence:");
        player.sendMessage("§7");
        player.sendMessage("§eType your answer in chat.");
    }

    /**
     * Display multiple choice puzzle
     */
    private void displayMultipleChoicePuzzle(Player player, Puzzle puzzle) {
        player.sendMessage("§6=== THE CHOICE ===");
        player.sendMessage("§eSelect the correct answer:");
        player.sendMessage("§7");
        player.sendMessage("§eType your choice in chat.");
    }

    /**
     * Handle a puzzle answer
     */
    public boolean handleAnswer(Player player, String answer) {
        UUID uuid = player.getUniqueId();
        PuzzleSession session = activeSessions.get(uuid);

        if (session == null) {
            return false;
        }

        Puzzle puzzle = session.getPuzzle();

        // Check answer
        boolean correct = checkAnswer(answer, puzzle);

        if (correct) {
            // Puzzle solved
            activeSessions.remove(uuid);
            player.sendMessage(plugin.getMessagesConfig().getMessage("puzzle-correct"));
            plugin.getClueManager().playStageSound(player);

            Stage stage = plugin.getStageManager().getCurrentStage(player);
            Mystery mystery = plugin.getMysteryManager().getMystery(
                    plugin.getPlayerManager().getProgress(uuid).getMysteryId());
            if (stage != null && mystery != null) {
                plugin.getServer().getPluginManager().callEvent(
                        new MysteryPuzzleSolveEvent(player, mystery, stage, puzzle, answer));
            }

            // Complete the stage
            plugin.getStageManager().completeStage(player, session.getStageNumber());
        } else {
            // Wrong answer
            session.incrementAttempts();

            if (puzzle.getAttempts() > 0 && session.getAttempts() >= puzzle.getAttempts()) {
                // No more attempts
                activeSessions.remove(uuid);
                player.sendMessage(plugin.getMessagesConfig().getMessage("puzzle-no-attempts"));
                Stage stage = plugin.getStageManager().getCurrentStage(player);
                if (stage != null) {
                    Mystery mystery = plugin.getMysteryManager().getMystery(
                            plugin.getPlayerManager().getProgress(uuid).getMysteryId());
                    if (mystery != null) {
                        plugin.getServer().getPluginManager().callEvent(
                                new MysteryFailEvent(player, mystery, "puzzle attempts exhausted"));
                    }
                }
            } else {
                int remaining = puzzle.getAttempts() > 0 ? puzzle.getAttempts() - session.getAttempts() : -1;
                if (remaining > 0) {
                    player.sendMessage(plugin.getMessagesConfig().getMessage("puzzle-attempts-remaining", "attempts", String.valueOf(remaining)));
                } else {
                    player.sendMessage(plugin.getMessagesConfig().getMessage("puzzle-incorrect"));
                }
            }
        }

        return true;
    }

    /**
     * Check if an answer is correct
     */
    private boolean checkAnswer(String answer, Puzzle puzzle) {
        String correctAnswer = puzzle.getAnswer();

        if (puzzle.isCaseSensitive()) {
            return answer.equals(correctAnswer);
        } else {
            return answer.equalsIgnoreCase(correctAnswer);
        }
    }

    /**
     * End a puzzle session
     */
    public void endPuzzle(UUID uuid) {
        activeSessions.remove(uuid);
    }

    /**
     * Get active puzzle session for a player
     */
    public PuzzleSession getSession(UUID uuid) {
        return activeSessions.get(uuid);
    }

    /**
     * Check if a player has an active puzzle
     */
    public boolean hasActivePuzzle(UUID uuid) {
        return activeSessions.containsKey(uuid);
    }
}
