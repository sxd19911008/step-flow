package com.eredar.stepflow.engine.impl;

import com.eredar.stepflow.config.StepFlowEngineProperties;
import com.eredar.stepflow.engine.ParamExpressionEngine;
import com.eredar.stepflow.engine.jexl.JexlInstanceBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

import java.util.Collections;
import java.util.Map;

/**
 * 基于 JEXL 的参数取值引擎：从上下文中解析诸如 {@code dto.num1} 这类表达式并返回值。
 */
public class JexlParamExpressionEngine implements ParamExpressionEngine {

    /** 线程安全的 JexlEngine，可并发执行 {@link JexlExpression#evaluate} */
    private final JexlEngine jexl;

    /**
     * @param config 引擎配置；未显式设置 maxExpressionCache 时默认 4096（与 Aviator 插件行为一致）
     */
    public JexlParamExpressionEngine(StepFlowEngineProperties config) {
        if (config == null) {
            config = new StepFlowEngineProperties();
        }
        if (config.getMaxExpressionCache() == null) {
            config.setMaxExpressionCache(4096);
        }
        this.jexl = JexlInstanceBuilder.buildJexlEngine(config);
    }

    /**
     * 在给定变量映射上求值表达式。
     *
     * @param expression 取值表达式（支持属性链、算术等 JEXL 表达式语法）
     * @param vars       根作用域变量（通常即流程 contextMap）
     * @return 求值结果，可能为 null（例如属性存在且值为 null）
     */
    @Override
    public Object getParam(String expression, Map<String, Object> vars) {
        Map<String, Object> ctx = vars != null ? vars : Collections.<String, Object>emptyMap();
        JexlExpression script = this.jexl.createExpression(expression);
        return script.evaluate(new MapContext(ctx));
    }
}
