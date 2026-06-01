package io.github.kentasun.stepflow.javaMethod;

import io.github.kentasun.stepflow.api.dto.OneOffParams;
import io.github.kentasun.stepflow.api.step.AbstractJavaStep;
import io.github.kentasun.stepflow.api.step.dto.StepData;

import java.math.BigDecimal;

/**
 * 对应原 Aviator 条件表达式 {@code calc_add > 100 && calc_subtract < 100} 的 Java 实现。
 * <p>用于 IF_ELSE 的 IF/ELSIF 条件判断，返回 {@link Boolean}。</p>
 */
public class Condition1 extends AbstractJavaStep {

    private static final BigDecimal THRESHOLD = new BigDecimal("100");

    @Override
    public Object invoke(StepData stepData, OneOffParams oneOffParams) {
        BigDecimal calcAdd = this.getAs(oneOffParams, "calc_add", true);
        BigDecimal calcSubtract = this.getAs(oneOffParams, "calc_subtract", true);
        return calcAdd.compareTo(THRESHOLD) > 0 && calcSubtract.compareTo(THRESHOLD) < 0;
    }
}
