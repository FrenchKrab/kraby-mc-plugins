package com.kraby.mcarcinizer.enchante.betteranvil;

import com.kraby.mcarcinizer.utils.Tagger;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemPersistentEnchantData {

    private static String TAG_ITEM_ATTRIBUTE_ENCHANTED = "enchante_attchanted";

    private ItemPersistentEnchantData() {
    }


    /**
     * Get the number of time an item has been attchanted.
     * @param m
     */
    public static int getAttributeEnchantedCount(ItemMeta m) {
        return Tagger.retrieve(m, TAG_ITEM_ATTRIBUTE_ENCHANTED, PersistentDataType.INTEGER, 0);
    }

    /**
     * Increment the number of time an item has been attchanted.
     * @param m
     */
    public static void incrementAttributeEnchantedCount(ItemMeta m) {
        int c = Tagger.retrieve(m, TAG_ITEM_ATTRIBUTE_ENCHANTED, PersistentDataType.INTEGER, 0);
        Tagger.store(m, TAG_ITEM_ATTRIBUTE_ENCHANTED, PersistentDataType.INTEGER, c + 1);
    }

}
