package com.kraby.mcarcinizer.utils;

import com.kraby.mcarcinizer.CarcinizerMain;
import org.apache.commons.lang.StringUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Tagger {
    private Tagger() {
    }

    /**
     * Apply an empty tag to a given entity.
     * @param e
     * @param tag
     */
    public static void tag(Entity e, String tag) {
        if (StringUtils.isEmpty(tag)) {
            return;
        }

        PersistentDataContainer dataContainer = e.getPersistentDataContainer();

        //create key tag and store it in the data container
        NamespacedKey tagKey = createTagNamespacedKey(tag);
        dataContainer.set(tagKey, PersistentDataType.BYTE, (byte)0);
    }

    /**
     * Remove a tag (empty or not) from a given entity.
     * @param e
     * @param tag
     */
    public static void untag(Entity e, String tag) {
        if (StringUtils.isEmpty(tag)) {
            return;
        }

        PersistentDataContainer dataContainer = e.getPersistentDataContainer();
        NamespacedKey tagKey = createTagNamespacedKey(tag);
        dataContainer.remove(tagKey);
    }

    /**
     * Store data onto the entity.
     * @param <T>
     * @param <Z>
     * @param e
     * @param tag
     * @param type
     * @param value
     */
    public static <T,Z> void store(Entity e, String tag, PersistentDataType<T,Z> type, Z value) {
        PersistentDataContainer dataContainer = e.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(CarcinizerMain.getSingleton(), tag);
        dataContainer.set(key, type, value);
    }

    /**
     * Retrieve stored data from an entity.
     * @param <T>
     * @param <Z>
     * @param e
     * @param tag
     * @param type
     * @return
     */
    public static <T,Z> Z retrieve(Entity e, String tag, PersistentDataType<T,Z> type) {
        PersistentDataContainer dataContainer = e.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(CarcinizerMain.getSingleton(), tag);
        return dataContainer.get(key, type);
    }

    /**
     * Retrive store data from an entity, with a default value.
     * @param <T>
     * @param <Z>
     * @param e
     * @param tag
     * @param type
     * @param defaultValue
     * @return
     */
    public static <T,Z> Z retrieve(
            Entity e,
            String tag,
            PersistentDataType<T,Z> type,
            Z defaultValue) {
        PersistentDataContainer dataContainer = e.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(CarcinizerMain.getSingleton(), tag);
        return dataContainer.getOrDefault(key, type, defaultValue);
    }

    /**
     * Test if the entity has a given tag.
     * @param e
     * @param tag
     * @return
     */
    public static boolean isTagged(Entity e, String tag) {
        if (StringUtils.isEmpty(tag)) {
            return false;
        }

        PersistentDataContainer dataContainer = e.getPersistentDataContainer();
        NamespacedKey tagKey = createTagNamespacedKey(tag);
        return dataContainer.has(tagKey, PersistentDataType.BYTE);
    }


    private static NamespacedKey createTagNamespacedKey(String tag) {
        return new NamespacedKey(CarcinizerMain.getSingleton(), tag);
    }


}
