# Mystery Plugin API Documentation

## Overview

The Mystery plugin provides a comprehensive API for developers to extend functionality, create custom mysteries programmatically, and integrate with other plugins.

## Events

The plugin fires the following events that can be listened to by other plugins:

### MysteryStartEvent
Called when a player starts a mystery.

```java
@EventHandler
public void onMysteryStart(MysteryStartEvent event) {
    Player player = event.getPlayer();
    Mystery mystery = event.getMystery();
    // Custom logic
}
```

### MysteryStageCompleteEvent
Called when a player completes a stage.

```java
@EventHandler
public void onStageComplete(MysteryStageCompleteEvent event) {
    Player player = event.getPlayer();
    Stage stage = event.getStage();
    // Custom logic
}
```

### MysteryClueRevealEvent
Called when a clue is revealed to a player. The clue can be modified.

```java
@EventHandler
public void onClueReveal(MysteryClueRevealEvent event) {
    // Modify the clue
    event.setClue(event.getClue() + " [Modified]");
}
```

### MysteryPuzzleSolveEvent
Called when a player solves a puzzle.

```java
@EventHandler
public void onPuzzleSolve(MysteryPuzzleSolveEvent event) {
    Player player = event.getPlayer();
    String answer = event.getAnswer();
    // Custom logic
}
```

### MysteryCompleteEvent
Called when a player completes a mystery.

```java
@EventHandler
public void onMysteryComplete(MysteryCompleteEvent event) {
    Player player = event.getPlayer();
    long completionTime = event.getCompletionTime();
    // Custom logic
}
```

### MysteryFailEvent
Called when a player fails a mystery.

```java
@EventHandler
public void onMysteryFail(MysteryFailEvent event) {
    Player player = event.getPlayer();
    String reason = event.getReason();
    // Custom logic
}
```

## API Access

### Get the Mystery Plugin Instance

```java
MysteryPlugin plugin = (MysteryPlugin) Bukkit.getPluginManager().getPlugin("Mystery");
```

### Access Managers

```java
MysteryManager mysteryManager = plugin.getMysteryManager();
StageManager stageManager = plugin.getStageManager();
PlayerManager playerManager = plugin.getPlayerManager();
RewardManager rewardManager = plugin.getRewardManager();
ClueManager clueManager = plugin.getClueManager();
TriggerManager triggerManager = plugin.getTriggerManager();
PuzzleManager puzzleManager = plugin.getPuzzleManager();
TestingManager testingManager = plugin.getTestingManager();
MultiplayerManager multiplayerManager = plugin.getMultiplayerManager();
```

### Creating Mysteries Programmatically

```java
// Create a new mystery
Mystery mystery = new Mystery();
mystery.setId("custom-mystery");
mystery.setName("&6Custom Mystery");
mystery.setDescription("&7A programmatically created mystery");
mystery.setMode("individual");

// Create a stage
Stage stage1 = new Stage();
stage1.setNumber(1);
stage1.setType("clue");
stage1.setClue("&eFind the hidden location");

// Create a location trigger
LocationTriggerData triggerData = new LocationTriggerData();
triggerData.setWorld("world");
triggerData.setX(100);
triggerData.setY(64);
triggerData.setZ(100);
triggerData.setRadius(5);

Trigger trigger = new Trigger();
trigger.setType("location");
trigger.setData(triggerData);
stage1.setTrigger(trigger);

// Add stage to mystery
mystery.addStage(1, stage1);

// Save the mystery
plugin.getMysteryManager().addMystery(mystery);
plugin.getMysteryManager().saveMystery(mystery);
```

### Custom Stage Types

To create custom stage types, extend the Stage class and implement custom trigger logic:

```java
public class CustomStage extends Stage {
    private String customData;

    // Custom methods
}
```

### Custom Puzzle Types

To create custom puzzle types, extend the Puzzle class:

```java
public class CustomPuzzle extends Puzzle {
    private String customLogic;

    // Custom validation logic
}
```

### Custom Reward Types

To create custom reward types, extend the Reward class:

```java
public class CustomReward extends Reward {
    private Object customData;

    // Custom reward logic
}
```

## Integration Points

### PlaceholderAPI

The plugin provides the following placeholders when PlaceholderAPI is installed:

- `%mystery_current%` - Current mystery name
- `%mystery_current_id%` - Current mystery ID
- `%mystery_stage%` - Current stage number
- `%mystery_progress%` - Progress percentage
- `%mystery_completed%` - Completed stages count
- `%mystery_total%` - Total stages
- `%mystery_status%` - Status (In Progress/Completed)
- `%mystery_time%` - Time spent in seconds
- `%mystery_time_formatted%` - Time formatted (mm:ss)

### Vault

The plugin integrates with Vault for economy-based hint costs and money rewards.

### Citizens (Planned)

Future integration with Citizens for NPC-based triggers and interactions.

### WorldGuard (Planned)

Future integration with WorldGuard for region-based triggers.

### LuckPerms (Planned)

Future integration with LuckPerms for permission-based rewards.

## License

This API is part of the Mystery plugin and follows the same license.
