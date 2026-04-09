package com.eredar.stepflow.engine.impl;

import com.eredar.stepflow.engine.ParamExpressionEngine;
import com.eredar.stepflow.engine.aviator.AviatorInstanceBuilder;
import com.eredar.stepflow.engine.aviator.dto.StepFlowAviatorConfigProperties;
import com.googlecode.aviator.AviatorEvaluatorInstance;

import java.util.Map;

/**
 * 基于 Aviator 框架的 参数获取引擎
 */
public class AviatorParamExpressionEngine implements ParamExpressionEngine {

    private final AviatorEvaluatorInstance aviator;

    public AviatorParamExpressionEngine(StepFlowAviatorConfigProperties config) {
        if (config == null) {
            config = new StepFlowAviatorConfigProperties();
        }
        if (config.getUseLRUExpressionCache() == null) {
            config.setUseLRUExpressionCache(4096);
        }
        // 创建新的实例
        aviator = AviatorInstanceBuilder.buildAviatorEvaluatorInstance(config);
    }

    /**
     * 获取参数
     *
     * @param expression 用于获取参数的表达式
     * @param vars 表达式参数
     * @return 获取到的参数
     */
    @Override
    public Object getParam(String expression, Map<String, Object> vars) {
        return aviator.execute(expression, vars);
    }
}
