package org.kraby.mcarcinizer.deathkeeper.data;

import java.io.IOException;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.kraby.mcarcinizer.CarcinizerMain;
import org.kraby.mcarcinizer.utils.InventorySerializer;
import org.kraby.mcarcinizer.utils.Tagger;

public class DeathKeeperData {
    public static final String NBT_IS_DEATHKEEPER = "is_deathkeeper";
    public static final String NBT_INVENTORY = "deathkeeper_inventory";
    public static final String NBT_SPAWNMILLIS = "deathkeeper_spawntime";
    public static final String NBT_OWNER = "deathkeeper_owner";
    public static final String NBT_LEVEL = "deathkeeper_level";


    private DeathKeeperData() {
    }



    public static boolean isDeathKeeper(Entity e) {
        return Tagger.isTagged(e, NBT_IS_DEATHKEEPER);
    }

    public static void tagAsDeathKeeper(Entity e) {
        Tagger.tag(e, NBT_IS_DEATHKEEPER);
    }

    public static void setInventory(Entity e, ItemStack[] inv) {
        String serializedInv = InventorySerializer.itemStackArrayToBase64(inv);
        Tagger.store(e, NBT_INVENTORY, PersistentDataType.STRING, serializedInv);
    }

    /**
     * Retrieve the DeathKeeper's kept inventory.
     * @param e
     * @return
     */
    public static ItemStack[] getInventory(Entity e) {
        String serializedInv = Tagger.retrieve(
            e,
            NBT_INVENTORY,
            PersistentDataType.STRING,
            "");

        ItemStack[] newDrops = null;
        try {
            newDrops = InventorySerializer.itemStackArrayFromBase64(serializedInv);
        } catch (IOException e1) {
            CarcinizerMain.getSingleton().getServer().broadcastMessage(
                ChatColor.RED + "DEATHKEEPER: error deserializing, inventory lost. data=["
                + serializedInv + "]"
            );
            newDrops = null;
        }
        return newDrops;
    }

    public static void setSpawnTime(Entity e, long millis) {
        Tagger.store(e, NBT_SPAWNMILLIS, PersistentDataType.LONG, millis);
    }

    /**
     * Get the DeathKeeper's spawn time (player's time of death).
     * @param e
     * @return
     */
    public static long getSpawnTime(Entity e) {
        return Tagger.retrieve(
            e,
            NBT_SPAWNMILLIS,
            PersistentDataType.LONG,
            0L);
    }

    public static void setOwner(Entity e, String owner) {
        Tagger.store(e, NBT_OWNER, PersistentDataType.STRING, owner);
    }

    /**
     * Get the name of the DeathKeeper's owner.
     * @param e
     * @return
     */
    public static String getOwner(Entity e) {
        return Tagger.retrieve(
            e,
            NBT_OWNER,
            PersistentDataType.STRING,
            "");
    }


    public static void setLevel(Entity e, long level) {
        Tagger.store(e, NBT_LEVEL, PersistentDataType.LONG, level);
    }

    /**
     * Get the Deathkeeper's level.
     * @param e
     * @return
     */
    public static long getLevel(Entity e) {
        return Tagger.retrieve(
            e,
            NBT_LEVEL,
            PersistentDataType.LONG,
            0L);
    }

}
