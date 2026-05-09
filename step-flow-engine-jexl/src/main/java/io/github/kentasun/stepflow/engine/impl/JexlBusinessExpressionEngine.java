package io.github.kentasun.stepflow.engine.impl;

import io.github.kentasun.stepflow.config.StepFlowEngineProperties;
import io.github.kentasun.stepflow.engine.BusinessExpressionEngine;
import io.github.kentasun.stepflow.engine.EngineCustomizer;
import io.github.kentasun.stepflow.engine.jexl.JexlInstanceBuilder;
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

    public JexlBusinessExpressionEngine(StepFlowEngineProperties config, EngineCustomizer customizer) {
        if (config == null) {
            config = new StepFlowEngineProperties();
        }
        if (config.getMaxExpressionCache() == null) {
            config.setMaxExpressionCache(2048);
        }
        this.jexl = JexlInstanceBuilder.buildJexlEngine(config, customizer);
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
