package com.eredar.stepflow.engine.impl;

import com.eredar.stepflow.engine.ParamExpressionEngine;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Options;

import java.util.Map;

/**
 * 基于 Aviator 框架的 参数获取引擎
 */
public class AviatorParamExpressionEngine implements ParamExpressionEngine {

    private final AviatorEvaluatorInstance aviator;

    public AviatorParamExpressionEngine() {
        // 创建新的实例
        aviator = AviatorEvaluator.newInstance();

        // 编译模式，表达式被直接翻译成 Java 字节码
        aviator.setOption(Options.OPTIMIZE_LEVEL, AviatorEvaluator.COMPILE);

        // 开启 LRU 缓存策略
        aviator.setCachedExpressionByDefault(true);

        // 设置最大缓存表达式数量
        aviator.useLRUExpressionCache(4096);

        // 语法糖配置：允许 policyInfo.applyDate 这种写法
        aviator.setOption(Options.ENABLE_PROPERTY_SYNTAX_SUGAR, true);

        // 容错配置：true：当访问对象属性为 null 时不抛异常，返回 null；false：直接报错。
        aviator.setOption(Options.NIL_WHEN_PROPERTY_NOT_FOUND, true);
    }

    /**
     * 获取参数
     *
     * @param expression 用于获取参数的表达式
     * @param vars 表达式参数
     * @return 获取到的参数
     */
    @Override
    public Object getParam(String expression, Map<String, Object> vars) {
        return aviator.execute(expression, vars);
    }
}
