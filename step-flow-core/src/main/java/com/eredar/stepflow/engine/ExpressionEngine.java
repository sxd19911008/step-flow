package com.eredar.stepflow.engine;

import java.util.Map;

public interface ExpressionEngine {

    /**
     * 执行计算表达式
     *
     * @param expression 计算表达式
     * @param vars 表达式参数
     * @return 表达式结果
     */
    Object execute(String expression, Map<String, Object> vars);

    /**
     * 获取参数
     *
     * @param expression 用于获取参数的表达式
     * @param vars 表达式参数
     * @return 获取到的参数
     */
    Object getParam(String expression, Map<String, Object> vars);
}
