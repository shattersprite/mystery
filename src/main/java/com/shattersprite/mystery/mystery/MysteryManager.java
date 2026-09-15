package com.shattersprite.mystery.mystery;

import com.shattersprite.mystery.MysteryPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Manages mysteries
 */
public class MysteryManager {

    private final MysteryPlugin plugin;
    private final Map<String, Mystery> mysteries;
    private final MysteryLoader loader;

    public MysteryManager(MysteryPlugin plugin) {
        this.plugin = plugin;
        this.mysteries = new HashMap<>();
        this.loader = new MysteryLoader(plugin);
    }

    /**
     * Load all mysteries from the mysteries folder
     */
    public void loadMysteries() {
        mysteries.clear();

        File mysteriesFolder = new File(plugin.getDataFolder(), "mysteries");
        if (!mysteriesFolder.exists()) {
            mysteriesFolder.mkdirs();
            plugin.saveResource("mysteries/example-mystery.yml", false);
        }

        File[] files = mysteriesFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null || files.length == 0) {
            plugin.getLogger().info("No mystery files found in mysteries folder.");
            return;
        }

        int loaded = 0;
        for (File file : files) {
            Mystery mystery = loader.loadMystery(file);
            if (mystery != null && mystery.isEnabled()) {
                mysteries.put(mystery.getId(), mystery);
                loaded++;
            }
        }

