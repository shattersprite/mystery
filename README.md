# Mystery

A Minecraft plugin for creating interactive mysteries, clue hunts, scavenger hunts, puzzles, and story-driven adventures on your server.

## Features

- 🕵️ Interactive mystery creation without coding
- 🧩 Modular puzzle system
- 🔎 Rich clue system with multiple delivery methods
- 📍 Location and interaction triggers
- 🌳 Multi-stage mysteries with optional branching paths
- 👥 Multiplayer support (individual, team, server-wide)
- 💾 Persistent player progress
- 🎁 Configurable rewards
- 💡 Hint system with optional costs
- 🛠️ In-game editor for easy location setting
- 🧪 Testing mode for administrators
- 📜 Shareable mystery configuration files
- 🔌 Optional integrations (Vault, PlaceholderAPI, Citizens, WorldGuard, LuckPerms)
- ⚡ Lightweight performance
- 🎨 Polished presentation with sounds and effects
- 🔌 Developer API for extensions

## Installation

1. Download the latest version of Mystery
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. Configure the plugin in `plugins/Mystery/config.yml`
5. Create mysteries in the `plugins/Mystery/mysteries/` folder

## Requirements

- Minecraft 1.21+ (Paper recommended)
- Java 17+

## Optional Dependencies

- Vault (for economy integration)
- PlaceholderAPI (for placeholders)
- Citizens (for NPC integration - planned)
- WorldGuard (for region-based triggers - planned)
- LuckPerms (for permission rewards - planned)

## Quick Start

1. Create a mystery file in `plugins/Mystery/mysteries/your-mystery.yml`
2. Use `/mystery list` to see available mysteries
3. Use `/mystery start <id>` to begin a mystery
4. Follow the clues and complete the stages!

## Commands

### Player Commands

- `/mystery help` - Show help
- `/mystery list` - List available mysteries
- `/mystery info <id>` - View mystery details
- `/mystery start <id>` - Start a mystery
- `/mystery stop` - Stop current mystery
- `/mystery progress` - View your progress
- `/mystery progress [player]` - View another player's progress (admin)
- `/mystery hint` - Request a hint
- `/mystery reset` - Reset your progress
- `/mystery reset [player]` - Reset another player's progress (admin)

### Admin Commands

- `/mystery create` - Create a new mystery (manual YAML creation)
- `/mystery edit <id>` - Edit a mystery (manual YAML editing)
- `/mystery delete <id>` - Delete a mystery
- `/mystery reload` - Reload the plugin
- `/mystery test <id>` - Test a mystery in testing mode
- `/mystery setlocation <id> <stage>` - Set a stage location to your current position

## Permissions

### Player Permissions

- `mystery.play` - Play mysteries
- `mystery.hint` - Request hints
- `mystery.progress` - View progress

### Admin Permissions

- `mystery.admin` - All admin permissions
- `mystery.create` - Create mysteries
- `mystery.edit` - Edit mysteries
- `mystery.delete` - Delete mysteries
- `mystery.start` - Start mysteries server-wide
- `mystery.stop` - Stop mysteries server-wide
- `mystery.reset` - Reset player progress
- `mystery.reload` - Reload plugin
- `mystery.test` - Test mysteries
- `mystery.bypass` - Bypass restrictions

## Configuration

### config.yml

```yaml
# Storage settings
storage:
  type: sqlite  # Options: sqlite, mysql

  # MySQL configuration (only used if type is mysql)
  mysql:
    host: localhost
    port: 3306
    database: mystery
    username: root
    password: ""
    pool-size: 10

# Hint system
hints:
  enabled: true
  cost-type: experience  # Options: experience, economy, points, none

# Trigger settings
triggers:
  location-check-interval: 20  # ticks (20 ticks = 1 second)
  location-distance: 3.0  # blocks

# Testing mode
testing:
  enabled: true

# Debug mode
debug: false

# GUI settings
gui:
  create-title: "CREATE MYSTERY"
  edit-title: "EDIT MYSTERY"
  list-title: "MYSTERIES"

# Effects
effects:
  clue-sound: "ENTITY_EXPERIENCE_ORB_PICKUP"
  stage-sound: "ENTITY_PLAYER_LEVELUP"
  complete-sound: "ENTITY_PLAYER_LEVELUP"
  puzzle-sound: "ENTITY_PLAYER_LEVELUP"
```

