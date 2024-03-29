package com.kraby.mcarcinizer.carcinizer.expressions.exp4j;

import java.util.ArrayList;
import java.util.List;
import net.objecthunter.exp4j.function.Function;

public class ExtendedFunctions {

    private ExtendedFunctions() {
    }

    /**
     * Get all extended functions.
     * @return
     */
    public static List<Function> getAllFunctions() {
        List<Function> fns = new ArrayList<>();
        fns.add(getMin());
        fns.add(getMax());
        fns.add(getPos());
        return fns;
    }

    /**
     * min(a,b), returns a if a < b, else b.
     * @return
     */
    public static Function getMin() {
        return new Function("min",2) {
            @Override
            public double apply(double... args) {
                return args[0] < args[1] ? args[0] : args[1];
            }
        };
    }

    /**
     * max(a,b), returns a if a > b, else b.
     * @return
     */
    public static Function getMax() {
        return new Function("max", 2) {
            @Override
            public double apply(double... args) {
                return args[0] > args[1] ? args[0] : args[1];
            }
        };
    }

    /**
     * pos(a), discards the negative sign of a if it has one.
     * @return
     */
    public static Function getPos() {
        return new Function("pos", 1) {
            @Override
            public double apply(double... args) {
                return args[0] >= 0 ? args[0] : -args[0];
            }
        };
    }
}
