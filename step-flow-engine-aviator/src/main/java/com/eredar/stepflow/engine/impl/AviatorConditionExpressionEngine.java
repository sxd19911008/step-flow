package com.eredar.stepflow.engine.impl;

import com.eredar.stepflow.engine.ConditionExpressionEngine;
import com.eredar.stepflow.engine.aviator.AviatorInstanceBuilder;
import com.eredar.stepflow.engine.aviator.dto.StepFlowAviatorConfigProperties;
import com.eredar.stepflow.exception.StepFlowException;
import com.googlecode.aviator.AviatorEvaluatorInstance;

import java.util.Map;

/**
 * 基于 AviatorScript 的条件表达式引擎实现。
 *
 * <p>无参构造器供 Java SPI（ServiceLoader）在非 Spring 环境下自动实例化使用，
 * 此时使用内置默认配置（LRU 缓存 2048）。
 * 带参构造器供 Spring Boot AutoConfiguration 注入定制配置使用。
 */
public class AviatorConditionExpressionEngine implements ConditionExpressionEngine {

    private final AviatorEvaluatorInstance aviator;

    /**
     * SPI 专用无参构造器，使用默认配置（LRU 缓存 2048 条）。
     */
    public AviatorConditionExpressionEngine() {
        this(null);
    }

    /**
     * 带配置构造器，供 Spring Boot Starter 注入定制参数时使用。
     *
     * @param config Aviator 配置项，传 null 时退回默认配置
     */
    public AviatorConditionExpressionEngine(StepFlowAviatorConfigProperties config) {
        if (config == null) {
            config = new StepFlowAviatorConfigProperties();
        }
        if (config.getMaxExpressionCache() == null) {
            config.setMaxExpressionCache(2048);
        }
        this.aviator = AviatorInstanceBuilder.buildAviatorEvaluatorInstance(config);
    }

    /**
     * 执行条件表达式，结果必须是布尔值。
     *
     * @param expression 条件表达式（如 "age >= 18 && score > 60"）
     * @param vars       表达式中引用的变量
     * @return 条件判断结果 true / false
     * @throws StepFlowException 表达式结果为 null 或非布尔类型时抛出
     */
    @Override
    public Boolean isTrue(String expression, Map<String, Object> vars) {
        Object res = aviator.execute(expression, vars);
        if (res instanceof Boolean) {
            return (Boolean) res;
        } else if (res == null) {
            throw new StepFlowException(String.format("执行条件表达式[%s]，返回null", expression));
        } else {
            throw new StepFlowException(String.format("执行条件表达式[%s]，返回错误类型：%s", expression, res.getClass().getName()));
        }
    }
}
