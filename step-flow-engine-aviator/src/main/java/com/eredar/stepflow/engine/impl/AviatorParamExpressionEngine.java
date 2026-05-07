package com.eredar.stepflow.engine.impl;

import com.eredar.stepflow.config.StepFlowEngineProperties;
import com.eredar.stepflow.engine.EngineCustomizer;
import com.eredar.stepflow.engine.ParamExpressionEngine;
import com.eredar.stepflow.engine.aviator.AviatorInstanceBuilder;
import com.googlecode.aviator.AviatorEvaluatorInstance;

import java.util.Map;

/**
 * 基于 AviatorScript 的参数取值引擎实现
 */
public class AviatorParamExpressionEngine implements ParamExpressionEngine {

    private final AviatorEvaluatorInstance aviator;

    public AviatorParamExpressionEngine(StepFlowEngineProperties config, EngineCustomizer customizer) {
        if (config == null) {
            config = new StepFlowEngineProperties();
        }
        // LRU 缓存大小为 4096（调用频率更高，缓存更大）
        if (config.getMaxExpressionCache() == null) {
            config.setMaxExpressionCache(4096);
        }
        this.aviator = AviatorInstanceBuilder.buildAviatorEvaluatorInstance(config);
        if (customizer != null) {
            customizer.customize(this.aviator);
        }
    }

    /**
     * 通过表达式从变量映射中取值（如 {@code dto.userId}、{@code context.result}）。
     *
     * @param expression 取值表达式
     * @param vars       变量上下文映射
     * @return 表达式求值结果
     */
    @Override
    public Object getParam(String expression, Map<String, Object> vars) {
        return aviator.execute(expression, vars);
    }
}
