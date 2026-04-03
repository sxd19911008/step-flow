package com.eredar.stepflow.engine;

import java.util.Map;

/**
 * 参数获取引擎
 */
public interface ParamExpressionEngine {

    /**
     * 获取参数
     *
     * @param expression 用于获取参数的表达式
     * @param vars 表达式参数
     * @return 获取到的参数
     */
    Object getParam(String expression, Map<String, Object> vars);
}
