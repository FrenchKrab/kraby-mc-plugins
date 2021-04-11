package com.kraby.mcarcinizer.utils.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.kraby.mcarcinizer.carcinizer.expressions.ExpressionEvaluator;
import com.kraby.mcarcinizer.carcinizer.expressions.exp4j.Exp4jEvaluator;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigEvaluatorsChecker {

    public class Checker {
        private static final double DUMMY_VALUE = -1 / 3.0D;

        private final String expressionString;
        private final String tag;
        private final ExpressionEvaluator expressionEvaluator;

        Checker(String exprString, String tag) {
            this.expressionEvaluator = new Exp4jEvaluator(exprString);
            this.expressionString = exprString;
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }

        public String getExpressionString() {
            return expressionString;
        }

        /**
         * Add given variable as provided to the evaluator.
         * @param name
         * @return The same checker instance, updated.
         */
        public Checker addVariable(String name) {
            expressionEvaluator.setVariable(name, DUMMY_VALUE);
            return this;
        }

        /**
         * Add given variables as provided to the evaluator.
         * @param names
         * @return The same checker instance, updated.
         */
        public Checker addVariables(List<String> names) {
            Map<String, Double> map = new HashMap<>();
            for (String name : names) {
                map.put(name, DUMMY_VALUE);
            }
            expressionEvaluator.setVariables(map);
            return this;
        }

        public boolean isValid() {
            return expressionEvaluator.isValid();
        }

        public List<String> getErrors() {
            return expressionEvaluator.getErrors();
        }
    }

    private List<Checker> checkers = new ArrayList<>();

    /**
     * Registers an evaluator from an expression string.
     * @param exprString
     * @return The registered evaluator checker.
     */
    public Checker registerEvaluator(String exprString, String tag) {
        Checker e = new Checker(exprString, tag);
        checkers.add(e);
        return e;
    }

    /**
     * Registers an evaluator from a config file and a field path.
     * @param config
     * @param fieldName
     * @return The registered evaluator checker.
     */
    public Checker registerEvaluator(FileConfiguration config, String fieldName) {
        String expString = config.getString(fieldName, "!<" + fieldName + "> NOT SET!");
        return registerEvaluator(expString, fieldName);
    }

    /**
     * Returns true if all checks have passed.
     * @return
     */
    public boolean allCheckedPassed() {
        for (Checker c : checkers) {
            if (!c.isValid()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get all errors detected in this error checker.
     * @return
     */
    public List<String> getErrors() {
        List<String> errors = new ArrayList<>();
        for (Checker c : checkers) {
            if (c.isValid()) {
                continue;
            }

            for (String err : c.getErrors()) {
                String formattedError = "";
                if (!c.getTag().isBlank()) {
                    formattedError += String.format("(%s)", c.getTag());
                }
                formattedError += String.format("%s", err);

                errors.add(formattedError);
            }
        }
        return errors;
    }
}
