package com.kraby.mcarcinizer.carcinizer.vanilla;

import com.kraby.mcarcinizer.CarcinizerMain;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class ArmorDamageFormulas {

    private ArmorDamageFormulas() {
    }

    /**
     * Get the chance of an armor to have its durability used w/ a certain unbreaking level.
     * Chance between 0.0 and 1.0
     * @param unbreakingLevel The unbreaking enchantment level
     * @return
     */
    public static float getArmorUseDurabilityChance(final int unbreakingLevel) {
        return Math.min(
                1.0f,
                0.6f + (0.4f / (unbreakingLevel + 1))
                );
    }

    /**
     * Get how much durability an armor loses from taking a certain amount of damages.
     * @param damages
     * @return
     */
    public static double getArmorDurabilityDamaged(final double damages) {
        // Any hit from a damage source that can be blocked by armor removes one point of
        // durability from each piece of armor worn for every 4 (♥♥) of incoming damage
        // (rounded down, but never below 1).
        // Source: https://minecraft.fandom.com/wiki/Armor#Durability
        return Math.max(1.0, Math.floor(damages / 4.0));
    }

    /**
     * Returns true if the given armor will lose durability due to this damage cause.
     * @param armorPiece
     * @param cause
     * @return
     */
    public static boolean isDamageCauseDurabilityUser(
        final ItemStack armorPiece, final DamageCause cause) {

        switch (cause) {
            case BLOCK_EXPLOSION:
                return true;
            case CONTACT:
                return true;
            case ENTITY_ATTACK:
                return true;
            case ENTITY_EXPLOSION:
                return true;
            case ENTITY_SWEEP_ATTACK:
                return true;
            case HOT_FLOOR:
                return true;
            case LAVA:
                return armorPiece.getType() == Material.NETHERITE_BOOTS
                    || armorPiece.getType() == Material.NETHERITE_LEGGINGS
                    || armorPiece.getType() == Material.NETHERITE_CHESTPLATE
                    || armorPiece.getType() == Material.NETHERITE_HELMET;
            case PROJECTILE:
                return true;
            case FALLING_BLOCK:
                return true;
            case THORNS:
                return true;
            default:
                return false;
        }
    }


    /**
     * Simulate an armor losing durability. Takes into account damage type, enchants, etc.
     * @param armorPiece
     * @param damages
     * @param cause
     * @return The points of durability lost.
     */
    public static int simulateArmorDurabilityHit(
        final ItemStack armorPiece, final double damages, final DamageCause cause) {

        // If the armor doesn't take these damages, no durability used
        if (!isDamageCauseDurabilityUser(armorPiece, cause)) {
            return 0;
        }

        int unbreakingLevel = armorPiece.getEnchantmentLevel(Enchantment.DURABILITY);
        float useDurabilityChance = getArmorUseDurabilityChance(unbreakingLevel);
        if (Math.random() >= useDurabilityChance) {
            return 0;
        }

        return (int) Math.round(getArmorDurabilityDamaged(damages));
    }


    /**
     * Returns the armorstack damaged.
     * Must be ONLY armor.
     * @param armorContents
     * @param damages
     * @param cause
     * @return
     */
    public static ItemStack[] getDamagedArmorStack(
        final ItemStack[] armorContents, final double damages, final DamageCause cause) {

        ItemStack[] damagedStack = new ItemStack[armorContents.length];

        // Damage each armor piece
        for (int i = 0; i < armorContents.length; i++) {
            // This piece of armor isn't worn : skip
            if (armorContents[i] == null) {
                continue;
            }
            // Damage the piece of armor
            damagedStack[i] = armorContents[i].clone();
            if (damagedStack[i].getItemMeta() instanceof Damageable) {
                Damageable dmgTarget = (Damageable)damagedStack[i].getItemMeta();
                int durabilityHit = simulateArmorDurabilityHit(armorContents[i], damages, cause);
                dmgTarget.setDamage(dmgTarget.getDamage() + durabilityHit);
                damagedStack[i].setItemMeta((ItemMeta)dmgTarget);
            }
        }

        return damagedStack;
    }
}
