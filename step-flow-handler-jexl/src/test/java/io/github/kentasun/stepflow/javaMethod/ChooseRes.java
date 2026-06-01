package io.github.kentasun.stepflow.javaMethod;

import io.github.kentasun.stepflow.api.dto.OneOffParams;
import io.github.kentasun.stepflow.api.step.AbstractJavaStep;
import io.github.kentasun.stepflow.api.step.dto.StepData;

import java.math.BigDecimal;

public class ChooseRes extends AbstractJavaStep {

    @Override
    public Object invoke(StepData stepData, OneOffParams oneOffParams) {
        BigDecimal calcMultiply = this.getAs(oneOffParams, "calc_multiply");
        BigDecimal calcDivide = this.getAs(oneOffParams, "calc_divide");

        if (calcMultiply == null) {
            return calcDivide;
        } else {
            return calcMultiply;
        }
    }
}
