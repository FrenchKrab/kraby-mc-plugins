package org.kraby.mcarcinizer.deathkeeper.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.kraby.mcarcinizer.deathkeeper.DeathKeeperSubplugin;
import org.kraby.mcarcinizer.deathkeeper.data.DeathKeeperData;
import org.kraby.mcarcinizer.utils.InventorySerializer;
import org.kraby.mcarcinizer.utils.exp4j.ExtendedFunctions;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class DkConfiguration {
    private static final String CFG_PLUGIN_ENABLED = "plugin_enabled";
    private static final String CFG_DEATHKEEPER_LVL_EXPR = "deathkeeper.lvl_expr";
    private static final String CFG_DEATHKEEPER_ATTACK_EXPR = "deathkeeper.attack_expr";
    private static final String CFG_DEATHKEEPER_HP_EXPR = "deathkeeper.hp_expr";
    private static final String CFG_DEATHKEEPER_SPEED_EXPR = "deathkeeper.speed_expr";
    private static final String CFG_DEATHKEEPER_ETERNAL_LVL = "deathkeeper.eternalkeeper_lvl";
    private static final String CFG_DEATHKEEPER_NAME = "deathkeeper.name";
    private static final String CFG_DEATHKEEPER_DEATH_MSG = "deathkeeper.death_message";

    private final FileConfiguration config;

    public DkConfiguration(final FileConfiguration config) {
        this.config = config;
    }


    public boolean isPluginEnabled() {
        return config.getBoolean(CFG_PLUGIN_ENABLED, false);
    }

    /**
     * Get the player's deathkeeper's level.
     * @param p
     * @return
     */
    public double getDeathKeeperLevel(final Player p) {
        Map<String, Double> variables = new HashMap<>();

        // put all stats
        for (Statistic s : Statistic.values()) {
            try {
                variables.put(s.name(), Double.valueOf(p.getStatistic(s)));
            } catch (Exception e) {
                // Statistic that requires arguments (mob type, material, etc)
            }
        }
        // put xp
        variables.put("XP", Double.valueOf(p.getTotalExperience()));
        // put invsize
        String serializedInventory = InventorySerializer
            .itemStackArrayToBase64(p.getInventory().getContents());
        variables.put("INVSIZE", Double.valueOf(serializedInventory.length()));

        //build expr
        String exprString = config.getString(CFG_DEATHKEEPER_LVL_EXPR, "0");
        Expression lvlExpr = new ExpressionBuilder(exprString)
            .functions(ExtendedFunctions.getAllFunctions())
            .variables(variables.keySet())
            .build();
        lvlExpr.setVariables(variables);

        if (!lvlExpr.validate().isValid()) {
            logExprErrors(lvlExpr);
            return 0.0f;
        }

        //evaluate
        return lvlExpr.evaluate();
    }

    public double getDeathKeeperAttack(double level) {
        return getLevelDependantExpr(CFG_DEATHKEEPER_ATTACK_EXPR, level);
    }

    public double getDeathKeeperHp(double level) {
        return getLevelDependantExpr(CFG_DEATHKEEPER_HP_EXPR, level);
    }

    public double getDeathKeeperSpeed(double level) {
        return getLevelDependantExpr(CFG_DEATHKEEPER_SPEED_EXPR, level);
    }

    /**
     * Get the death message customized for the given deathkeeper.
     * @param deathKeeper
     * @return
     */
    public String getDeathKeeperDeathMessage(Entity deathKeeper) {
        String deathMessage = config.getString(CFG_DEATHKEEPER_DEATH_MSG, "");

        String owner = DeathKeeperData.getOwner(deathKeeper);
        long level = DeathKeeperData.getLevel(deathKeeper);
        long spawnMillis = DeathKeeperData.getSpawnTime(deathKeeper);

        Date resultdate = new Date(spawnMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat hoursFormat = new SimpleDateFormat("HH'h'mm");
        String dateString = dateFormat.format(resultdate);
        String hoursString = hoursFormat.format(resultdate);

        return deathMessage.replace("{DK_OWNER}", owner)
            .replace("{DK_LEVEL}", Long.toString(level))
            .replace("{DK_DATE}", dateString)
            .replace("{DK_HOUR}", hoursString);
    }

    /**
     * Get the DeathKeeper's customized name
     * (uses values stored as tags, make sure they are set beforehand).
     * @param deathKeeper
     * @return
     */
    public String getDeathKeeperName(Entity deathKeeper) {
        String name = config.getString(CFG_DEATHKEEPER_NAME, "???'s DeathKeeper");
        String owner = DeathKeeperData.getOwner(deathKeeper);
        long level = DeathKeeperData.getLevel(deathKeeper);

        return name.replace("{DK_OWNER}", owner).replace("{DK_LEVEL}", Long.toString(level));
    }

    public int getEternalKeeperLevel() {
        return config.getInt(CFG_DEATHKEEPER_ETERNAL_LVL, Integer.MAX_VALUE);
    }

    private double getLevelDependantExpr(String var, double level) {
        //build expr
        String exprString = config.getString(var, "0");
        Expression lvlExpr = new ExpressionBuilder(exprString)
            .functions(ExtendedFunctions.getAllFunctions())
            .variable("LVL")
            .build();
        lvlExpr.setVariable("LVL", level);

        if (!lvlExpr.validate().isValid()) {
            logExprErrors(lvlExpr);
            return 0.0f;
        }

        //evaluate
        return lvlExpr.evaluate();
    }

    private void logExprErrors(Expression expr) {
        Logger logger = DeathKeeperSubplugin.getSingleton().getOwner().getLogger();
        logger.info(String.format("Invalid expr:%n%s%nproblems:", expr.toString()));
        for (String err : expr.validate().getErrors()) {
            logger.info(String.format("%n- %s", err));
        }
    }
}
