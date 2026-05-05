package com.eredar.stepflow.javaMethod;

import com.eredar.stepflow.dto.OneOffParams;
import com.eredar.stepflow.dto.StepFlowContext;
import com.eredar.stepflow.step.dto.StepData;
import com.eredar.stepflow.step.intf.JavaStep;
import com.eredar.stepflow.utils.StepFlowUtils;

import java.math.BigDecimal;

public class ChooseRes implements JavaStep {

    @Override
    public Object invoke(StepData stepData, StepFlowContext stepFlowContext, OneOffParams oneOffParams) {
        BigDecimal calcMultiply = StepFlowUtils.getValByMap("calc_multiply", stepFlowContext.getContextMap(), BigDecimal.class);
        BigDecimal calcDivide = StepFlowUtils.getValByMap("calc_divide", stepFlowContext.getContextMap(), BigDecimal.class);

        if (calcMultiply == null) {
            return calcDivide;
        } else {
            return calcMultiply;
        }
    }
}
