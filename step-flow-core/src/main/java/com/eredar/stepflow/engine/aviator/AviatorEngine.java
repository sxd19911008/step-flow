package com.eredar.stepflow.engine.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Options;
import com.googlecode.aviator.lexer.token.OperatorType;

import java.util.Map;

/**
 * Aviator 表达式引擎
 */
public class AviatorEngine {

    private static final AviatorEvaluatorInstance businessEngine;
    private static final AviatorEvaluatorInstance paramMappingEngine;

    static {
        /* ---------- 业务表达式引擎 begin ---------- */
        businessEngine = AviatorEvaluator.getInstance();

        // 编译模式，表达式被直接翻译成 Java 字节码
        businessEngine.setOption(Options.OPTIMIZE_LEVEL, AviatorEvaluator.COMPILE);

        // 开启 LRU 缓存策略
        businessEngine.setCachedExpressionByDefault(true);

        // 设置最大缓存表达式数量
        businessEngine.useLRUExpressionCache(2048);

        // 添加工具方法
        try {
            businessEngine.importFunctions(Utils.class);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        // 语法糖配置：允许 policyInfo.applyDate 这种写法
        businessEngine.setOption(Options.ENABLE_PROPERTY_SYNTAX_SUGAR, true);

        // 容错配置：true：当访问对象属性为 null 时不抛异常，返回 null；false：直接报错。
        businessEngine.setOption(Options.NIL_WHEN_PROPERTY_NOT_FOUND, false);

        // 设置全局脚本执行超时时间为 5 秒。默认值为 0，表示永不超时
        businessEngine.setOption(Options.EVAL_TIMEOUT_MS, 5000L);

        // 循环次数限制：防止死循环
        businessEngine.setOption(Options.MAX_LOOP_COUNT, 10000);

        // 自定义算术运算
        businessEngine.addOpFunction(OperatorType.ADD, new AddFunction());
        businessEngine.addOpFunction(OperatorType.SUB, new SubFunction());
        businessEngine.addOpFunction(OperatorType.MULT, new MultFunction());
        businessEngine.addOpFunction(OperatorType.DIV, new DivFunction());

        // 调试日志
        // instance.setOption(Options.TRACE_EVAL, true);
        /* ---------- 业务表达式引擎 end ---------- */

        /* ---------- 参数映射引擎 begin ---------- */
        paramMappingEngine = AviatorEvaluator.newInstance();

        // 编译模式，表达式被直接翻译成 Java 字节码
        paramMappingEngine.setOption(Options.OPTIMIZE_LEVEL, AviatorEvaluator.COMPILE);

        // 开启 LRU 缓存策略
        paramMappingEngine.setCachedExpressionByDefault(true);

        // 设置最大缓存表达式数量
        paramMappingEngine.useLRUExpressionCache(2048);

        // 语法糖配置：允许 policyInfo.applyDate 这种写法
        paramMappingEngine.setOption(Options.ENABLE_PROPERTY_SYNTAX_SUGAR, true);

        // 容错配置：true：当访问对象属性为 null 时不抛异常，返回 null；false：直接报错。
        paramMappingEngine.setOption(Options.NIL_WHEN_PROPERTY_NOT_FOUND, true);
        /* ---------- 参数映射引擎 end ---------- */
    }


    /**
     * 【核心入口】计算表达式
     *
     * @param expression 表达式
     * @param vars       参数集合
     * @return 计算结果
     */
    public static Object execute(String expression, Map<String, Object> vars) {
        return businessEngine.execute(expression, vars);
    }

    /**
     * `policyInfo.applyDate`类型的参数获取
     *
     * @param expression 表达式
     * @param vars       参数集合
     * @return 计算结果
     */
    public static Object getParam(String expression, Map<String, Object> vars) {
        return paramMappingEngine.execute(expression, vars);
    }
}
