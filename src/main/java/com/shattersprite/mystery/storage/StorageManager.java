package com.shattersprite.mystery.storage;

import com.shattersprite.mystery.MysteryPlugin;
import com.shattersprite.mystery.storage.impl.SQLiteStorage;
import com.shattersprite.mystery.storage.impl.MySQLStorage;
import com.shattersprite.mystery.storage.Storage;

/**
 * Manages storage backend
 */
public class StorageManager {

    private final MysteryPlugin plugin;
    private Storage storage;

    public StorageManager(MysteryPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Initialize the storage backend
     */
    public void initialize() {
        String type = plugin.getConfigManager().getStorageType().toLowerCase();

        switch (type) {
            case "mysql":
                storage = new MySQLStorage(plugin);
                break;
            case "sqlite":
            default:
                storage = new SQLiteStorage(plugin);
                break;
        }

        storage.initialize();
        plugin.getLogger().info("Storage initialized: " + type);
    }

    /**
     * Get the storage instance
     */
    public Storage getStorage() {
        return storage;
    }

    /**
     * Close the storage connection
     */
    public void close() {
        if (storage != null) {
            storage.close();
        }
    }
}
