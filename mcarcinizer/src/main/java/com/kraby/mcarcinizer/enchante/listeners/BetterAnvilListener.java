package com.kraby.mcarcinizer.enchante.listeners;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Locale.Category;

import com.kraby.mcarcinizer.carcinizer.expressions.ExpressionEvaluator;
import com.kraby.mcarcinizer.carcinizer.utils.ItemCategoryChecker;
import com.kraby.mcarcinizer.carcinizer.vanilla.ItemsDefaultAttributes;
import com.kraby.mcarcinizer.enchante.EnchanteSubplugin;
import com.kraby.mcarcinizer.enchante.betteranvil.CustomEnchantSeed;
import com.kraby.mcarcinizer.enchante.betteranvil.ItemPersistentEnchantData;
import com.kraby.mcarcinizer.enchante.config.BetterAnvilConfig;
import com.kraby.mcarcinizer.enchante.config.betteranvil.ItemAttributeEnchanterData;
import com.kraby.mcarcinizer.enchante.config.betteranvil.ItemRepairCostReducerData;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

public class BetterAnvilListener implements Listener {

    private static final String VARIABLE_BONUS = "BONUS";
    private static final String VARIABLE_CURRENT = "CURRENT";
    private static final String ATTRIBUTES_NAME = "enchante";


    @EventHandler
    public void onAnvilRepairReduceItem(PrepareAnvilEvent e) {
        AnvilInventory inv = e.getInventory();
        ItemStack consumable = inv.getItem(1);

        if (!(inv.getItem(0) != null && consumable != null && e.getResult() == null))
            return;

        String itemName = consumable.getType().toString();
        ItemRepairCostReducerData r = getConfig().getItemRepairCostReduceData(itemName);

        // Cancel if no data found for this object
        if (r == null)
            return;

        // Cancel if the amount isn't what's excepted
        if (r.count != consumable.getAmount())
            return;

        ItemStack stack = inv.getItem(0).clone();
        ItemMeta meta = stack.getItemMeta();

        if (meta instanceof Repairable) {
            Repairable repairable = (Repairable)meta;
            repairable.setRepairCost(repairable.getRepairCost() - r.reduction);
            inv.setRepairCost(r.cost);

            stack.setItemMeta(meta);
            e.setResult(stack);
        }
    }

    @EventHandler
    public void onAnvilAttributeEnchantItem(PrepareAnvilEvent e) {
        AnvilInventory inv = e.getInventory();

        if (inv.getItem(1) != null && inv.getItem(0) != null && e.getResult() == null) {
            String itemName = inv.getItem(1).getType().toString();

            List<ItemAttributeEnchanterData> attDataList =
                getConfig().getItemAttributeEnchantData(itemName);

            if (attDataList.isEmpty())
                return;

            ItemStack result = inv.getItem(0).clone();
            ItemMeta newMeta = result.getItemMeta();

            boolean appliedAttributes = false;
            for (ItemAttributeEnchanterData attData : attDataList) {
                boolean applied = applyAttributeEnchantement(e, attData, result, newMeta);
                appliedAttributes = appliedAttributes || applied;
            }

            result.setItemMeta(newMeta);

            if (appliedAttributes)
                e.setResult(result);
        }
    }



