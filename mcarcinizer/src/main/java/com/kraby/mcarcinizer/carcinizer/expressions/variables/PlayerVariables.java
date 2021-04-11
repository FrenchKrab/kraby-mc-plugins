package com.kraby.mcarcinizer.carcinizer.expressions.variables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.kraby.mcarcinizer.utils.InventorySerializer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PlayerVariables extends HashMap<String, Double> {

    public static final String VARIABLE_INVSIZE = "INVSIZE";
    public static final String VARIABLE_XP = "XP";

    public PlayerVariables(Player player) {
        populate(player);
    }

    protected void populate(Player player) {
        this.putAll(getStatisticsVariables(player));
        this.put(VARIABLE_INVSIZE, getInvSizeValue(player.getInventory()));
        this.put(VARIABLE_XP, Double.valueOf(player.getTotalExperience()));
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

    public static double getInvSizeValue(Inventory inv) {
        return InventorySerializer.itemStackArrayToBase64(inv.getContents()).length();
    }

    /**
     * Returns all accepted player variables.
     * @return
     */
    public static List<String> getVariableNames() {
        List<String> list = new ArrayList<>();
        for (Statistic s : Statistic.values()) {
            list.add(s.name());
        }
        list.add(VARIABLE_INVSIZE);
        list.add(VARIABLE_XP);
        return list;
    }
}
