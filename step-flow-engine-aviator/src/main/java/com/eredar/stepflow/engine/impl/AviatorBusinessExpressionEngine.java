package com.eredar.stepflow.engine.impl;

import com.eredar.stepflow.engine.BusinessExpressionEngine;
import com.eredar.stepflow.engine.aviator.AviatorInstanceBuilder;
import com.eredar.stepflow.engine.aviator.dto.StepFlowAviatorConfigProperties;
import com.googlecode.aviator.AviatorEvaluatorInstance;

import java.util.Map;

/**
 * 基于 AviatorScript 的业务表达式引擎实现。
 *
 * <p>无参构造器供 Java SPI（ServiceLoader）在非 Spring 环境下自动实例化使用，
 * 此时使用内置默认配置（LRU缓存 2048）。
 * 带参构造器供 Spring Boot AutoConfiguration 注入定制配置使用。
 */
public class AviatorBusinessExpressionEngine implements BusinessExpressionEngine {

    private final AviatorEvaluatorInstance aviator;

    /**
     * SPI 专用无参构造器，使用默认配置（LRU 缓存 2048 条）。
     */
    public AviatorBusinessExpressionEngine() {
        this(null);
    }

    /**
     * 带配置构造器，供 Spring Boot Starter 注入定制参数时使用。
     *
     * @param config Aviator 配置项，传 null 时退回默认配置
     */
    public AviatorBusinessExpressionEngine(StepFlowAviatorConfigProperties config) {
        if (config == null) {
            config = new StepFlowAviatorConfigProperties();
        }
        if (config.getMaxExpressionCache() == null) {
            config.setMaxExpressionCache(2048);
        }
        this.aviator = AviatorInstanceBuilder.buildAviatorEvaluatorInstance(config);
    }

    /**
     * 执行业务计算表达式，返回计算结果。
     *
     * @param expression 计算表达式（如 "a + b * rate"）
     * @param vars       表达式中引用的变量
     * @return 表达式计算结果
     */
    @Override
    public Object execute(String expression, Map<String, Object> vars) {
        return aviator.execute(expression, vars);
    }
}
