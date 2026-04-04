package com.eredar.stepflow.engine.impl;

import com.eredar.stepflow.engine.BusinessExpressionEngine;
import com.eredar.stepflow.engine.aviator.*;
import com.eredar.stepflow.engine.aviator.function.AddFunction;
import com.eredar.stepflow.engine.aviator.function.DivFunction;
import com.eredar.stepflow.engine.aviator.function.MultFunction;
import com.eredar.stepflow.engine.aviator.function.SubFunction;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Options;
import com.googlecode.aviator.lexer.token.OperatorType;

import java.util.Map;

/**
 * 基于 Aviator 框架的 业务表达式引擎
 */
public class AviatorBusinessExpressionEngine implements BusinessExpressionEngine {

    private final AviatorEvaluatorInstance aviator;

    public AviatorBusinessExpressionEngine() {
        // 创建新的实例
        aviator = AviatorEvaluator.newInstance();

        // 编译模式，表达式被直接翻译成 Java 字节码
        aviator.setOption(Options.OPTIMIZE_LEVEL, AviatorEvaluator.COMPILE);

        // 开启 LRU 缓存策略
        aviator.setCachedExpressionByDefault(true);

        // 设置最大缓存表达式数量
        aviator.useLRUExpressionCache(2048);

        // 添加工具方法
        try {
            aviator.importFunctions(Utils.class);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        // 语法糖配置：允许 policyInfo.applyDate 这种写法
        aviator.setOption(Options.ENABLE_PROPERTY_SYNTAX_SUGAR, true);

        // 容错配置：true：当访问对象属性为 null 时不抛异常，返回 null；false：直接报错。
        aviator.setOption(Options.NIL_WHEN_PROPERTY_NOT_FOUND, false);

        // 设置全局脚本执行超时时间为 5 秒。默认值为 0，表示永不超时
        aviator.setOption(Options.EVAL_TIMEOUT_MS, 5000L);

        // 循环次数限制：防止死循环
        aviator.setOption(Options.MAX_LOOP_COUNT, 10000);

        // 自定义算术运算
        aviator.addOpFunction(OperatorType.ADD, new AddFunction());
        aviator.addOpFunction(OperatorType.SUB, new SubFunction());
        aviator.addOpFunction(OperatorType.MULT, new MultFunction());
        aviator.addOpFunction(OperatorType.DIV, new DivFunction());

        // 自定义转换器

        // 调试日志
        // instance.setOption(Options.TRACE_EVAL, true);
    }

    /**
     * 执行计算表达式
     *
     * @param expression 计算表达式
     * @param vars 表达式参数
     * @return 表达式结果
     */
    @Override
    public Object execute(String expression, Map<String, Object> vars) {
        return aviator.execute(expression, vars);
    }
}
