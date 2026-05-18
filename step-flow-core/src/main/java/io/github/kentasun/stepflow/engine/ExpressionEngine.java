package io.github.kentasun.stepflow.engine;

import java.util.Map;

/**
 * 表达式引擎
 */
public interface ExpressionEngine {

    /**
     * 执行表达式
     *
     * @param expression 表达式
     * @param vars 表达式参数
     * @return 表达式结果
     */
    Object execute(String expression, Map<String, Object> vars);
}
