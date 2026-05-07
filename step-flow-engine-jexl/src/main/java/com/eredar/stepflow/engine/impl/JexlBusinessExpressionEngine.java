package com.eredar.stepflow.engine.impl;

import com.eredar.stepflow.config.StepFlowEngineProperties;
import com.eredar.stepflow.engine.BusinessExpressionEngine;
import com.eredar.stepflow.engine.jexl.JexlInstanceBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

import java.util.Collections;
import java.util.Map;

/**
 * 基于 JEXL 的业务计算引擎：执行步骤中配置的数学或逻辑表达式并返回原始 Java 对象结果。
 */
public class JexlBusinessExpressionEngine implements BusinessExpressionEngine {

    private final JexlEngine jexl;

    /**
     * @param config 引擎配置；未显式设置 maxExpressionCache 时默认 2048
     */
    public JexlBusinessExpressionEngine(StepFlowEngineProperties config) {
        if (config == null) {
            config = new StepFlowEngineProperties();
        }
        if (config.getMaxExpressionCache() == null) {
            config.setMaxExpressionCache(2048);
        }
        this.jexl = JexlInstanceBuilder.buildJexlEngine(config);
    }

    /**
     * 在给定变量上映射求值业务表达式。
     */
    @Override
    public Object execute(String expression, Map<String, Object> vars) {
        Map<String, Object> ctx = vars != null ? vars : Collections.<String, Object>emptyMap();
        JexlExpression script = this.jexl.createExpression(expression);
        return script.evaluate(new MapContext(ctx));
    }
}
