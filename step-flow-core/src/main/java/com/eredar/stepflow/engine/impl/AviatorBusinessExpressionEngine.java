package com.eredar.stepflow.engine.impl;

import com.eredar.stepflow.engine.BusinessExpressionEngine;
import com.eredar.stepflow.engine.aviator.AviatorInstanceBuilder;
import com.eredar.stepflow.engine.aviator.dto.StepFlowAviatorConfigProperties;
import com.googlecode.aviator.AviatorEvaluatorInstance;

import java.util.Map;

/**
 * 基于 Aviator 框架的 业务表达式引擎
 */
public class AviatorBusinessExpressionEngine implements BusinessExpressionEngine {

    private final AviatorEvaluatorInstance aviator;

    public AviatorBusinessExpressionEngine(StepFlowAviatorConfigProperties config) {
        if (config == null) {
            config = new StepFlowAviatorConfigProperties();
        }
        if (config.getUseLRUExpressionCache() == null) {
            config.setUseLRUExpressionCache(2048);
        }
        // 创建新的实例
        aviator = AviatorInstanceBuilder.buildAviatorEvaluatorInstance(config);
    }

    /**
     * 执行计算表达式
     *
     * @param expression 计算表达式
     * @param vars 表达式参数
     * @return 表达式结果
     */
    @Override
    public Object execute(String expression, Map<String, Object> vars) {
        return aviator.execute(expression, vars);
    }
}
