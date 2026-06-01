package io.github.kentasun.stepflow.javaMethod;

import io.github.kentasun.stepflow.api.dto.OneOffParams;
import io.github.kentasun.stepflow.api.step.AbstractJavaStep;
import io.github.kentasun.stepflow.api.step.dto.StepData;

import java.math.BigDecimal;

/**
 * 对应原 Aviator 表达式 {@code a * b} 的 Java 实现。
 */
public class Multiply extends AbstractJavaStep {

    @Override
    public Object invoke(StepData stepData, OneOffParams oneOffParams) {
        BigDecimal a = this.getAs(oneOffParams, "a", true);
        BigDecimal b = this.getAs(oneOffParams, "b", true);
        return a.multiply(b);
    }
}