### Creating Mysteries

Mysteries are defined in YAML files in the `mysteries/` folder. Example:

```yaml
mystery:
  id: forgotten-founder
  name: "&6The Forgotten Founder"
  description: "&7Something has been hidden beneath the server..."

  # Multiplayer mode: individual, first-completion, team, server-wide
  mode: individual

  # Stages are numbered sequentially
  stages:
    1:
      type: clue
      clue: "&eWhere the world first wakes, your journey begins."
      trigger:
        type: location
        world: world
        x: 0
        y: 64
        z: 0
        radius: 5

    2:
      type: clue
      clue: "&2Among the trees, something watches from above."
      trigger:
        type: location
        world: world
        x: 100
        y: 70
        z: 100
        radius: 5

    3:
      type: puzzle
      clue: "&aWhat year was this server founded?"
      puzzle:
        type: code
        answer: "2026"
        attempts: 3
        case-sensitive: false

    4:
      type: final
      clue: "&eYou have discovered the truth!"
      rewards:
        - type: item
          material: DIAMOND
          amount: 5
        - type: experience
          amount: 1000
        - type: command
          command: "title %player% title {\"text\":\"The Forgotten Founder\",\"color\":\"gold\"}"

  # Hints configuration
  hints:
    enabled: true
    1:
      hint: "&7Look for the spawn point."
      cost: 0
    2:
      hint: "&7Search near the forest."
      cost: 100
    3:
      hint: "&7The answer is the current year."
      cost: 250
```

## Stage Types

- `clue` - Display a clue to the player
- `puzzle` - Require the player to solve a puzzle
- `final` - Final stage with rewards

## Trigger Types

- `location` - Player reaches a specific location
- `interaction` - Player interacts with a block/button/lever
- `item` - Player obtains a specific item
- `command` - Player executes a specific command
- `chat` - Player says a specific phrase
- `advancement` - Player obtains a Minecraft advancement
- `manual` - Staff manually completes the stage

## Puzzle Types

- `code` - Enter a code/answer
- `word` - Solve a word puzzle
- `math` - Solve a math problem
- `sequence` - Complete a sequence
- `multiple_choice` - Select from multiple choices

## Reward Types

- `item` - Give an item to the player
- `command` - Execute a command
- `experience` - Give experience points or levels
- `money` - Give money (requires Vault)
- `message` - Send a message to the player
- `title` - Display a title
- `sound` - Play a sound

## Multiplayer Modes

- `individual` - Each player progresses independently
- `first-completion` - First player to finish wins
- `team` - Multiple players share progress
- `server-wide` - Everyone works toward the same mystery

## PlaceholderAPI Integration

When PlaceholderAPI is installed, the following placeholders are available:

- `%mystery_current%` - Current mystery name
- `%mystery_current_id%` - Current mystery ID
- `%mystery_stage%` - Current stage number
- `%mystery_progress%` - Progress percentage
- `%mystery_completed%` - Completed stages count
- `%mystery_total%` - Total stages
- `%mystery_status%` - Status (In Progress/Completed)
- `%mystery_time%` - Time spent in seconds
- `%mystery_time_formatted%` - Time formatted (mm:ss)

## Development

This plugin is built with Maven. To build:

```bash
mvn clean package
```

The compiled JAR will be in `target/`.

## API Documentation

For developers who want to extend Mystery, see [API.md](API.md) for detailed API documentation including events and extension points.

## License

This project is licensed under the MIT License.

## Support

For issues and suggestions, please visit the GitHub repository.
