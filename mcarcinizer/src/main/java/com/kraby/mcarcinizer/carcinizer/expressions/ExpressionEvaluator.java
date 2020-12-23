package com.kraby.mcarcinizer.carcinizer.expressions;

import java.util.List;
import java.util.Map;

public interface ExpressionEvaluator {

    public abstract void setVariable(String name, Double value);

    public abstract void setVariables(Map<String, Double> variables);

    public abstract boolean isValid();

    public abstract List<String> getErrors();

    public abstract double evaluate();
}
