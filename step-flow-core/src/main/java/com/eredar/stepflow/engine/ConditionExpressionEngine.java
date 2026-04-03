package com.eredar.stepflow.engine;

import java.util.Map;

/**
 * 条件表达式引擎
 */
public interface ConditionExpressionEngine {

    /**
     * 执行条件表达式
     *
     * @param expression 条件表达式
     * @param vars 表达式参数
     * @return 表达式结果: true / false
     */
    Boolean isTrue(String expression, Map<String, Object> vars);
}
