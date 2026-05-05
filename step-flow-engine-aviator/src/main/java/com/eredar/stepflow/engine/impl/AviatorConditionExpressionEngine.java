package com.eredar.stepflow.engine.impl;

import com.eredar.stepflow.config.StepFlowEngineProperties;
import com.eredar.stepflow.engine.ConditionExpressionEngine;
import com.eredar.stepflow.engine.aviator.AviatorInstanceBuilder;
import com.eredar.stepflow.exception.StepFlowException;
import com.googlecode.aviator.AviatorEvaluatorInstance;

import java.util.Map;

/**
 * 基于 AviatorScript 的条件表达式引擎实现
 */
public class AviatorConditionExpressionEngine implements ConditionExpressionEngine {

    private final AviatorEvaluatorInstance aviator;

    public AviatorConditionExpressionEngine(StepFlowEngineProperties config) {
        if (config == null) {
            config = new StepFlowEngineProperties();
        }
        // LRU 缓存大小为 2048
        if (config.getMaxExpressionCache() == null) {
            config.setMaxExpressionCache(2048);
        }
        this.aviator = AviatorInstanceBuilder.buildAviatorEvaluatorInstance(config);
    }

    /**
     * 执行条件表达式，结果必须为布尔值。
     *
     * @param expression 条件表达式（如 {@code age >= 18 && score > 60}）
     * @param vars       表达式中引用的变量映射
     * @return 条件判断结果 {@code true} / {@code false}
     */
    @Override
    public Boolean isTrue(String expression, Map<String, Object> vars) {
        Object res = aviator.execute(expression, vars);
        if (res instanceof Boolean) {
            return (Boolean) res;
        } else if (res == null) {
            throw new StepFlowException(String.format("执行条件表达式[%s]，返回null", expression));
        } else {
            throw new StepFlowException(String.format("执行条件表达式[%s]，返回错误类型：%s", expression, res.getClass().getName()));
        }
    }
}
