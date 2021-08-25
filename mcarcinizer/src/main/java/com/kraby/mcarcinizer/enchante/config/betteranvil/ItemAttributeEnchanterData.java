package com.kraby.mcarcinizer.enchante.config.betteranvil;

import com.kraby.mcarcinizer.CarcinizerMain;
import com.kraby.mcarcinizer.carcinizer.expressions.ExpressionEvaluator;
import com.kraby.mcarcinizer.carcinizer.expressions.exp4j.Exp4jEvaluator;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.EquipmentSlot;

public class ItemAttributeEnchanterData {
    private static final String CFG_ATTCHANT_TYPE = "type";
    private static final String CFG_ATTCHANT_SLOT = "slot";
    private static final String CFG_ATTCHANT_ATTRIBUTE = "attribute";
    private static final String CFG_ATTCHANT_VALUE = "value";
    private static final String CFG_ATTCHANT_COST = "cost";
    private static final String CFG_ATTCHANT_FORCECOST = "force_cost";
    private static final String CFG_ATTCHANT_REPAIRRAISE = "repair_raise";
    private static final String CFG_ATTCHANT_COUNT = "count";
    private static final String CFG_ATTCHANT_DEVIATION = "deviation";
    private static final String CFG_ATTCHANT_CUMULATION = "cumulation";
    private static final String CFG_ATTCHANT_PROBABILTY = "probability";
    private static final String CFG_ATTCHANT_REROLL = "reroll";
    private static final String CFG_ATTCHANT_MIN_REPAIR = "min_repair_cost";
    private static final String CFG_ATTCHANT_MAX_REPAIR = "max_repair_cost";


    public final Operation operation;
    public final Attribute attribute;
    public final double value;
    public final int cost;
    public final boolean forceCost;
    public final int repairRaise;
    public final int count;
    public final double deviation;
    public final ExpressionEvaluator cumulationFormula;
    public final double probability;
    public final AttributeRerollBehaviour rerollBehaviour;
    public final int minRepairCost;
    public final int maxRepairCost;
    public final EquipmentSlot slot;

    private final ConfigurationSection section;

    /**
     * Initialize an {@link ItemAttributeEnchanterData}.
     * Stores config data about how an item enchants with attributes.
     * @param section
     */
    public ItemAttributeEnchanterData(ConfigurationSection section) {
        this.section = section;

        this.operation = readOperation();
        this.attribute = readAttribute();
        this.value = section.getDouble(CFG_ATTCHANT_VALUE, 0);
        this.cost = section.getInt(CFG_ATTCHANT_COST, 0);
        this.repairRaise = section.getInt(CFG_ATTCHANT_REPAIRRAISE, 1);
        this.count = section.getInt(CFG_ATTCHANT_COUNT, 1);
        this.forceCost = section.getBoolean(CFG_ATTCHANT_FORCECOST, false);
        this.deviation = section.getDouble(CFG_ATTCHANT_DEVIATION, 0);
        this.cumulationFormula = readCumulationFormula();
        this.probability = section.getDouble(CFG_ATTCHANT_PROBABILTY, 1.0);
        this.rerollBehaviour = readReroll();
        this.minRepairCost = section.getInt(CFG_ATTCHANT_MIN_REPAIR, 0);
        this.maxRepairCost = section.getInt(CFG_ATTCHANT_MAX_REPAIR, Integer.MAX_VALUE);
        this.slot = readSlot();
    }

    public boolean isValid() {
        return operation != null && attribute != null && count > 0;
    }


    private Operation readOperation() {
        String opString = section.getString(CFG_ATTCHANT_TYPE, "?").toLowerCase();
        Operation op = null;
        if (opString.startsWith("ad")) {
            op = Operation.ADD_NUMBER;
        } else if (opString.startsWith("mul")) {
            op = Operation.MULTIPLY_SCALAR_1;
        }
        return op;
    }

    private EquipmentSlot readSlot() {
        String slotString = section.getString(CFG_ATTCHANT_SLOT, "?");
        EquipmentSlot slot;
        try {
            slot = EquipmentSlot.valueOf(slotString);
        } catch (Exception e) {
            slot = null;
        }
        return slot;
    }

    private Attribute readAttribute() {
        String attributeString = section.getString(CFG_ATTCHANT_ATTRIBUTE, "?");
        Attribute attribute;
        try {
            attribute = Attribute.valueOf(attributeString);
        } catch (Exception e) {
            attribute = null;
        }
        return attribute;
    }

    private ExpressionEvaluator readCumulationFormula() {
        String exprString = section.getString(CFG_ATTCHANT_CUMULATION, "0.404");
        return new Exp4jEvaluator(exprString);
    }

    private AttributeRerollBehaviour readReroll() {
        String str = section.getString(CFG_ATTCHANT_REROLL, "?");
        try {
            return AttributeRerollBehaviour.valueOf(str);
        } catch (Exception e) {
            CarcinizerMain.getSingleton().getServer().broadcastMessage("oups with " + str);
            return AttributeRerollBehaviour.ATTCHANT_COUNT_ITEM;
        }
    }

    @Override
    public String toString() {
        return String.format("op=%s, att=%s, value=%f, cost=%d, forceCost=%b, repairRaise=%d, "
            + "count=%d, deviation=%f, cumulation=%s, prob=%f",
            operation, attribute, value, cost, forceCost, repairRaise, count, deviation,
            cumulationFormula.toString(), probability);
    }
}