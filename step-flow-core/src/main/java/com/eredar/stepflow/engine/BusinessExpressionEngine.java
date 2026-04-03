package com.eredar.stepflow.engine;

import java.util.Map;

/**
 * 业务表达式引擎
 */
public interface BusinessExpressionEngine {

    /**
     * 执行计算表达式
     *
     * @param expression 计算表达式
     * @param vars 表达式参数
     * @return 表达式结果
     */
    Object execute(String expression, Map<String, Object> vars);
}
