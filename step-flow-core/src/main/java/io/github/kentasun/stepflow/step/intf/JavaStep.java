package io.github.kentasun.stepflow.step.intf;

import io.github.kentasun.stepflow.dto.OneOffParams;
import io.github.kentasun.stepflow.dto.StepFlowContext;
import io.github.kentasun.stepflow.step.dto.StepData;

/**
 * Java 步骤使用的 Java 方法
 */
public interface JavaStep {

    Object invoke(StepData stepData, StepFlowContext stepFlowContext, OneOffParams oneOffParams);
}
