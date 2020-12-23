package com.kraby.mcarcinizer.carcinizer.expressions.variables;

import java.util.HashMap;
import java.util.Map;
import com.kraby.mcarcinizer.utils.InventorySerializer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PlayerVariables extends HashMap<String, Double> {
    public PlayerVariables(Player player) {
        populate(player);
    }

    protected void populate(Player player) {
        this.putAll(getStatisticsVariables(player));
        this.put("INVSIZE", getInvSizeValue(player.getInventory()));
        this.put("XP", Double.valueOf(player.getTotalExperience()));
    }

    private static Map<String, Double> getStatisticsVariables(Player p) {
        Map<String, Double> variables = new HashMap<>();

        // put all stats
        for (Statistic s : Statistic.values()) {
            try {
                variables.put(s.name(), Double.valueOf(p.getStatistic(s)));
            } catch (Exception e) {
                // Statistic that requires arguments (mob type, material, etc)
            }
        }
        return variables;
    }

    private static double getInvSizeValue(Inventory inv) {
        return InventorySerializer.itemStackArrayToBase64(inv.getContents()).length();
    }
}
