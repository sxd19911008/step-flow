package io.github.kentasun.stepflow.javaMethod;

import io.github.kentasun.stepflow.api.dto.OneOffParams;
import io.github.kentasun.stepflow.api.step.JavaStep;
import io.github.kentasun.stepflow.api.step.dto.StepData;
import io.github.kentasun.stepflow.utils.StepFlowUtils;

import java.math.BigDecimal;

public class ChooseRes implements JavaStep {

    @Override
    public Object invoke(StepData stepData, OneOffParams oneOffParams) {
        BigDecimal calcMultiply = StepFlowUtils.getValByMap("calc_multiply", oneOffParams.getVars(), BigDecimal.class);
        BigDecimal calcDivide = StepFlowUtils.getValByMap("calc_divide", oneOffParams.getVars(), BigDecimal.class);

        if (calcMultiply == null) {
            return calcDivide;
        } else {
            return calcMultiply;
        }
    }
}
