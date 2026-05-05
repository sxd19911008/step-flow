package com.eredar.stepflow.engine.impl;

import com.eredar.stepflow.engine.ParamExpressionEngine;
import com.eredar.stepflow.engine.aviator.AviatorInstanceBuilder;
import com.eredar.stepflow.engine.aviator.dto.StepFlowAviatorConfigProperties;
import com.googlecode.aviator.AviatorEvaluatorInstance;

import java.util.Map;

/**
 * 基于 AviatorScript 的参数取值引擎实现。
 *
 * <p>无参构造器供 Java SPI（ServiceLoader）在非 Spring 环境下自动实例化使用，
 * 此时使用内置默认配置（LRU 缓存 4096，参数取值场景调用频率更高故缓存更大）。
 * 带参构造器供 Spring Boot AutoConfiguration 注入定制配置使用。
 */
public class AviatorParamExpressionEngine implements ParamExpressionEngine {

    private final AviatorEvaluatorInstance aviator;

    /**
     * SPI 专用无参构造器，使用默认配置（LRU 缓存 4096 条）。
     */
    public AviatorParamExpressionEngine() {
        this(null);
    }

    /**
     * 带配置构造器，供 Spring Boot Starter 注入定制参数时使用。
     *
     * @param config Aviator 配置项，传 null 时退回默认配置
     */
    public AviatorParamExpressionEngine(StepFlowAviatorConfigProperties config) {
        if (config == null) {
            config = new StepFlowAviatorConfigProperties();
        }
        if (config.getUseLRUExpressionCache() == null) {
            // 参数取值表达式数量通常多于业务表达式，缓存适当调大
            config.setUseLRUExpressionCache(4096);
        }
        this.aviator = AviatorInstanceBuilder.buildAviatorEvaluatorInstance(config);
    }

    /**
     * 通过表达式从变量映射中取值（如 "dto.userId"、"context.result"）。
     *
     * @param expression 取值表达式
     * @param vars       变量上下文
     * @return 表达式求值结果
     */
    @Override
    public Object getParam(String expression, Map<String, Object> vars) {
        return aviator.execute(expression, vars);
    }
}
