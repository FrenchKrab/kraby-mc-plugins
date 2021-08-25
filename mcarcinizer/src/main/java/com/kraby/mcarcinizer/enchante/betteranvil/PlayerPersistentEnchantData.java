package com.kraby.mcarcinizer.enchante.betteranvil;

import com.kraby.mcarcinizer.utils.Tagger;

import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class PlayerPersistentEnchantData {

    private static String TAG_PLAYER_ATTRIBUTE_ENCHANTED = "enchante_attchanted";

    private PlayerPersistentEnchantData() {
    }


    /**
     * Get the number of time a player has attchanted.
     * @param p
     */
    public static int getAttributeEnchantedCount(Player p) {
        return Tagger.retrieve(p, TAG_PLAYER_ATTRIBUTE_ENCHANTED, PersistentDataType.INTEGER, 0);
    }

    /**
     * Increment the number of time a player has attchanted.
     * @param p
     */
    public static void incrementAttributeEnchantedCount(Player p) {
        int c = Tagger.retrieve(p, TAG_PLAYER_ATTRIBUTE_ENCHANTED, PersistentDataType.INTEGER, 0);
        Tagger.store(p, TAG_PLAYER_ATTRIBUTE_ENCHANTED, PersistentDataType.INTEGER, c + 1);
    }

}
