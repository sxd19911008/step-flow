package com.eredar.stepflow.engine.impl;

import com.eredar.stepflow.config.StepFlowEngineProperties;
import com.eredar.stepflow.engine.BusinessExpressionEngine;
import com.eredar.stepflow.engine.EngineCustomizer;
import com.eredar.stepflow.engine.aviator.AviatorInstanceBuilder;
import com.googlecode.aviator.AviatorEvaluatorInstance;

import java.util.Map;

/**
 * 基于 AviatorScript 的业务表达式引擎实现
 */
public class AviatorBusinessExpressionEngine implements BusinessExpressionEngine {

    private final AviatorEvaluatorInstance aviator;

    public AviatorBusinessExpressionEngine(StepFlowEngineProperties config, EngineCustomizer customizer) {
        if (config == null) {
            config = new StepFlowEngineProperties();
        }
        // 默认 LRU 缓存大小为 2048
        if (config.getMaxExpressionCache() == null) {
            config.setMaxExpressionCache(2048);
        }
        this.aviator = AviatorInstanceBuilder.buildAviatorEvaluatorInstance(config);
        if (customizer != null) {
            customizer.customize(this.aviator);
        }
    }

    /**
     * 执行业务计算表达式，返回计算结果。
     *
     * @param expression 计算表达式（如 {@code a + b * rate}）
     * @param vars       表达式中引用的变量映射
     * @return 表达式计算结果
     */
    @Override
    public Object execute(String expression, Map<String, Object> vars) {
        return aviator.execute(expression, vars);
    }
}
