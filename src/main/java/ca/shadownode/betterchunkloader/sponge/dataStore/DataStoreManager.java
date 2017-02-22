package ca.shadownode.betterchunkloader.sponge.dataStore;

import ca.shadownode.betterchunkloader.sponge.BetterChunkLoader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data store manager
 */
public final class DataStoreManager {

    private final BetterChunkLoader plugin;

    private final Map<String, Class<? extends IDataStore>> dataStores = new HashMap<>();
    private IDataStore dataStore;

    public DataStoreManager(BetterChunkLoader plugin) {
        this.plugin = plugin;
    }

    public boolean load() {
        if (getDataStore() != null) {
            clearDataStores();
        }
        registerDataStore("MYSQL", MySQLDataStore.class);
        switch (plugin.getConfig().selectedDataStore.toUpperCase()) {
            case "MYSQL":
                setDataStoreInstance("MYSQL");
                plugin.getLogger().info("Loading datastore: " + getDataStore().getName());
                return getDataStore().load();
            default:
                plugin.getLogger().error("Unable to determine selected datastore.");
                plugin.getLogger().info("Available datastores: " + getAvailableDataStores().toString());
                return false;
        }
    }

    /**
     * Register a new Data Store. This should be run at onLoad()<br>
     *
     * @param dataStoreId ID that identifies this data store <br>
     * @param dataStoreClass a class that implements IDataStore
     */
    public void registerDataStore(String dataStoreId, Class<? extends IDataStore> dataStoreClass) {
        dataStores.put(dataStoreId, dataStoreClass);
    }

    /**
     * Unregisters the data store with the provided id
     *
     * @param dataStoreId
     */
    public void unregisterDataStore(String dataStoreId) {
        dataStores.remove(dataStoreId);
    }

    /**
     * Unregisters all data stores
     */
    public void clearDataStores() {
        dataStores.clear();
    }

    /**
     * List of registered data stores id
     *
     * @return
     */
    public List<String> getAvailableDataStores() {
        List<String> list = new ArrayList<>();
        list.addAll(dataStores.keySet());
        return Collections.unmodifiableList(list);
    }

    /**
     * Sets and instantiate the data store
     *
     * @param dataStoreId
     */
    public void setDataStoreInstance(String dataStoreId) {
        try {
            dataStore = dataStores.get(dataStoreId).getConstructor(BetterChunkLoader.class).newInstance(plugin);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException("Couldn't instantate data store " + dataStoreId);
        }
    }

    /**
     * Gets current data store. Returns null if there isn't an instantiated data
     * store
     *
     * @return
     */
    public IDataStore getDataStore() {
        return dataStore;
    }
}
