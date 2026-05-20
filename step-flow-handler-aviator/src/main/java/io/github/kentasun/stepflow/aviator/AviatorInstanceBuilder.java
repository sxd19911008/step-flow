package io.github.kentasun.stepflow.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Options;
import io.github.kentasun.stepflow.aviator.dto.AviatorStepHandlerProperties;

import java.math.MathContext;
import java.math.RoundingMode;

/**
 * AviatorScript 引擎实例工厂，集中管理所有 Aviator 选项配置
 */
public class AviatorInstanceBuilder {

    /**
     * 根据配置构建一个独立的 {@link AviatorEvaluatorInstance}。
     *
     * @param config 引擎配置项，传入 null 时所有选项使用内置默认值
     * @return 配置完毕、可直接使用的 AviatorEvaluatorInstance
     */
    public static AviatorEvaluatorInstance buildAviatorEvaluatorInstance(AviatorStepHandlerProperties config) {
        if (config == null) {
            config = new AviatorStepHandlerProperties();
        }

        // 每次创建独立实例，避免全局状态污染
        AviatorEvaluatorInstance aviator = AviatorEvaluator.newInstance();

        // 编译模式：表达式被直接翻译成 Java 字节码，性能最优
        aviator.setOption(Options.OPTIMIZE_LEVEL, AviatorEvaluator.COMPILE);

        // 开启 LRU 缓存策略，避免重复编译相同表达式
        aviator.setCachedExpressionByDefault(true);

        // 设置最大缓存表达式数量
        aviator.useLRUExpressionCache(defaultIfNull(config.getUseLRUExpressionCache(), 2048));

        // decimal 数字精度，四舍五入，精度 40 位
        aviator.setOption(Options.MATH_CONTEXT, new MathContext(40, RoundingMode.HALF_UP));

        // 浮点数统一使用 BigDecimal，避免精度丢失
        aviator.setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);

        // 单次脚本执行超时时间（毫秒），防止恶意/死循环表达式长期占用线程
        aviator.setOption(Options.EVAL_TIMEOUT_MS, 5000L);

        // 最大循环次数，防止死循环
        aviator.setOption(Options.MAX_LOOP_COUNT, defaultIfNull(config.getMaxLoopCount(), 10000));

        // 调试日志，生产环境建议关闭
        aviator.setOption(Options.TRACE_EVAL, defaultIfNull(config.getTraceEval(), Boolean.FALSE));

        return aviator;
    }

    /**
     * 默认值
     *
     * @param object       目标对象
     * @param defaultValue 默认值对象
     * @param <T>          目标对象的类型
     * @return 如果 {@code object} 不为 {@code null} 则返回 {@code object}；反之返回 {@code defaultValue}
     */
    private static <T> T defaultIfNull(final T object, final T defaultValue) {
        return object != null ? object : defaultValue;
    }
}
