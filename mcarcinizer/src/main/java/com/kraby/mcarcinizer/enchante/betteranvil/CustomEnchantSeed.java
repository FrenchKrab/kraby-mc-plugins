package com.kraby.mcarcinizer.enchante.betteranvil;

import com.kraby.mcarcinizer.CarcinizerMain;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomEnchantSeed {
    private CustomEnchantSeed() {
    }

    /**
     * Deterministically generate a seed based on the number of time a player attchanted.
     * @param p
     * @return
     */
    public static long getPlayerAttchantCountSeed(Player p) {
        int seed = p.getName().hashCode();
        seed += PlayerPersistentEnchantData.getAttributeEnchantedCount(p);
        CarcinizerMain.getSingleton().getServer().broadcastMessage(
            "c=" + PlayerPersistentEnchantData.getAttributeEnchantedCount(p));
        return seed;
    }

    /**
     * Deterministically generate a seed based on the number of time an item was attchanted.
     * @param s
     * @return
     */
    public static long getItemAttchantCountSeed(ItemStack s) {
        int seed = s.getType().toString().hashCode();
        seed += ItemPersistentEnchantData.getAttributeEnchantedCount(s.getItemMeta());
        return seed;
    }

    /**
     * Deterministically generate a seed based on the state of the item.
     * @param s
     * @return
     */
    public static long getItemChangedSeed(ItemStack s) {
        int seed = s.getType().toString().hashCode();
        seed += s.getItemMeta().serialize().toString().hashCode();
        return seed;
    }
}
