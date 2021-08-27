package com.kraby.mcarcinizer.carcinizer.utils;

import java.util.Collection;

import org.bukkit.Material;

public class ItemCategoryChecker {
    private static final String CAT_ALL = "ALL";
    private static final String CAT_ANY = "ANY";
    private static final String CAT_OBJECTS = "OBJECTS";
    private static final String CAT_WEAPONS = "WEAPONS";
    private static final String CAT_ARMORS = "ARMORS";
    private static final String CAT_TOOLS = "TOOLS";
    private static final String CAT_SHOVEL = "SHOVEL";
    private static final String CAT_PICKAXE = "PICKAXE";
    private static final String CAT_AXE = "AXE";
    private static final String CAT_HOE = "HOE";
    private static final String CAT_SWORD = "TOOLS";

    private ItemCategoryChecker() {
    }

    /**
     * Returns true if at least one category matches the material.
     * @param mat Material to match
     * @param categories
     * @return
     */
    public static boolean isItemInOneCategory(Material mat, Collection<String> categories) {
        return categories.stream().anyMatch(c -> ItemCategoryChecker.isItemInCategory(mat, c));
    }

    /**
     * Returns true of the item matches the category.
     * @param mat
     * @param category
     * @return
     */
    public static boolean isItemInCategory(Material mat, String category) {
        category = category.toUpperCase();

        if (category.equals(CAT_ALL) || category.equals(CAT_ANY))
            return true;

        if (category.equals(CAT_WEAPONS) && isWeapon(mat))
            return true;

        if (category.equals(CAT_TOOLS) && isTool(mat))
            return true;

        if (category.equals(CAT_SHOVEL) && isShovel(mat))
            return true;

        if (category.equals(CAT_PICKAXE) && isPickaxe(mat))
            return true;

        if (category.equals(CAT_AXE) && isAxe(mat))
            return true;

        if (category.equals(CAT_SWORD) && isSword(mat))
            return true;

        if (category.equals(CAT_HOE) && isHoe(mat))
            return true;

        if (category.equals(CAT_ARMORS) && isArmor(mat))
            return true;

        if (category.equals(CAT_OBJECTS) && !isArmor(mat) && !isTool(mat) && !isWeapon(mat))
            return true;

        if (mat.toString().equals(category.toString()))
            return true;

        return false;
    }


    private static boolean isSword(Material m) {
        return m.toString().endsWith("_SWORD");
    }

    private static boolean isAxe(Material m) {
        return m.toString().endsWith("_AXE");
    }

    private static boolean isShovel(Material m) {
        return m.toString().endsWith("_SHOVEL");
    }

    private static boolean isHoe(Material m) {
        return m.toString().endsWith("_HOE");
    }

    private static boolean isPickaxe(Material m) {
        return m.toString().endsWith("_PICKAXE");
    }

    private static boolean isArmor(Material m) {
        final String s = m.toString();
        return s.endsWith("_HELMET") || s.endsWith("_CHESTPLATE")
            || s.endsWith("_LEGGINGS") || s.endsWith("_BOOTS");
    }

    private static boolean isTool(Material m) {
        return isAxe(m) || isShovel(m) || isHoe(m) || isPickaxe(m) || m == Material.FISHING_ROD;
    }

    private static boolean isWeapon(Material m) {
        return isSword(m) || m == Material.TRIDENT || m == Material.BOW || m == Material.CROSSBOW;
    }
}