    private boolean applyAttributeEnchantement(
            PrepareAnvilEvent e,
            ItemAttributeEnchanterData attData,
            ItemStack result,
            ItemMeta newMeta) {
        final AnvilInventory inv = e.getInventory();

        // Enchantment applyable only if the attData is valid
        if (!attData.isValid())
            return false;

        // Enchantment applyable only if the object count is right
        if (inv.getItem(1).getAmount() != attData.count)
            return false;

        // If restricted to specific slot, check the item goes in that slot
        EquipmentSlot targetSlot = result.getType().getEquipmentSlot();
        if (attData.slot != null && attData.slot != targetSlot)
            return false;

        // Enchantment applyable only if the object cost is in the desired range
        ItemMeta ogMeta = inv.getItem(0).getItemMeta();
        if (ogMeta instanceof Repairable
                && (((Repairable)ogMeta).getRepairCost() < attData.minRepairCost
                    || ((Repairable)ogMeta).getRepairCost() > attData.maxRepairCost)) {
            return false;
        }

        // Enchantment applyable only if the object fits the required category
        if (!ItemCategoryChecker.isItemInOneCategory(result.getType(), attData.worksOn))
            return false;


        Random r = new Random();
        switch (attData.rerollBehaviour) {
            case ITEM_CHANGED:
                r.setSeed(CustomEnchantSeed.getItemChangedSeed(result));
                break;
            case ATTCHANT_COUNT_ITEM:
                r.setSeed(CustomEnchantSeed.getItemChangedSeed(result));
                break;
            // case ATTCHANT_COUNT_PLAYER:
            //     Player p = (Player)e.getViewers().get(0);
            //     r.setSeed(CustomEnchantSeed.getPlayerAttchantCountSeed(p));
            //     break;
            default:
                break;
        }

        double appearRandom = r.nextDouble();
        if (appearRandom > attData.probability)
            return false;

        double valueBonus = attData.value + r.nextGaussian() * attData.deviation;

        if (attData.attribute != null) {
            addOrMergeAttributeChange(result.getType(), newMeta, attData, valueBonus, targetSlot);
        } else if (attData.enchantment != null) {
            int intBonus = (int)Math.round(valueBonus);
            addOrMergeEnchant(attData, newMeta, intBonus);
        }

        int repairCost;
        if (!attData.forceCost && newMeta instanceof Repairable) {
            Repairable repairable = (Repairable)newMeta;
            repairCost = 0;

            repairable.setRepairCost(repairable.getRepairCost() + attData.repairRaise);
        } else {
            repairCost = attData.cost;
        }

        repairCost = Math.max(1, repairCost);   // 0 repair cost prevents anvil from working
        inv.setRepairCost(inv.getRepairCost() + repairCost);
        ItemPersistentEnchantData.incrementAttributeEnchantedCount(newMeta);

        return true;
    }


    private static void addOrMergeEnchant(
            ItemAttributeEnchanterData attData,
            ItemMeta meta,
            int bonus) {
        //
        Enchantment enchant = attData.enchantment;
        int newLevel = meta.hasEnchant(enchant) ? meta.getEnchantLevel(enchant) : 0;
        newLevel += bonus;
        meta.addEnchant(enchant, newLevel, true);
    }

    private static void addOrMergeAttributeChange(
            Material mat,
            ItemMeta meta,
            ItemAttributeEnchanterData attData,
            double bonus,
            EquipmentSlot slot) {
        // First try to merge change with existing ones
        boolean successfullyModifiedExistingAttribute = false;
        if (meta.hasAttributeModifiers()) {
            Collection<AttributeModifier> modifiers = meta.getAttributeModifiers(attData.attribute);

            if (modifiers != null && !modifiers.isEmpty()) {
                AttributeModifier oldModifier = modifiers.iterator().next();
                if (oldModifier.getOperation() == attData.operation) {
                    ExpressionEvaluator mergeFormula = attData.cumulationFormula;
                    mergeFormula.setVariable(VARIABLE_BONUS, bonus);
                    mergeFormula.setVariable(VARIABLE_CURRENT, oldModifier.getAmount());

                    AttributeModifier newModifier = new AttributeModifier(
                        oldModifier.getUniqueId(),
                        ATTRIBUTES_NAME,
                        mergeFormula.evaluate(),
                        attData.operation,
                        oldModifier.getSlot());

                    meta.removeAttributeModifier(attData.attribute, oldModifier);
                    meta.addAttributeModifier(attData.attribute, newModifier);

                    successfullyModifiedExistingAttribute = true;
                }
            }
        } else {
            ItemsDefaultAttributes vanillaAttData = ItemsDefaultAttributes.getSingleton();
            Map<Attribute, AttributeModifier> vanillaAtts = vanillaAttData.getItemAttributes(mat);
            for (Attribute att : vanillaAtts.keySet()) {
                meta.addAttributeModifier(att, vanillaAtts.get(att));
            }
        }

        // If nothing to merge with, create new
        if (!successfullyModifiedExistingAttribute) {
            meta.addAttributeModifier(
                attData.attribute,
                new AttributeModifier(
                    UUID.randomUUID(),
                    ATTRIBUTES_NAME,
                    bonus,
                    attData.operation,
                    slot));
        }
    }


    private static BetterAnvilConfig getConfig() {
        return EnchanteSubplugin.getSingleton().getBetterAnvilConfig();
    }
}
