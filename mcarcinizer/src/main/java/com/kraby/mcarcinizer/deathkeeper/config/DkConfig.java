package com.kraby.mcarcinizer.deathkeeper.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import com.kraby.mcarcinizer.carcinizer.expressions.ExpressionEvaluator;
import com.kraby.mcarcinizer.carcinizer.expressions.exp4j.Exp4jEvaluator;
import com.kraby.mcarcinizer.carcinizer.expressions.variables.PlayerVariables;
import com.kraby.mcarcinizer.deathkeeper.DeathKeeperSubplugin;
import com.kraby.mcarcinizer.deathkeeper.data.DeathKeeperData;
import com.kraby.mcarcinizer.utils.config.ConfigAccessor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DkConfig extends ConfigAccessor {
    private static final String CFG_PLUGIN_ENABLED = "plugin_enabled";
    private static final String CFG_DEATHKEEPER_LVL_EXPR = "deathkeeper.lvl_expr";
    private static final String CFG_DEATHKEEPER_ATTACK_EXPR = "deathkeeper.attack_expr";
    private static final String CFG_DEATHKEEPER_HP_EXPR = "deathkeeper.hp_expr";
    private static final String CFG_DEATHKEEPER_SPEED_EXPR = "deathkeeper.speed_expr";
    private static final String CFG_DEATHKEEPER_ETERNAL_LVL = "deathkeeper.eternalkeeper_lvl";
    private static final String CFG_DEATHKEEPER_NAME = "deathkeeper.name";
    private static final String CFG_DEATHKEEPER_DEATH_MSG = "deathkeeper.death_message";
    private static final String CFG_BAN_ON_DEATH = "deathkeeper.ban_on_death";
    private static final String CFG_DEATH_BAN_EXPR = "deathkeeper.death_ban_expr";

    public DkConfig(final FileConfiguration config) {
        super(config);
    }


    public DkConfig(Plugin plugin, String configFileName) {
        super(plugin, configFileName);
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
        String exprString = config.getString(CFG_DEATHKEEPER_LVL_EXPR, "0");
        ExpressionEvaluator evaluator = new Exp4jEvaluator(exprString);
        evaluator.setVariables(new PlayerVariables(p));

        if (!evaluator.isValid()) {
            logExprErrors(evaluator);
            return 0.0;
        }

        return evaluator.evaluate();
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

    public boolean isBanOnDeathEnabled() {
        return config.getBoolean(CFG_BAN_ON_DEATH, false);
    }

    /**
     * Get ban time on death (in seconds).
     * @param p
     * @param deathKeeperLevel
     * @return
     */
    public double getDeathBanTime(Player p, double deathKeeperLevel) {
        String exprString = config.getString(CFG_DEATH_BAN_EXPR, "0");
        ExpressionEvaluator evaluator = new Exp4jEvaluator(exprString);
        evaluator.setVariables(new PlayerVariables(p));
        evaluator.setVariable("DK_LVL", deathKeeperLevel);

        if (!evaluator.isValid()) {
            logExprErrors(evaluator);
            return 0.0;
        }

        return evaluator.evaluate();
    }

    /**
     * Evaluate an expression. Only available var is LVL.
     * @param var
     * @param level
     * @return
     */
    private double getLevelDependantExpr(String var, double level) {
        String exprString = config.getString(var, "0");
        ExpressionEvaluator evaluator = new Exp4jEvaluator(exprString);
        evaluator.setVariable("LVL", level);

        if (!evaluator.isValid()) {
            logExprErrors(evaluator);
            return 0.0;
        }

        return evaluator.evaluate();
    }


    private void logExprErrors(ExpressionEvaluator expr) {
        Logger logger = DeathKeeperSubplugin.getSingleton().getOwner().getLogger();
        logger.warning(String.format("Invalid expr:%n%s%nproblems:", expr.toString()));
        for (String err : expr.getErrors()) {
            logger.info(String.format("%n- %s", err));
        }
    }
}
