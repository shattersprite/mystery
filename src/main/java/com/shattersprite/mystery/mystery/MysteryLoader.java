package com.shattersprite.mystery.mystery;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.reward.*;
import com.shattersprite.mystery.mystery.trigger.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Loads mysteries from YAML files
 */
public class MysteryLoader {

    private final MysteryPlugin plugin;

    public MysteryLoader(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Load a mystery from a file
     */
    public Mystery loadMystery(File file) {
        try {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            ConfigurationSection mysterySection = config.getConfigurationSection("mystery");

            if (mysterySection == null) {
                plugin.getLogger().warning("Invalid mystery file: " + file.getName() + " (missing 'mystery' section)");
                return null;
            }

            Mystery mystery = new Mystery();
            mystery.setId(mysterySection.getString("id", file.getName().replace(".yml", "")));
            mystery.setName(mysterySection.getString("name", "Unnamed Mystery"));
            mystery.setDescription(mysterySection.getString("description", ""));
            mystery.setMode(mysterySection.getString("mode", "individual"));
            mystery.setEnabled(mysterySection.getBoolean("enabled", true));

            // Load stages
            ConfigurationSection stagesSection = mysterySection.getConfigurationSection("stages");
            if (stagesSection != null) {
                for (String key : stagesSection.getKeys(false)) {
                    try {
                        int stageNumber = Integer.parseInt(key);
                        ConfigurationSection stageSection = stagesSection.getConfigurationSection(key);
                        Stage stage = loadStage(stageSection, stageNumber);
                        if (stage != null) {
                            mystery.addStage(stageNumber, stage);
                        }
                    } catch (NumberFormatException e) {
                        plugin.getLogger().warning("Invalid stage number: " + key + " in " + file.getName());
                    }
                }
            }

            // Load hints
            ConfigurationSection hintsSection = mysterySection.getConfigurationSection("hints");
            if (hintsSection != null) {
                Map<Integer, Hint> hints = new HashMap<>();
                for (String key : hintsSection.getKeys(false)) {
                    try {
                        int hintNumber = Integer.parseInt(key);
                        ConfigurationSection hintSection = hintsSection.getConfigurationSection(key);
                        Hint hint = loadHint(hintSection);
                        if (hint != null) {
                            hints.put(hintNumber, hint);
                        }
                    } catch (NumberFormatException e) {
                        plugin.getLogger().warning("Invalid hint number: " + key + " in " + file.getName());
                    }
                }
                mystery.setHints(hints);
            }

            plugin.getLogger().info("Loaded mystery: " + mystery.getId());
            return mystery;

        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load mystery: " + file.getName(), e);
            return null;
        }
    }

    /**
     * Load a stage from configuration
     */
    private Stage loadStage(ConfigurationSection section, int number) {
        Stage stage = new Stage();
        stage.setNumber(number);
        stage.setType(section.getString("type", "clue"));
        stage.setClue(section.getString("clue", ""));

        // Load trigger
        ConfigurationSection triggerSection = section.getConfigurationSection("trigger");
        if (triggerSection != null) {
            Trigger trigger = loadTrigger(triggerSection);
            stage.setTrigger(trigger);
        }

        // Load puzzle
        ConfigurationSection puzzleSection = section.getConfigurationSection("puzzle");
        if (puzzleSection != null) {
            Puzzle puzzle = loadPuzzle(puzzleSection);
            stage.setPuzzle(puzzle);
        }

        // Load rewards
        if (section.isList("rewards")) {
            for (Object rewardObj : section.getList("rewards")) {
                if (rewardObj instanceof java.util.Map) {
                    java.util.Map<?, ?> rewardMap = (java.util.Map<?, ?>) rewardObj;
                    Reward reward = loadReward(rewardMap);
                    if (reward != null) {
                        stage.getRewards().add(reward);
                    }
                }
            }
        }

        return stage;
    }

    /**
     * Load a trigger from configuration
     */
    private Trigger loadTrigger(ConfigurationSection section) {
        Trigger trigger = new Trigger();
        trigger.setType(section.getString("type", "location"));

        // Load trigger-specific data
        if (trigger.getType().equals("location")) {
            LocationTriggerData data = new LocationTriggerData();
            data.setWorld(section.getString("world", "world"));
            data.setX(section.getDouble("x", 0));
            data.setY(section.getDouble("y", 64));
            data.setZ(section.getDouble("z", 0));
            data.setRadius(section.getDouble("radius", 5.0));
            trigger.setData(data);
        } else if (trigger.getType().equals("interaction")) {
            InteractionTriggerData data = new InteractionTriggerData();
            data.setBlockType(section.getString("block-type"));
            data.setInteractionType(section.getString("interaction-type", "block"));
            trigger.setData(data);
        } else if (trigger.getType().equals("item")) {
            ItemTriggerData data = new ItemTriggerData();
            data.setMaterial(section.getString("material"));
            data.setAmount(section.getInt("amount", 1));
            data.setName(section.getString("name"));
            data.setExactMatch(section.getBoolean("exact-match", false));
            trigger.setData(data);
        } else if (trigger.getType().equals("command")) {
            CommandTriggerData data = new CommandTriggerData();
            data.setCommand(section.getString("command"));
            data.setExactMatch(section.getBoolean("exact-match", true));
            trigger.setData(data);
        } else if (trigger.getType().equals("chat")) {
            ChatTriggerData data = new ChatTriggerData();
            data.setPhrase(section.getString("phrase"));
            data.setCaseSensitive(section.getBoolean("case-sensitive", false));
            data.setExactMatch(section.getBoolean("exact-match", false));
            trigger.setData(data);
        } else if (trigger.getType().equals("advancement")) {
            AdvancementTriggerData data = new AdvancementTriggerData();
            data.setAdvancement(section.getString("advancement"));
            trigger.setData(data);
        }

        return trigger;
    }

    /**
     * Load a puzzle from configuration
     */
    private Puzzle loadPuzzle(ConfigurationSection section) {
        Puzzle puzzle = new Puzzle();
        puzzle.setType(section.getString("type", "code"));
        puzzle.setAnswer(section.getString("answer", ""));
        puzzle.setAttempts(section.getInt("attempts", -1));
        puzzle.setCaseSensitive(section.getBoolean("case-sensitive", false));
        puzzle.setTimeout(section.getInt("timeout", 0));
        return puzzle;
    }

    /**
     * Load a hint from configuration
     */
    private Hint loadHint(ConfigurationSection section) {
        Hint hint = new Hint();
        hint.setText(section.getString("hint", ""));
        hint.setCost(section.getInt("cost", 0));
        return hint;
    }

    /**
     * Load a reward from configuration
     */
    private Reward loadReward(Map<?, ?> rewardMap) {
        String type = (String) rewardMap.get("type");
        if (type == null) {
            return null;
        }

        Reward reward = new Reward();
        reward.setType(type);

        switch (type.toLowerCase()) {
            case "item":
                ItemRewardData itemData = new ItemRewardData();
                itemData.setMaterial((String) rewardMap.get("material"));
                Object itemAmount = rewardMap.containsKey("amount") ? rewardMap.get("amount") : 1;
                itemData.setAmount(((Number) itemAmount).intValue());
                itemData.setName((String) rewardMap.get("name"));
                reward.setData(itemData);
                break;
            case "command":
                CommandRewardData commandData = new CommandRewardData();
                commandData.setCommand((String) rewardMap.get("command"));
                Object console = rewardMap.containsKey("console") ? rewardMap.get("console") : false;
                commandData.setConsole((Boolean) console);
                reward.setData(commandData);
                break;
            case "experience":
                ExperienceRewardData expData = new ExperienceRewardData();
                expData.setAmount(((Number) rewardMap.get("amount")).intValue());
                Object levels = rewardMap.containsKey("levels") ? rewardMap.get("levels") : false;
                expData.setLevels((Boolean) levels);
                reward.setData(expData);
                break;
            case "money":
                reward.setData(((Number) rewardMap.get("amount")).doubleValue());
                break;
            case "message":
                reward.setData(rewardMap.get("message"));
                break;
            case "title":
                Map<String, String> titleData = new HashMap<>();
                titleData.put("title", String.valueOf(rewardMap.containsKey("title") ? rewardMap.get("title") : ""));
                titleData.put("subtitle", String.valueOf(rewardMap.containsKey("subtitle") ? rewardMap.get("subtitle") : ""));
                reward.setData(titleData);
                break;
            case "sound":
                Map<String, Object> soundData = new HashMap<>();
                soundData.put("sound", rewardMap.get("sound"));
                soundData.put("volume", rewardMap.containsKey("volume") ? rewardMap.get("volume") : 1.0);
                soundData.put("pitch", rewardMap.containsKey("pitch") ? rewardMap.get("pitch") : 1.0);
                reward.setData(soundData);
                break;
            default:
                plugin.getLogger().warning("Unknown reward type: " + type);
                return null;
        }

        return reward;
    }
}