        plugin.getLogger().info("Loaded " + loaded + " mysteries.");
    }

    /**
     * Get a mystery by ID
     */
    public Mystery getMystery(String id) {
        return mysteries.get(id);
    }

    /**
     * Get all mysteries
     */
    public Map<String, Mystery> getMysteries() {
        return new HashMap<>(mysteries);
    }

    /**
     * Add a mystery
     */
    public void addMystery(Mystery mystery) {
        mysteries.put(mystery.getId(), mystery);
    }

    /**
     * Remove a mystery
     */
    public void removeMystery(String id) {
        mysteries.remove(id);
    }

    /**
     * Reload mysteries
     */
    public void reloadMysteries() {
        mysteries.clear();
        loadMysteries();
    }

    /**
     * Save a mystery to a file
     */
    public void saveMystery(Mystery mystery) {
        File mysteriesFolder = new File(plugin.getDataFolder(), "mysteries");
        if (!mysteriesFolder.exists()) {
            mysteriesFolder.mkdirs();
        }

        File file = new File(mysteriesFolder, mystery.getId() + ".yml");

        try {
            org.bukkit.configuration.file.YamlConfiguration config = new org.bukkit.configuration.file.YamlConfiguration();

            config.set("mystery.id", mystery.getId());
            config.set("mystery.name", mystery.getName());
            config.set("mystery.description", mystery.getDescription());
            config.set("mystery.mode", mystery.getMode());
            config.set("mystery.enabled", mystery.isEnabled());

            // Save stages
            for (Map.Entry<Integer, Stage> entry : mystery.getStages().entrySet()) {
                int stageNumber = entry.getKey();
                Stage stage = entry.getValue();

                config.set("mystery.stages." + stageNumber + ".type", stage.getType());
                config.set("mystery.stages." + stageNumber + ".clue", stage.getClue());

                // Save trigger
                if (stage.getTrigger() != null) {
                    config.set("mystery.stages." + stageNumber + ".trigger.type", stage.getTrigger().getType());

                    if (stage.getTrigger().getData() instanceof com.shattersprite.mystery.mystery.trigger.LocationTriggerData) {
                        com.shattersprite.mystery.mystery.trigger.LocationTriggerData data =
                                (com.shattersprite.mystery.mystery.trigger.LocationTriggerData) stage.getTrigger().getData();
                        config.set("mystery.stages." + stageNumber + ".trigger.world", data.getWorld());
                        config.set("mystery.stages." + stageNumber + ".trigger.x", data.getX());
                        config.set("mystery.stages." + stageNumber + ".trigger.y", data.getY());
                        config.set("mystery.stages." + stageNumber + ".trigger.z", data.getZ());
                        config.set("mystery.stages." + stageNumber + ".trigger.radius", data.getRadius());
                    } else if (stage.getTrigger().getData() instanceof com.shattersprite.mystery.mystery.trigger.InteractionTriggerData data) {
                        config.set("mystery.stages." + stageNumber + ".trigger.block-type", data.getBlockType());
                        config.set("mystery.stages." + stageNumber + ".trigger.interaction-type", data.getInteractionType());
                    } else if (stage.getTrigger().getData() instanceof com.shattersprite.mystery.mystery.trigger.ItemTriggerData data) {
                        config.set("mystery.stages." + stageNumber + ".trigger.material", data.getMaterial());
                        config.set("mystery.stages." + stageNumber + ".trigger.amount", data.getAmount());
                        config.set("mystery.stages." + stageNumber + ".trigger.name", data.getName());
                        config.set("mystery.stages." + stageNumber + ".trigger.exact-match", data.isExactMatch());
                    } else if (stage.getTrigger().getData() instanceof com.shattersprite.mystery.mystery.trigger.CommandTriggerData data) {
                        config.set("mystery.stages." + stageNumber + ".trigger.command", data.getCommand());
                        config.set("mystery.stages." + stageNumber + ".trigger.exact-match", data.isExactMatch());
                    } else if (stage.getTrigger().getData() instanceof com.shattersprite.mystery.mystery.trigger.ChatTriggerData data) {
                        config.set("mystery.stages." + stageNumber + ".trigger.phrase", data.getPhrase());
                        config.set("mystery.stages." + stageNumber + ".trigger.case-sensitive", data.isCaseSensitive());
                        config.set("mystery.stages." + stageNumber + ".trigger.exact-match", data.isExactMatch());
                    } else if (stage.getTrigger().getData() instanceof com.shattersprite.mystery.mystery.trigger.AdvancementTriggerData data) {
                        config.set("mystery.stages." + stageNumber + ".trigger.advancement", data.getAdvancement());
                    }
                }

                List<Map<String, Object>> rewards = new ArrayList<>();
                for (com.shattersprite.mystery.reward.Reward reward : stage.getRewards()) {
                    Map<String, Object> rewardMap = new HashMap<>();
                    rewardMap.put("type", reward.getType());
                    if (reward.getData() instanceof com.shattersprite.mystery.reward.ItemRewardData data) {
                        rewardMap.put("material", data.getMaterial());
                        rewardMap.put("amount", data.getAmount());
                        rewardMap.put("name", data.getName());
                    } else if (reward.getData() instanceof com.shattersprite.mystery.reward.CommandRewardData data) {
                        rewardMap.put("command", data.getCommand());
                        rewardMap.put("console", data.isConsole());
                    } else if (reward.getData() instanceof com.shattersprite.mystery.reward.ExperienceRewardData data) {
                        rewardMap.put("amount", data.getAmount());
                        rewardMap.put("levels", data.isLevels());
                    } else {
                        rewardMap.put("amount", reward.getData());
                    }
                    rewards.add(rewardMap);
                }
                config.set("mystery.stages." + stageNumber + ".rewards", rewards);

                // Save puzzle
                if (stage.getPuzzle() != null) {
                    config.set("mystery.stages." + stageNumber + ".puzzle.type", stage.getPuzzle().getType());
                    config.set("mystery.stages." + stageNumber + ".puzzle.answer", stage.getPuzzle().getAnswer());
                    config.set("mystery.stages." + stageNumber + ".puzzle.attempts", stage.getPuzzle().getAttempts());
                    config.set("mystery.stages." + stageNumber + ".puzzle.case-sensitive", stage.getPuzzle().isCaseSensitive());
                }
            }

            // Save hints
            for (Map.Entry<Integer, Hint> entry : mystery.getHints().entrySet()) {
                int hintNumber = entry.getKey();
                Hint hint = entry.getValue();
                config.set("mystery.hints." + hintNumber + ".hint", hint.getText());
                config.set("mystery.hints." + hintNumber + ".cost", hint.getCost());
            }

            config.save(file);
            plugin.getLogger().info("Saved mystery: " + mystery.getId());

        } catch (IOException e) {
            plugin.getLogger().log(java.util.logging.Level.SEVERE, "Failed to save mystery: " + mystery.getId(), e);
        }
    }

    /**
     * Delete a mystery file
     */
    public boolean deleteMysteryFile(String id) {
        File mysteriesFolder = new File(plugin.getDataFolder(), "mysteries");
        File file = new File(mysteriesFolder, id + ".yml");

        if (file.exists()) {
            if (file.delete()) {
                plugin.getLogger().info("Deleted mystery file: " + id);
                return true;
            } else {
                plugin.getLogger().warning("Failed to delete mystery file: " + id);
                return false;
            }
        }

        return false;
    }
}
