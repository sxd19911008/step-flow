package io.github.kentasun.stepflow.engine.impl;

import io.github.kentasun.stepflow.config.StepFlowEngineProperties;
import io.github.kentasun.stepflow.engine.ExpressionEngine;
import io.github.kentasun.stepflow.engine.EngineCustomizer;
import io.github.kentasun.stepflow.engine.aviator.AviatorInstanceBuilder;
import com.googlecode.aviator.AviatorEvaluatorInstance;

import java.util.Map;

/**
 * 基于 AviatorScript 的表达式引擎实现
 */
public class AviatorExpressionEngine implements ExpressionEngine {

    private final AviatorEvaluatorInstance aviator;

    public AviatorExpressionEngine(StepFlowEngineProperties config, EngineCustomizer customizer) {
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
     * 执行表达式，返回计算结果。
     *
     * @param expression 表达式
     * @param vars       表达式中引用的变量映射
     * @return 表达式计算结果
     */
    @Override
    public Object execute(String expression, Map<String, Object> vars) {
        return aviator.execute(expression, vars);
    }
}
