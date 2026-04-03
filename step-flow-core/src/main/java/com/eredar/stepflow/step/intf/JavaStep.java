package com.eredar.stepflow.step.intf;

import com.eredar.stepflow.dto.OneOffParams;
import com.eredar.stepflow.dto.StepFlowContext;
import com.eredar.stepflow.step.dto.StepData;

/**
 * Java 步骤使用的 Java 方法
 */
public interface JavaStep {

    Object invoke(StepData stepData, StepFlowContext stepFlowContext, OneOffParams oneOffParams);
}
