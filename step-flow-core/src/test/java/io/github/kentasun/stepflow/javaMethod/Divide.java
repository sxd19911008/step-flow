package io.github.kentasun.stepflow.javaMethod;

import io.github.kentasun.stepflow.api.dto.OneOffParams;
import io.github.kentasun.stepflow.api.step.AbstractJavaStep;
import io.github.kentasun.stepflow.api.step.dto.StepData;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 对应原 Aviator 表达式 {@code a / b} 的 Java 实现。
 * <p>使用 {@link BigDecimal#divide(BigDecimal, int, RoundingMode)} 避免除不尽时抛异常。</p>
 */
public class Divide extends AbstractJavaStep {

    @Override
    public Object invoke(StepData stepData, OneOffParams oneOffParams) {
        BigDecimal a = this.getAs(oneOffParams, "a", true);
        BigDecimal b = this.getAs(oneOffParams, "b", true);
        return a.divide(b, 10, RoundingMode.HALF_UP);
    }
}
