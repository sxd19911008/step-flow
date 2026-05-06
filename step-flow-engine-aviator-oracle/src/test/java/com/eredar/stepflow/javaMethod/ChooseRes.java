package com.eredar.stepflow.javaMethod;

import com.eredar.aviatororacle.number.OraDecimal;
import com.eredar.stepflow.dto.OneOffParams;
import com.eredar.stepflow.dto.StepFlowContext;
import com.eredar.stepflow.step.dto.StepData;
import com.eredar.stepflow.step.intf.JavaStep;
import com.eredar.stepflow.utils.StepFlowUtils;

public class ChooseRes implements JavaStep {

    @Override
    public Object invoke(StepData stepData, StepFlowContext stepFlowContext, OneOffParams oneOffParams) {
        OraDecimal calcMultiply = StepFlowUtils.getValByMap("calc_multiply", stepFlowContext.getContextMap(), OraDecimal.class);
        OraDecimal calcDivide = StepFlowUtils.getValByMap("calc_divide", stepFlowContext.getContextMap(), OraDecimal.class);

        if (calcMultiply == null) {
            return calcDivide;
        } else {
            return calcMultiply;
        }
    }
}
