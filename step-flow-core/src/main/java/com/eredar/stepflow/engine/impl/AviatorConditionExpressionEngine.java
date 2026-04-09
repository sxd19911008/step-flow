package com.eredar.stepflow.engine.impl;

import com.eredar.stepflow.engine.ConditionExpressionEngine;
import com.eredar.stepflow.engine.aviator.AviatorInstanceBuilder;
import com.eredar.stepflow.engine.aviator.dto.StepFlowAviatorConfigProperties;
import com.eredar.stepflow.exception.StepFlowException;
import com.googlecode.aviator.AviatorEvaluatorInstance;

import java.util.Map;

/**
 * 基于 Aviator 框架的 业务表达式引擎
 */
public class AviatorConditionExpressionEngine implements ConditionExpressionEngine {

    private final AviatorEvaluatorInstance aviator;

    public AviatorConditionExpressionEngine(StepFlowAviatorConfigProperties config) {
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
     * 执行条件表达式
     *
     * @param expression 条件表达式
     * @param vars 表达式参数
     * @return 表达式结果: true / false
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
