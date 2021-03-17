package com.kraby.mcarcinizer.carcinizer.expressions.exp4j;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import com.kraby.mcarcinizer.carcinizer.expressions.ExpressionEvaluator;
import com.kraby.mcarcinizer.deathkeeper.DeathKeeperSubplugin;
import java.util.List;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Exp4jEvaluator implements ExpressionEvaluator {

    protected ExpressionBuilder builder;
    protected Map<String, Double> variables = new HashMap<>();

    public Exp4jEvaluator(String expression) {
        builder = new ExpressionBuilder(expression);
        builder.functions(ExtendedFunctions.getAllFunctions());
    }


    @Override
    public void setVariable(String name, Double value) {
        variables.put(name, value);
    }

    @Override
    public void setVariables(Map<String, Double> vars) {
        variables.putAll(vars);
    }

    @Override
    public boolean isValid() {
        return getExpression().validate().getErrors() == null;
    }

    @Override
    public List<String> getErrors() {
        return getExpression().validate().getErrors();
    }

    @Override
    public double evaluate() {
        return getExpression().evaluate();
    }

    @Override
    public String toString() {
        return builder.toString();
    }


    private Expression getExpression() {
        Logger logger = DeathKeeperSubplugin.getSingleton().getOwner().getLogger();
        return builder.variables(variables.keySet()).build().setVariables(variables);
    }

}
