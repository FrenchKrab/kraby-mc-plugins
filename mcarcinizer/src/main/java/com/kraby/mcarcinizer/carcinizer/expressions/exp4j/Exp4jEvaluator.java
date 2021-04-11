package com.kraby.mcarcinizer.carcinizer.expressions.exp4j;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import com.kraby.mcarcinizer.carcinizer.expressions.ExpressionEvaluator;
import com.kraby.mcarcinizer.deathkeeper.DeathKeeperSubplugin;
import java.util.List;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;
import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;

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
        builder.variable(name);
    }

    @Override
    public void setVariables(Map<String, Double> vars) {
        variables.putAll(vars);
        builder.variables(variables.keySet());
    }

    @Override
    public boolean isValid() {
        try {
            Expression e = builder.build();
            ValidationResult res = e.validate(false);
            return res.isValid();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<String> getErrors() {
        try {
            Expression e = builder.build();
            ValidationResult res = e.validate();
            return res.getErrors();
        } catch (UnknownFunctionOrVariableException e) {
            return List.of(e.getMessage());
        }
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
        return builder.build().setVariables(variables);
    }

}
