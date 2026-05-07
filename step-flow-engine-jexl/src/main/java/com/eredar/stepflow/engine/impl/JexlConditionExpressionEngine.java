package com.eredar.stepflow.engine.impl;

import com.eredar.stepflow.config.StepFlowEngineProperties;
import com.eredar.stepflow.engine.ConditionExpressionEngine;
import com.eredar.stepflow.engine.EngineCustomizer;
import com.eredar.stepflow.engine.jexl.JexlInstanceBuilder;
import com.eredar.stepflow.exception.StepFlowException;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.MapContext;

import java.util.Collections;
import java.util.Map;

/**
 * 基于 JEXL 的条件表达式引擎：要求表达式最终结果必须为 {@link Boolean}，与 Aviator 插件语义一致。
 */
public class JexlConditionExpressionEngine implements ConditionExpressionEngine {

    private final JexlEngine jexl;

    public JexlConditionExpressionEngine(StepFlowEngineProperties config, EngineCustomizer customizer) {
        if (config == null) {
            config = new StepFlowEngineProperties();
        }
        if (config.getMaxExpressionCache() == null) {
            config.setMaxExpressionCache(2048);
        }
        this.jexl = JexlInstanceBuilder.buildJexlEngine(config, customizer);
    }

    /**
     * 执行条件表达式；返回值必须是布尔类型，否则抛出 {@link StepFlowException}。
     */
    @Override
    public Boolean isTrue(String expression, Map<String, Object> vars) {
        Map<String, Object> ctx = vars != null ? vars : Collections.<String, Object>emptyMap();
        Object res = this.jexl.createExpression(expression).evaluate(new MapContext(ctx));
        if (res instanceof Boolean) {
            return (Boolean) res;
        }
        if (res == null) {
            throw new StepFlowException(String.format("执行条件表达式[%s]，返回null", expression));
        }
        throw new StepFlowException(String.format("执行条件表达式[%s]，返回错误类型：%s", expression, res.getClass().getName()));
    }
}
