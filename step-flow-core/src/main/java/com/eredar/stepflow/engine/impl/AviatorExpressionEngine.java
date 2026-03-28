package com.eredar.stepflow.engine.impl;

import com.eredar.stepflow.engine.ExpressionEngine;
import com.eredar.stepflow.engine.aviator.AviatorEngine;

import java.util.Map;

/**
 * 表达式引擎
 */
public class AviatorExpressionEngine implements ExpressionEngine {

    @Override
    public Object execute(String expression, Map<String, Object> vars) {
        return AviatorEngine.execute(expression, vars);
    }

    @Override
    public Object getParam(String expression, Map<String, Object> vars) {
        return AviatorEngine.getParam(expression, vars);
    }
}
