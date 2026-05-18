package io.github.kentasun.stepflow.engine.impl;

import io.github.kentasun.aviatororacle.AviatorOracleBuilder;
import io.github.kentasun.stepflow.config.StepFlowEngineProperties;
import io.github.kentasun.stepflow.engine.ExpressionEngine;
import io.github.kentasun.stepflow.engine.EngineCustomizer;
import com.googlecode.aviator.AviatorEvaluatorInstance;

import java.util.Map;

/**
 * 基于 AviatorScript 的表达式引擎实现
 */
public class AviatorOracleExpressionEngine implements ExpressionEngine {

    private final AviatorEvaluatorInstance aviator;

    public AviatorOracleExpressionEngine(StepFlowEngineProperties config, EngineCustomizer customizer) {
        if (config == null) {
            config = new StepFlowEngineProperties();
        }
        // 默认 LRU 缓存大小为 2048
        if (config.getMaxExpressionCache() == null) {
            config.setMaxExpressionCache(2048);
        }
        this.aviator = AviatorOracleBuilder.builder()
                .useLRUExpressionCache(config.getMaxExpressionCache())
                .maxLoopCount(config.getMaxLoopCount())
                .traceEval(config.getLogEnabled())
                .build();
        if (customizer != null) {
            customizer.customize(this.aviator);
        }
    }

    @Override
    public Object execute(String expression, Map<String, Object> vars) {
        return aviator.execute(expression, vars);
    }
}
