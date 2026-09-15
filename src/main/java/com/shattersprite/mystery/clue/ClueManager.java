package com.shattersprite.mystery.clue;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.mystery.Stage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Manages clue delivery and display
 */
public class ClueManager {

    private final MysteryPlugin plugin;
    private final MiniMessage miniMessage;

    public ClueManager(MysteryPlugin plugin) {
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();
    }

    /**
     * Display a clue to a player
     */
    public void displayClue(Player player, Stage stage) {
        String clue = stage.getClue();
        if (clue == null || clue.isEmpty()) {
            return;
        }

        // Parse MiniMessage if available
        Component parsed = LegacyComponentSerializer.legacyAmpersand().deserialize(clue);

        // Default to chat display
        displayChat(player, parsed);

        // Play sound
        playClueSound(player);
    }

    /**
     * Display clue in chat
     */
    private void displayChat(Player player, Component clue) {
        player.sendMessage(Component.text(""));
        player.sendMessage(Component.text("§e✦ CLUE DISCOVERED ✦"));
        player.sendMessage(clue);
        player.sendMessage(Component.text(""));
    }

    /**
     * Display clue as a title
     */
    public void displayTitle(Player player, String title, String subtitle) {
        player.showTitle(
                net.kyori.adventure.title.Title.title(
                        miniMessage.deserialize(title),
                        miniMessage.deserialize(subtitle)
                )
        );
    }

    /**
     * Display clue in action bar
     */
    public void displayActionBar(Player player, String message) {
        player.sendActionBar(miniMessage.deserialize(message));
    }

    /**
     * Display clue as a book
     */
    public void displayBook(Player player, String title, String content) {
        org.bukkit.inventory.ItemStack book = new org.bukkit.inventory.ItemStack(org.bukkit.Material.WRITTEN_BOOK);
        org.bukkit.inventory.meta.BookMeta meta = (org.bukkit.inventory.meta.BookMeta) book.getItemMeta();
        meta.setTitle(title);
        meta.setAuthor("Mystery");
        meta.addPage(content);
        book.setItemMeta(meta);
        player.openBook(book);
    }

    /**
     * Display clue as a written book item
     */
    public void giveClueBook(Player player, String title, String content) {
        org.bukkit.inventory.ItemStack book = new org.bukkit.inventory.ItemStack(org.bukkit.Material.WRITTEN_BOOK);
        org.bukkit.inventory.meta.BookMeta meta = (org.bukkit.inventory.meta.BookMeta) book.getItemMeta();
        meta.setTitle(title);
        meta.setAuthor("Mystery");
        meta.addPage(content);
        book.setItemMeta(meta);
        player.getInventory().addItem(book);
    }

    /**
     * Play the clue sound
     */
    private void playClueSound(Player player) {
        String soundName = plugin.getConfig().getString("effects.clue-sound", "ENTITY_EXPERIENCE_ORB_PICKUP");
        try {
            Sound sound = Sound.valueOf(soundName);
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid sound in config: " + soundName);
        }
    }

    /**
     * Play the stage completion sound
     */
    public void playStageSound(Player player) {
        String soundName = plugin.getConfig().getString("effects.stage-sound", "ENTITY_PLAYER_LEVELUP");
        try {
            Sound sound = Sound.valueOf(soundName);
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid sound in config: " + soundName);
        }
    }

    /**
     * Play the mystery completion sound
     */
    public void playCompleteSound(Player player) {
        String soundName = plugin.getConfig().getString("effects.complete-sound", "ENTITY_PLAYER_LEVELUP");
        try {
            Sound sound = Sound.valueOf(soundName);
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid sound in config: " + soundName);
        }
    }
}
